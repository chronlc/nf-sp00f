package com.example.nfsp00f.manager

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.example.nfsp00f.data.*
import kotlinx.coroutines.flow.*
import java.io.File
import java.util.concurrent.ConcurrentHashMap

/**
 * Unified Card Profile Manager
 * Singleton for managing EMV card profiles with real-time UI updates
 */
class CardProfileManager private constructor(private val context: Context) {
    
    companion object {
        @Volatile
        private var INSTANCE: CardProfileManager? = null
        
        fun getInstance(context: Context): CardProfileManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: CardProfileManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()
    private val storageDir = File(context.filesDir, "card_profiles")
    
    // Thread-safe storage
    private val cardProfiles = ConcurrentHashMap<String, CardProfile>()
    private val recentApduLogs = ConcurrentHashMap<String, MutableList<ApduLogEntry>>()
    
    // Reactive state streams
    private val _profilesFlow = MutableStateFlow<List<CardProfile>>(emptyList())
    val profilesFlow: StateFlow<List<CardProfile>> = _profilesFlow.asStateFlow()
    
    private val _recentActivityFlow = MutableStateFlow<List<ApduLogEntry>>(emptyList())
    val recentActivityFlow: StateFlow<List<ApduLogEntry>> = _recentActivityFlow.asStateFlow()
    
    init {
        storageDir.mkdirs()
        loadProfiles()
    }
    
    /**
     * Add or update card profile
     */
    fun addOrUpdateCard(cardData: EmvCardData): CardProfile {
        val existingProfile = cardProfiles[cardData.cardUid]
        val profile = if (existingProfile != null) {
            // Update existing profile
            val updatedCardData = cardData.copy(
                timestampFirstSeen = existingProfile.emvCardData.timestampFirstSeen,
                timestampUpdated = System.currentTimeMillis()
            )
            existingProfile.copy(emvCardData = updatedCardData)
        } else {
            // Create new profile
            CardProfile(emvCardData = cardData)
        }
        
        cardProfiles[profile.emvCardData.cardUid] = profile
        
        // Update APDU logs
        if (cardData.apduLog.isNotEmpty()) {
            val logs = recentApduLogs.getOrPut(profile.emvCardData.cardUid) { mutableListOf() }
            logs.addAll(cardData.apduLog)
            
            // Keep only recent logs (last 100 entries per card)
            if (logs.size > 100) {
                logs.subList(0, logs.size - 100).clear()
            }
        }
        
        // Update reactive streams
        updateFlows()
        
        // Persist to storage
        saveProfile(profile)
        
        return profile
    }
    
    /**
     * Get all profiles
     */
    fun getAllProfiles(): List<CardProfile> {
        return cardProfiles.values.toList()
    }
    
    /**
     * Get profile by ID
     */
    fun getProfileById(id: String): CardProfile? {
        return cardProfiles[id]
    }
    
    /**
     * Delete profile
     */
    fun deleteProfile(id: String): Boolean {
        val removed = cardProfiles.remove(id) != null
        if (removed) {
            recentApduLogs.remove(id)
            deleteProfileFile(id)
            updateFlows()
        }
        return removed
    }
    
    /**
     * Search profiles by criteria
     */
    fun searchProfiles(
        query: String = "",
        cardType: String = "",
        tags: List<String> = emptyList()
    ): List<CardProfile> {
        return cardProfiles.values.filter { profile ->
            val matchesQuery = if (query.isEmpty()) true else {
                profile.getDisplayName().contains(query, ignoreCase = true) ||
                profile.emvCardData.pan.contains(query, ignoreCase = true) ||
                profile.emvCardData.cardholderName.contains(query, ignoreCase = true)
            }
            
            val matchesType = if (cardType.isEmpty()) true else {
                profile.getCardBrand().equals(cardType, ignoreCase = true)
            }
            
            val matchesTags = if (tags.isEmpty()) true else {
                tags.all { tag -> profile.tags.contains(tag) }
            }
            
            matchesQuery && matchesType && matchesTags
        }
    }
    
    /**
     * Export profiles to JSON
     */
    fun exportProfilesToJson(): String {
        return gson.toJson(cardProfiles.values.toList())
    }
    
    /**
     * Import profiles from JSON
     */
    fun importProfilesFromJson(json: String): Boolean {
        return try {
            val profiles = gson.fromJson(json, Array<CardProfile>::class.java)
            profiles.forEach { profile ->
                cardProfiles[profile.emvCardData.cardUid] = profile
                saveProfile(profile)
            }
            updateFlows()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Get statistics
     */
    fun getStats(): ProfileStats {
        val profiles = cardProfiles.values
        val totalLogs = recentApduLogs.values.sumOf { it.size }
        
        return ProfileStats(
            totalProfiles = profiles.size,
            totalApduLogs = totalLogs,
            cardBrands = profiles.groupingBy { it.getCardBrand() }.eachCount(),
            averageTagsPerCard = if (profiles.isNotEmpty()) {
                profiles.sumOf { it.emvCardData.emvTags.size } / profiles.size.toDouble()
            } else 0.0
        )
    }
    
    private fun updateFlows() {
        _profilesFlow.value = cardProfiles.values.toList()
        _recentActivityFlow.value = recentApduLogs.values.flatten().takeLast(50)
    }
    
    private fun loadProfiles() {
        storageDir.listFiles()?.forEach { file ->
            try {
                val json = file.readText()
                val profile = gson.fromJson(json, CardProfile::class.java)
                cardProfiles[profile.emvCardData.cardUid] = profile
            } catch (e: Exception) {
                // Skip corrupted files
            }
        }
        updateFlows()
    }
    
    private fun saveProfile(profile: CardProfile) {
        try {
            val file = File(storageDir, "${profile.emvCardData.cardUid}.json")
            file.writeText(gson.toJson(profile))
        } catch (e: Exception) {
            // Handle save error
        }
    }
    
    private fun deleteProfileFile(id: String) {
        File(storageDir, "$id.json").delete()
    }
}

/**
 * Statistics container
 */
data class ProfileStats(
    val totalProfiles: Int,
    val totalApduLogs: Int,
    val cardBrands: Map<String, Int>,
    val averageTagsPerCard: Double
)
