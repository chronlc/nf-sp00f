package com.nf_sp00f.app.emulation

import android.nfc.cardemulation.HostApduService
import android.os.Bundle
<<<<<<< HEAD
import android.util.Log
import com.nf_sp00f.app.data.ApduLogEntry
import java.util.*

/**
 * Production-grade Enhanced HCE Service for EMV Attack Emulation Implements dynamic APDU processing
 * with comprehensive EMV protocol support
 */
class EnhancedHceService : HostApduService() {

  private var currentEmulationProfile: EmulationProfile? = null
  private val apduLog = mutableListOf<ApduLogEntry>()
  private var sessionStartTime = System.currentTimeMillis()

  companion object {
    private const val TAG = "EnhancedHceService"

    // EMV Status Words
    private const val SW_SUCCESS = "9000"
    private const val SW_WRONG_LENGTH = "6700"
    private const val SW_WRONG_P1_P2 = "6A86"
    private const val SW_INSTRUCTION_NOT_SUPPORTED = "6D00"
    private const val SW_CLASS_NOT_SUPPORTED = "6E00"
    private const val SW_FILE_NOT_FOUND = "6A82"

    // EMV Command Classes and Instructions
    private const val CLA_ISO = 0x00.toByte()
    private const val CLA_EMV = 0x80.toByte()
    private const val INS_SELECT = 0xA4.toByte()
    private const val INS_GET_PROCESSING_OPTIONS = 0xA8.toByte()
    private const val INS_READ_RECORD = 0xB2.toByte()
    private const val INS_GENERATE_AC = 0xAE.toByte()
    private const val INS_GET_DATA = 0xCA.toByte()
    private const val INS_VERIFY = 0x20.toByte()

    // Callback interface for UI updates
    interface HceServiceCallback {
      fun onApduReceived(logEntry: ApduLogEntry)
      fun onEmulationStarted(profileName: String)
      fun onEmulationStopped()
      fun onError(error: String)
    }

    private var callback: HceServiceCallback? = null

    fun setCallback(cb: HceServiceCallback?) {
      callback = cb
    }

    fun setEmulationProfile(profile: EmulationProfile?) {
      // Static reference for service access
      currentProfile = profile
    }

    private var currentProfile: EmulationProfile? = null
  }

  override fun onCreate() {
    super.onCreate()
    Log.d(TAG, "EnhancedHceService created")
    sessionStartTime = System.currentTimeMillis()
    callback?.onEmulationStarted(currentProfile?.name ?: "Unknown")
  }

  override fun onDestroy() {
    super.onDestroy()
    Log.d(TAG, "EnhancedHceService destroyed")
    callback?.onEmulationStopped()
  }

  override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
    val startTime = System.currentTimeMillis()

    if (commandApdu == null || commandApdu.isEmpty()) {
      return hexToBytes(SW_WRONG_LENGTH)
    }

    // Log incoming APDU
    val commandHex = bytesToHex(commandApdu)
    Log.d(TAG, "Received APDU: $commandHex")

    val response =
            try {
              // Use current profile or fallback to default
              val profile = currentProfile ?: EmulationProfiles.getDefaultProfile()

              when {
                isSelectCommand(commandApdu) -> handleSelectCommand(commandApdu, profile)
                isGetProcessingOptionsCommand(commandApdu) ->
                        handleGetProcessingOptions(commandApdu, profile)
                isReadRecordCommand(commandApdu) -> handleReadRecord(commandApdu, profile)
                isGenerateAcCommand(commandApdu) -> handleGenerateAc(commandApdu, profile)
                isGetDataCommand(commandApdu) -> handleGetData(commandApdu, profile)
                isVerifyCommand(commandApdu) -> handleVerify(commandApdu, profile)
                else -> handleUnknownCommand(commandApdu, profile)
              }
            } catch (e: Exception) {
              Log.e(TAG, "Error processing APDU: ${e.message}", e)
              hexToBytes(SW_INSTRUCTION_NOT_SUPPORTED)
            }

    val responseHex = bytesToHex(response)
    val executionTime = System.currentTimeMillis() - startTime
    val statusWord = if (responseHex.length >= 4) responseHex.takeLast(4) else "0000"

    // Create log entry
    val logEntry =
            ApduLogEntry(
                    command = commandHex,
                    response = responseHex,
                    statusWord = statusWord,
                    description = getCommandDescription(commandApdu),
                    executionTimeMs = executionTime,
                    timestamp = System.currentTimeMillis()
            )

