package com.nf_sp00f.app.emulation

/** EMV Attack Emulation Profile Interface Defines contract for EMV attack implementations */
abstract class EmulationProfile(val name: String, val description: String) {

  enum class ProfileType {
    PPSE_POISONING,
    AIP_BYPASS,
    TRACK2_SPOOFING,
    CRYPTOGRAM_ATTACK,
    CVM_BYPASS
  }

  // EMV Command Handlers - must be implemented by attack profiles
  abstract fun handleSelectPpse(aid: ByteArray): ByteArray
  abstract fun handleSelectAid(aid: ByteArray): ByteArray
  abstract fun handleGetProcessingOptions(pdolData: ByteArray): ByteArray
  abstract fun handleReadRecord(sfi: Int, recordNumber: Int): ByteArray
  abstract fun handleGenerateAc(acType: Int, cdolData: ByteArray): ByteArray
  abstract fun handleGetData(tag: Int): ByteArray
  abstract fun handleVerify(p1: Int, p2: Int, data: ByteArray): ByteArray
  abstract fun handleUnknownCommand(apdu: ByteArray): ByteArray?

  // Utility functions for all profiles
  protected fun bytesToHex(bytes: ByteArray): String {
    return bytes.joinToString("") { "%02X".format(it) }
  }

  protected fun hexToBytes(hex: String): ByteArray {
    val cleanHex = hex.replace(" ", "").uppercase()
    return cleanHex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
  }

  protected fun createTlv(tag: String, value: String): String {
    val valueLength = value.length / 2
    val lengthHex = String.format("%02X", valueLength)
    return tag + lengthHex + value
  }

  protected fun createSuccessResponse(data: String = ""): ByteArray {
    return hexToBytes(data + "9000")
  }

  protected fun createErrorResponse(statusWord: String): ByteArray {
    return hexToBytes(statusWord)
  }
}
