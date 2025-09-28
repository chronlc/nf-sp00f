package com.example.nfsp00f.emulation

/** EMV Attack Emulation Manager Coordinates attack modules and manages emulation profiles */
class EmvAttackEmulationManager private constructor() {

  private var activeProfile: EmulationProfile? = null
  private var isEmulating = false

  companion object {
    @Volatile private var INSTANCE: EmvAttackEmulationManager? = null

    fun getInstance(): EmvAttackEmulationManager {
      return INSTANCE
              ?: synchronized(this) {
                INSTANCE ?: EmvAttackEmulationManager().also { INSTANCE = it }
              }
    }
  }

  /** Start emulation with specified profile */
  fun startEmulation(profileType: EmulationProfile.ProfileType): Boolean {
    return try {
      stopEmulation() // Stop any existing emulation

      activeProfile = EmulationProfiles.createProfile(profileType)

      // Configure HCE service
      EnhancedHceService.setEmulationProfile(activeProfile)

      isEmulating = true
      true
    } catch (e: Exception) {
      false
    }
  }

  /** Stop current emulation */
  fun stopEmulation() {
    isEmulating = false
    activeProfile = null
    EnhancedHceService.setEmulationProfile(null)
  }

  /** Get current emulation status */
  fun isEmulating(): Boolean = isEmulating

  /** Get active profile */
  fun getActiveProfile(): EmulationProfile? = activeProfile

  /** Get available profiles */
  fun getAvailableProfiles(): List<EmulationProfile.ProfileType> {
    return EmulationProfile.ProfileType.values().toList()
  }
}
