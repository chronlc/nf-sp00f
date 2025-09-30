package com.nf_sp00f.app.data

import java.text.SimpleDateFormat
import java.util.*

/**
 * Card Profile for persistent storage and UI representation
 * Wraps EmvCardData with additional metadata and presentation helpers
 */
data class CardProfile(
    val id: String = UUID.randomUUID().toString(),
    val createdTimestamp: Long = System.currentTimeMillis(),
    val updatedTimestamp: Long = System.currentTimeMillis(),
    val emvCardData: EmvCardData,
    val apduLogs: MutableList<ApduLogEntry> = mutableListOf(),
    
    // UI-specific metadata
    val nickname: String = "",
    val notes: String = "",
    val isFavorite: Boolean = false,
    val tags: List<String> = listOf(),
    
    // Analysis metadata
    val analysisScore: Double = 0.0,
    val vulnerabilityFlags: List<String> = listOf(),
    val lastAnalysisTimestamp: Long = 0L
) {
    
    // Derived UI properties for easy access
    val cardholderName: String
        get() = emvCardData.cardholderName.ifEmpty { "Unknown Cardholder" }
    
    val applicationLabel: String
        get() = emvCardData.applicationLabel.ifEmpty { emvCardData.detectCardType() }
    
    val expirationDate: String
        get() = emvCardData.getFormattedExpiryDate().ifEmpty { "N/A" }
    
    val maskedPan: String
        get() = emvCardData.getMaskedPan().ifEmpty { "Unknown PAN" }
    
    val unmaskedPan: String
        get() = emvCardData.getUnmaskedPan()
    
    /**
     * Get display name for UI (nickname or cardholder name)
     */
    fun getDisplayName(): String {
        return when {
            nickname.isNotEmpty() -> nickname
            cardholderName != "Unknown Cardholder" -> cardholderName
            applicationLabel.isNotEmpty() -> "$applicationLabel Card"
            else -> "Card ${id.take(8)}"
        }
    }
    
    /**
     * Get card brand/type for UI icons
     */
    fun getCardBrand(): String = emvCardData.detectCardType()
    
    /**
     * Get formatted creation date
     */
    fun getFormattedCreatedDate(): String {
        val date = Date(createdTimestamp)
        val format = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        return format.format(date)
    }
    
    /**
     * Get formatted last updated date
     */
    fun getFormattedUpdatedDate(): String {
        val date = Date(updatedTimestamp)
        val format = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
        return format.format(date)
    }
    
    /**
     * Get summary statistics for UI display
     */
    fun getStats(): CardStats {
        return CardStats(
            totalApduExchanges = apduLogs.size,
            successfulCommands = apduLogs.count { it.isSuccess() },
            averageExecutionTimeMs = if (apduLogs.isNotEmpty()) {
                apduLogs.map { it.executionTimeMs }.average().toLong()
            } else 0L,
            emvTagsExtracted = emvCardData.emvTags.count { it.value.isNotEmpty() },
            attackSurfaceScore = emvCardData.calculateAttackSurfaceScore(),
            isContactlessCapable = emvCardData.isContactlessCapable()
        )
    }
    
    /**
     * Get quick summary for list display
     */
    fun getSummary(): String {
        val stats = getStats()
        return buildString {
            append("${getCardBrand()} • ")
            append("${stats.emvTagsExtracted} tags • ")
            append("${stats.totalApduExchanges} APDUs")
            if (emvCardData.isContactlessCapable()) {
                append(" • Contactless")
            }
        }
    }
    
    /**
     * Add APDU log entry and update timestamp
     */
    fun addApduLog(logEntry: ApduLogEntry): CardProfile {
        val updatedLogs = apduLogs.toMutableList()
        updatedLogs.add(logEntry)
        
        return this.copy(
            apduLogs = updatedLogs,
            updatedTimestamp = System.currentTimeMillis()
        )
    }
    
    /**
     * Update EMV data and refresh timestamp
     */
    fun updateEmvData(newEmvData: EmvCardData): CardProfile {
        return this.copy(
            emvCardData = newEmvData,
            updatedTimestamp = System.currentTimeMillis()
        )
    }
    
    /**
     * Update analysis results
     */
    fun updateAnalysis(score: Double, vulnerabilities: List<String>): CardProfile {
        return this.copy(
            analysisScore = score,
            vulnerabilityFlags = vulnerabilities,
            lastAnalysisTimestamp = System.currentTimeMillis(),
            updatedTimestamp = System.currentTimeMillis()
        )
    }
    
    /**
     * Check if profile needs analysis update (older than 1 hour)
     */
    fun needsAnalysisUpdate(): Boolean {
        val oneHourAgo = System.currentTimeMillis() - (60 * 60 * 1000)
        return lastAnalysisTimestamp < oneHourAgo
    }
    
    /**
     * Get attack compatibility assessment
     */
    fun getAttackCompatibility(): Map<String, AttackCompatibility> {
        return mapOf(
            "PPSE_POISONING" to assessPpsePoisoning(),
            "AIP_FORCE_OFFLINE" to assessAipForceOffline(),
            "TRACK2_SPOOFING" to assessTrack2Spoofing(),
            "CRYPTOGRAM_DOWNGRADE" to assessCryptogramDowngrade(),
            "CVM_BYPASS" to assessCvmBypass()
        )
    }
    
    private fun assessPpsePoisoning(): AttackCompatibility {
        val hasMultipleAids = emvCardData.availableAids.size > 1
        val hasPpseData = emvCardData.emvTags["6F"]?.isNotEmpty() == true
        
        return AttackCompatibility(
            isCompatible = hasMultipleAids && hasPpseData,
            confidence = if (hasMultipleAids && hasPpseData) 0.9 else 0.2,
            requirements = listOf("Multiple AIDs", "PPSE response data"),
            reasoning = "PPSE poisoning requires multiple AID support and PPSE template data"
        )
    }
    
    private fun assessAipForceOffline(): AttackCompatibility {
        val hasAip = emvCardData.applicationInterchangeProfile.isNotEmpty()
        val supportsOffline = emvCardData.isContactlessCapable()
        
        return AttackCompatibility(
            isCompatible = hasAip && supportsOffline,
            confidence = if (hasAip && supportsOffline) 0.8 else 0.3,
            requirements = listOf("AIP data", "Offline capability"),
            reasoning = "AIP manipulation requires existing AIP and offline processing support"
        )
    }
    
    private fun assessTrack2Spoofing(): AttackCompatibility {
        val hasTrack2 = emvCardData.track2Data.isNotEmpty()
        val hasPan = emvCardData.pan.isNotEmpty()
        
        return AttackCompatibility(
            isCompatible = hasTrack2 || hasPan,
            confidence = if (hasTrack2) 0.95 else if (hasPan) 0.7 else 0.1,
            requirements = listOf("Track 2 data or PAN"),
            reasoning = "Track 2 spoofing requires magnetic stripe data or PAN for reconstruction"
        )
    }
    
    private fun assessCryptogramDowngrade(): AttackCompatibility {
        val hasCryptogram = emvCardData.applicationCryptogram.isNotEmpty()
        val hasCdol = emvCardData.cdol1.isNotEmpty() || emvCardData.cdol2.isNotEmpty()
        
        return AttackCompatibility(
            isCompatible = hasCryptogram && hasCdol,
            confidence = if (hasCryptogram && hasCdol) 0.75 else 0.15,
            requirements = listOf("Application cryptogram", "CDOL data"),
            reasoning = "Cryptogram attacks require existing AC and CDOL for manipulation"
        )
    }
    
    private fun assessCvmBypass(): AttackCompatibility {
        val hasCvmList = emvCardData.emvTags["8E"]?.isNotEmpty() == true
        val hasCvmResults = emvCardData.emvTags["9F34"]?.isNotEmpty() == true
        
        return AttackCompatibility(
            isCompatible = hasCvmList || hasCvmResults,
            confidence = if (hasCvmList && hasCvmResults) 0.85 else if (hasCvmList) 0.6 else 0.25,
            requirements = listOf("CVM List or CVM Results"),
            reasoning = "CVM bypass requires cardholder verification method data"
        )
    }
    
    /**
     * Export to JSON for backup/sharing
     */
    fun toJson(): String {
        // Note: In production, use proper JSON serialization library like Gson/Moshi
        return """
        {
            "id": "$id",
            "createdTimestamp": $createdTimestamp,
            "updatedTimestamp": $updatedTimestamp,
            "nickname": "$nickname",
            "notes": "$notes",
            "isFavorite": $isFavorite,
            "tags": [${tags.joinToString(",") { "\"$it\"" }}],
            "emvCardData": {
                "pan": "${emvCardData.pan}",
                "cardholderName": "${emvCardData.cardholderName}",
                "applicationLabel": "${emvCardData.applicationLabel}",
                "expiryDate": "${emvCardData.expiryDate}",
                "cardBrand": "${getCardBrand()}"
            },
            "stats": {
                "totalApduExchanges": ${getStats().totalApduExchanges},
                "emvTagsExtracted": ${getStats().emvTagsExtracted},
                "attackSurfaceScore": ${getStats().attackSurfaceScore}
            }
        }
        """.trimIndent()
    }
}

/**
 * Statistics container for card profile analysis
 */
data class CardStats(
    val totalApduExchanges: Int,
    val successfulCommands: Int,
    val averageExecutionTimeMs: Long,
    val emvTagsExtracted: Int,
    val attackSurfaceScore: Double,
    val isContactlessCapable: Boolean
)

/**
 * Attack compatibility assessment result
 */
data class AttackCompatibility(
    val isCompatible: Boolean,
    val confidence: Double,  // 0.0 to 1.0
    val requirements: List<String>,
    val reasoning: String
)