    apduLog.add(logEntry)
    callback?.onApduReceived(logEntry)

    Log.d(TAG, "Response APDU: $responseHex (${executionTime}ms)")

    return response
  }

  private fun isSelectCommand(apdu: ByteArray): Boolean {
    return apdu.size >= 2 && apdu[0] == CLA_ISO && apdu[1] == INS_SELECT
  }

  private fun isGetProcessingOptionsCommand(apdu: ByteArray): Boolean {
    return apdu.size >= 2 && apdu[0] == CLA_EMV && apdu[1] == INS_GET_PROCESSING_OPTIONS
  }

  private fun isReadRecordCommand(apdu: ByteArray): Boolean {
    return apdu.size >= 2 && apdu[0] == CLA_ISO && apdu[1] == INS_READ_RECORD
  }

  private fun isGenerateAcCommand(apdu: ByteArray): Boolean {
    return apdu.size >= 2 && apdu[0] == CLA_EMV && apdu[1] == INS_GENERATE_AC
  }

  private fun isGetDataCommand(apdu: ByteArray): Boolean {
    return apdu.size >= 2 && apdu[0] == CLA_EMV && apdu[1] == INS_GET_DATA
  }

  private fun isVerifyCommand(apdu: ByteArray): Boolean {
    return apdu.size >= 2 && apdu[0] == CLA_ISO && apdu[1] == INS_VERIFY
  }

  private fun handleSelectCommand(apdu: ByteArray, profile: EmulationProfile): ByteArray {
    if (apdu.size < 6) {
      return hexToBytes(SW_WRONG_LENGTH)
    }

    val p1 = apdu[2]
    val p2 = apdu[3]
    val lc = apdu[4].toInt() and 0xFF

    if (apdu.size < 5 + lc) {
      return hexToBytes(SW_WRONG_LENGTH)
    }

    val aidBytes = apdu.sliceArray(5 until 5 + lc)
    val aidHex = bytesToHex(aidBytes)

    Log.d(TAG, "SELECT command: P1=$p1, P2=$p2, AID=$aidHex")

    // Determine selection type
    return when {
      // SELECT PPSE
      aidHex.equals("325041592E5359532E4444463031", ignoreCase = true) -> {
        profile.handleSelectPpse(aidBytes)
      }
      // SELECT AID
      p1.toInt() == 0x04 && p2.toInt() == 0x00 -> {
        profile.handleSelectAid(aidBytes)
      }
      else -> {
        Log.w(TAG, "Unsupported SELECT parameters: P1=$p1, P2=$p2")
        hexToBytes(SW_WRONG_P1_P2)
      }
    }
  }

  private fun handleGetProcessingOptions(apdu: ByteArray, profile: EmulationProfile): ByteArray {
    if (apdu.size < 5) {
      return hexToBytes(SW_WRONG_LENGTH)
    }

    val lc = apdu[4].toInt() and 0xFF
    val pdolData =
            if (lc > 0 && apdu.size >= 5 + lc) {
              apdu.sliceArray(5 until 5 + lc)
            } else {
              byteArrayOf()
            }

    Log.d(TAG, "GET PROCESSING OPTIONS: PDOL data length=$lc")

    return profile.handleGetProcessingOptions(pdolData)
  }

  private fun handleReadRecord(apdu: ByteArray, profile: EmulationProfile): ByteArray {
    if (apdu.size < 5) {
      return hexToBytes(SW_WRONG_LENGTH)
    }

    val recordNumber = apdu[2].toInt() and 0xFF
    val sfi = (apdu[3].toInt() and 0xFF) shr 3

    Log.d(TAG, "READ RECORD: SFI=$sfi, Record=$recordNumber")

    return profile.handleReadRecord(sfi, recordNumber)
  }

  private fun handleGenerateAc(apdu: ByteArray, profile: EmulationProfile): ByteArray {
    if (apdu.size < 5) {
      return hexToBytes(SW_WRONG_LENGTH)
    }

    val p1 = apdu[2].toInt() and 0xFF
    val lc = apdu[4].toInt() and 0xFF
    val cdolData =
            if (lc > 0 && apdu.size >= 5 + lc) {
              apdu.sliceArray(5 until 5 + lc)
            } else {
              byteArrayOf()
            }

    Log.d(TAG, "GENERATE AC: P1=$p1 (${getAcTypeDescription(p1)}), CDOL length=$lc")

    return profile.handleGenerateAc(p1, cdolData)
  }

  private fun handleGetData(apdu: ByteArray, profile: EmulationProfile): ByteArray {
    if (apdu.size < 5) {
      return hexToBytes(SW_WRONG_LENGTH)
    }

    val p1 = apdu[2].toInt() and 0xFF
    val p2 = apdu[3].toInt() and 0xFF
    val tag = (p1 shl 8) or p2

    Log.d(TAG, "GET DATA: Tag=${String.format("%04X", tag)}")

    return profile.handleGetData(tag)
  }

  private fun handleVerify(apdu: ByteArray, profile: EmulationProfile): ByteArray {
    if (apdu.size < 5) {
      return hexToBytes(SW_WRONG_LENGTH)
    }

    val p1 = apdu[2].toInt() and 0xFF
    val p2 = apdu[3].toInt() and 0xFF
    val lc = apdu[4].toInt() and 0xFF
    val data =
            if (lc > 0 && apdu.size >= 5 + lc) {
              apdu.sliceArray(5 until 5 + lc)
            } else {
              byteArrayOf()
            }

    Log.d(TAG, "VERIFY: P1=$p1, P2=$p2, Data length=$lc")

    return profile.handleVerify(p1, p2, data)
  }

  private fun handleUnknownCommand(apdu: ByteArray, profile: EmulationProfile): ByteArray {
    val cla = apdu[0].toInt() and 0xFF
    val ins = apdu[1].toInt() and 0xFF

    Log.w(
            TAG,
            "Unknown command: CLA=${String.format("%02X", cla)}, INS=${String.format("%02X", ins)}"
    )

    return profile.handleUnknownCommand(apdu) ?: hexToBytes(SW_INSTRUCTION_NOT_SUPPORTED)
  }

  private fun getCommandDescription(apdu: ByteArray): String {
    if (apdu.size < 2) return "Invalid APDU"

    val cla = apdu[0].toInt() and 0xFF
    val ins = apdu[1].toInt() and 0xFF

    return when (ins) {
      0xA4 -> "SELECT"
      0xA8 -> "GET PROCESSING OPTIONS"
      0xB2 -> "READ RECORD"
      0xAE -> "GENERATE AC"
      0xCA -> "GET DATA"
      0x20 -> "VERIFY"
      else -> "Unknown (CLA=${String.format("%02X", cla)}, INS=${String.format("%02X", ins)})"
    }
  }

  private fun getAcTypeDescription(p1: Int): String {
    return when (p1 shr 6) {
      0x00 -> "AAC Request"
      0x01 -> "TC Request"
      0x02 -> "ARQC Request"
      0x03 -> "Reserved"
      else -> "Unknown"
    }
  }

  override fun onDeactivated(reason: Int) {
    val reasonStr =
            when (reason) {
              DEACTIVATION_LINK_LOSS -> "Link Loss"
              DEACTIVATION_DESELECTED -> "Deselected"
              else -> "Unknown ($reason)"
            }

    Log.d(TAG, "HCE Service deactivated: $reasonStr")

    val sessionDuration = System.currentTimeMillis() - sessionStartTime
    Log.i(TAG, "Session completed: ${apduLog.size} APDUs processed in ${sessionDuration}ms")

    callback?.onEmulationStopped()
  }

  // Utility functions
  private fun bytesToHex(bytes: ByteArray): String {
    return bytes.joinToString("") { "%02X".format(it) }
  }

  private fun hexToBytes(hex: String): ByteArray {
    val cleanHex = hex.replace(" ", "").uppercase()
    return cleanHex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
  }
=======

class EnhancedHceService : HostApduService() {
    
    override fun processCommandApdu(commandApdu: ByteArray?, extras: Bundle?): ByteArray {
        // EMV HCE processing will be implemented here
        // For now, return a basic response
        return byteArrayOf(0x90.toByte(), 0x00.toByte()) // SW_NO_ERROR
    }
    
    override fun onDeactivated(reason: Int) {
        // Handle HCE deactivation
    }
>>>>>>> 52c0655 (🎯 Complete Priority 1-3: Production-grade CardReadingScreen with EmvWorkflowProcessor)
}
