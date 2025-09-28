package com.example.nfsp00f.cardreading

/**
 * Production-grade EMV TLV Parser Implements RFIDIOt/Proxmark3-style BER-TLV parsing with
 * comprehensive EMV tag support
 */
object EmvTlvParser {

  // EMV tag definitions - comprehensive mapping from EMV 4.3 specification
  private val emvTagMap =
          mapOf(
                  "4F" to "Application Identifier (AID)",
                  "50" to "Application Label",
                  "57" to "Track 2 Equivalent Data",
                  "5A" to "Application Primary Account Number (PAN)",
                  "5F20" to "Cardholder Name",
                  "5F24" to "Application Expiration Date",
                  "5F25" to "Application Effective Date",
                  "5F28" to "Issuer Country Code",
                  "5F2A" to "Transaction Currency Code",
                  "5F2D" to "Language Preference",
                  "5F30" to "Service Code",
                  "5F34" to "Application Primary Account Number (PAN) Sequence Number",
                  "5F36" to "Transaction Currency Exponent",
                  "5F50" to "Issuer URL",
                  "5F53" to "International Bank Account Number (IBAN)",
                  "5F54" to "Bank Identifier Code (BIC)",
                  "5F55" to "Issuer Country Code (alpha2 format)",
                  "5F56" to "Issuer Country Code (alpha3 format)",
                  "61" to "Application Template",
                  "6F" to "File Control Information (FCI) Template",
                  "70" to "READ RECORD Response Message Template",
                  "71" to "Issuer Script Template 1",
                  "72" to "Issuer Script Template 2",
                  "73" to "Directory Discretionary Template",
                  "77" to "Response Message Template Format 2",
                  "80" to "Response Message Template Format 1",
                  "81" to "Amount, Authorised (Binary)",
                  "82" to "Application Interchange Profile",
                  "83" to "Command Template",
                  "84" to "Dedicated File (DF) Name",
                  "86" to "Issuer Script Command",
                  "87" to "Application Priority Indicator",
                  "88" to "Short File Identifier (SFI)",
                  "89" to "Authorisation Code",
                  "8A" to "Authorisation Response Code",
                  "8C" to "Card Risk Management Data Object List 1 (CDOL1)",
                  "8D" to "Card Risk Management Data Object List 2 (CDOL2)",
                  "8E" to "Cardholder Verification Method (CVM) List",
                  "8F" to "Certification Authority Public Key Index",
                  "90" to "Issuer Public Key Certificate",
                  "91" to "Issuer Authentication Data",
                  "92" to "Issuer Public Key Remainder",
                  "93" to "Signed Static Application Data",
                  "94" to "Application File Locator (AFL)",
                  "95" to "Terminal Verification Results",
                  "97" to "Transaction Certificate Data Object List (TDOL)",
                  "98" to "Transaction Certificate (TC) Hash Value",
                  "99" to "Transaction Personal Identification Number (PIN) Data",
                  "9A" to "Transaction Date",
                  "9B" to "Transaction Status Information",
                  "9C" to "Transaction Type",
                  "9D" to "Directory Definition File (DDF) Name",
                  "9F01" to "Acquirer Identifier",
                  "9F02" to "Amount, Authorised (Numeric)",
                  "9F03" to "Amount, Other (Numeric)",
                  "9F04" to "Amount, Other (Binary)",
                  "9F05" to "Application Discretionary Data",
                  "9F06" to "Application Identifier (AID) - terminal",
                  "9F07" to "Application Usage Control",
                  "9F08" to "Application Version Number",
                  "9F09" to "Application Version Number",
                  "9F0B" to "Cardholder Name Extended",
                  "9F0D" to "Issuer Action Code - Default",
                  "9F0E" to "Issuer Action Code - Denial",
                  "9F0F" to "Issuer Action Code - Online",
                  "9F10" to "Issuer Application Data",
                  "9F11" to "Issuer Code Table Index",
                  "9F12" to "Application Preferred Name",
                  "9F13" to "Last Online Application Transaction Counter (ATC) Register",
                  "9F14" to "Lower Consecutive Offline Limit",
                  "9F15" to "Merchant Category Code",
                  "9F16" to "Merchant Identifier",
                  "9F17" to "Personal Identification Number (PIN) Try Counter",
                  "9F18" to "Issuer Script Identifier",
                  "9F1A" to "Terminal Country Code",
                  "9F1B" to "Terminal Floor Limit",
                  "9F1C" to "Terminal Identification",
                  "9F1D" to "Terminal Risk Management Data",
                  "9F1E" to "Interface Device (IFD) Serial Number",
                  "9F1F" to "Track 1 Discretionary Data",
                  "9F20" to "Track 2 Discretionary Data",
                  "9F21" to "Transaction Time",
                  "9F22" to "Certification Authority Public Key Index",
                  "9F23" to "Upper Consecutive Offline Limit",
                  "9F26" to "Application Cryptogram",
                  "9F27" to "Cryptogram Information Data",
                  "9F2D" to "Integrated Circuit Card (ICC) PIN Encipherment Public Key Certificate",
                  "9F2E" to "Integrated Circuit Card (ICC) PIN Encipherment Public Key Exponent",
                  "9F2F" to "Integrated Circuit Card (ICC) PIN Encipherment Public Key Remainder",
                  "9F32" to "Issuer Public Key Exponent",
                  "9F33" to "Terminal Capabilities",
                  "9F34" to "Cardholder Verification Method (CVM) Results",
                  "9F35" to "Terminal Type",
                  "9F36" to "Application Transaction Counter (ATC)",
                  "9F37" to "Unpredictable Number",
                  "9F38" to "Processing Options Data Object List (PDOL)",
                  "9F39" to "Point-of-Service (POS) Entry Mode",
                  "9F3A" to "Amount, Reference Currency",
                  "9F3B" to "Currency Code, Application Reference",
                  "9F3C" to "Currency Code, Transaction Reference",
                  "9F3D" to "Currency Exponent, Transaction Reference",
                  "9F40" to "Additional Terminal Capabilities",
                  "9F41" to "Transaction Sequence Counter",
                  "9F42" to "Application Currency Code",
                  "9F43" to "Application Reference Currency Exponent",
                  "9F44" to "Application Currency Exponent",
                  "9F45" to "Data Authentication Code",
                  "9F46" to "Integrated Circuit Card (ICC) Public Key Certificate",
                  "9F47" to "Integrated Circuit Card (ICC) Public Key Exponent",
                  "9F48" to "Integrated Circuit Card (ICC) Public Key Remainder",
                  "9F49" to "Dynamic Data Authentication Data Object List (DDOL)",
                  "9F4A" to "Static Data Authentication Tag List",
                  "9F4B" to "Signed Dynamic Application Data",
                  "9F4C" to "ICC Dynamic Number",
                  "9F4D" to "Log Entry",
                  "9F4E" to "Merchant Name and Location",
                  "9F4F" to "Log Format",
                  "9F66" to "Terminal Transaction Qualifiers (TTQ)",
                  "9F6C" to "Card Transaction Qualifiers (CTQ)",
                  "BF0C" to "File Control Information (FCI) Issuer Discretionary Data"
          )

  /** Parse TLV data and extract EMV tags with descriptions */
  fun parseTlvData(tlvData: String): Map<String, TlvEntry> {
    val parsedTags = mutableMapOf<String, TlvEntry>()

    if (tlvData.isBlank()) return parsedTags

    var offset = 0
    val cleanTlv = tlvData.replace(" ", "").uppercase()

    while (offset < cleanTlv.length - 3) {
      try {
        // Parse tag
        val tag = parseTag(cleanTlv, offset)
        if (tag.isEmpty()) break

        offset += tag.length

        // Parse length
        val (length, lengthBytes) = parseLength(cleanTlv, offset)
        offset += lengthBytes

        // Extract value
        val valueLength = length * 2
        if (offset + valueLength > cleanTlv.length) break

        val value = cleanTlv.substring(offset, offset + valueLength)
        offset += valueLength

        val tlvEntry =
                TlvEntry(
                        tag = tag,
                        length = length,
                        value = value,
                        description = emvTagMap[tag] ?: "Unknown Tag",
                        parsedValue = parseTagValue(tag, value)
                )

        parsedTags[tag] = tlvEntry
      } catch (e: Exception) {
        // Skip malformed data
        offset += 2
      }
    }

    return parsedTags
  }

  /** Parse EMV tag (handles single and multi-byte tags) */
  private fun parseTag(data: String, offset: Int): String {
    if (offset + 1 >= data.length) return ""

    val firstByte = data.substring(offset, offset + 2).toInt(16)

    // Check if multi-byte tag
    if (firstByte and 0x1F == 0x1F) {
      // Multi-byte tag
      var tagOffset = offset + 2
      while (tagOffset + 1 < data.length) {
        val nextByte = data.substring(tagOffset, tagOffset + 2).toInt(16)
        tagOffset += 2
        if (nextByte and 0x80 == 0) break
      }
      return data.substring(offset, tagOffset)
    } else {
      // Single byte tag
      return data.substring(offset, offset + 2)
    }
  }

  /** Parse BER-TLV length field */
  private fun parseLength(data: String, offset: Int): Pair<Int, Int> {
    if (offset + 1 >= data.length) return Pair(0, 0)

    val lengthByte = data.substring(offset, offset + 2).toInt(16)

    if (lengthByte and 0x80 == 0) {
      // Short form - length in single byte
      return Pair(lengthByte, 2)
    } else {
      // Long form - multiple bytes
      val lengthBytes = lengthByte and 0x7F
      if (lengthBytes == 0 || offset + 2 + lengthBytes * 2 > data.length) {
        return Pair(0, 2)
      }

      val lengthHex = data.substring(offset + 2, offset + 2 + lengthBytes * 2)
      val length = lengthHex.toInt(16)

      return Pair(length, 2 + lengthBytes * 2)
    }
  }

  /** Parse tag value based on EMV specification */
  private fun parseTagValue(tag: String, value: String): String {
    return when (tag) {
      "4F" -> "AID: ${formatAid(value)}"
      "50", "9F12" -> "\"${hexToAscii(value)}\""
      "57" -> parseTrack2Data(value)
      "5A" -> formatPan(value)
      "5F20" -> "\"${hexToAscii(value)}\""
      "5F24" -> formatDate(value, "YYMMDD")
      "5F25" -> formatDate(value, "YYMMDD")
      "5F2A" -> formatCurrencyCode(value)
      "82" -> parseAip(value)
      "84" -> "DF Name: ${formatAid(value)}"
      "87" -> "Priority: ${value.toIntOrNull(16) ?: 0}"
      "8C", "8D" -> parseDol(value)
      "8E" -> parseCvmList(value)
      "94" -> parseAfl(value)
      "95" -> parseTvr(value)
      "9A" -> formatDate(value, "YYMMDD")
      "9C" -> parseTransactionType(value)
      "9F02", "9F03" -> formatAmount(value)
      "9F07" -> parseAuc(value)
      "9F1A" -> formatCountryCode(value)
      "9F21" -> formatTime(value)
      "9F26" -> "AC: $value"
      "9F27" -> parseCid(value)
      "9F33" -> parseTerminalCapabilities(value)
      "9F36" -> "ATC: ${value.toIntOrNull(16) ?: 0}"
      "9F37" -> "UN: $value"
      "9F38" -> parseDol(value)
      "9F40" -> parseAdditionalTerminalCapabilities(value)
      "9F66" -> parseTtq(value)
      else -> value
    }
  }

  // Parsing helper functions
  private fun hexToAscii(hex: String): String {
    return try {
      hex.chunked(2).map { it.toInt(16).toChar() }.joinToString("").trim()
    } catch (e: Exception) {
      hex
    }
  }

  private fun formatAid(aid: String): String {
    return aid.chunked(2).joinToString(" ")
  }

  private fun formatPan(pan: String): String {
    val cleanPan = pan.replace("F", "")
    return if (cleanPan.length >= 13) {
      "${cleanPan.substring(0, 4)} **** **** ${cleanPan.takeLast(4)}"
    } else {
      cleanPan
    }
  }

  private fun formatDate(dateHex: String, format: String): String {
    return when (format) {
      "YYMMDD" -> {
        if (dateHex.length >= 6) {
          val year = "20${dateHex.substring(0, 2)}"
          val month = dateHex.substring(2, 4)
          val day = dateHex.substring(4, 6)
          "$month/$day/$year"
        } else dateHex
      }
      else -> dateHex
    }
  }

  private fun formatTime(timeHex: String): String {
    return if (timeHex.length >= 6) {
      val hour = timeHex.substring(0, 2)
      val minute = timeHex.substring(2, 4)
      val second = timeHex.substring(4, 6)
      "$hour:$minute:$second"
    } else timeHex
  }

  private fun formatAmount(amountHex: String): String {
    val amount = amountHex.toLongOrNull(16) ?: 0
    return "$${amount / 100}.${(amount % 100).toString().padStart(2, '0')}"
  }

  private fun formatCurrencyCode(code: String): String {
    val currencies =
            mapOf(
                    "0840" to "USD",
                    "0978" to "EUR",
                    "0826" to "GBP",
                    "0392" to "JPY",
                    "0124" to "CAD",
                    "0036" to "AUD"
            )
    return currencies[code] ?: code
  }

  private fun formatCountryCode(code: String): String {
    val countries =
            mapOf(
                    "0840" to "United States",
                    "0276" to "Germany",
                    "0826" to "United Kingdom",
                    "0392" to "Japan",
                    "0124" to "Canada",
                    "0036" to "Australia"
            )
    return countries[code] ?: code
  }

  private fun parseTrack2Data(track2: String): String {
    val separator = track2.indexOf('D')
    return if (separator > 0) {
      val pan = track2.substring(0, separator)
      val expiry = track2.substring(separator + 1, separator + 5)
      "PAN: ${formatPan(pan)}, Exp: ${expiry.substring(2, 4)}/${expiry.substring(0, 2)}"
    } else "Track2: $track2"
  }

  private fun parseAip(aip: String): String {
    val byte1 = aip.take(2).toIntOrNull(16) ?: 0
    val byte2 = if (aip.length > 2) aip.drop(2).take(2).toIntOrNull(16) ?: 0 else 0

    val features = mutableListOf<String>()
    if (byte1 and 0x40 != 0) features.add("SDA")
    if (byte1 and 0x20 != 0) features.add("DDA")
    if (byte1 and 0x10 != 0) features.add("CHV")
    if (byte1 and 0x08 != 0) features.add("Terminal Risk")
    if (byte1 and 0x04 != 0) features.add("Issuer Auth")
    if (byte1 and 0x02 != 0) features.add("CDA")

    return "AIP: [${features.joinToString(", ")}]"
  }

  private fun parseTvr(tvr: String): String {
    if (tvr.length < 10) return "TVR: $tvr"

    val byte1 = tvr.take(2).toIntOrNull(16) ?: 0
    val violations = mutableListOf<String>()

    if (byte1 and 0x80 != 0) violations.add("Offline Data Authentication failed")
    if (byte1 and 0x40 != 0) violations.add("SDA failed")
    if (byte1 and 0x20 != 0) violations.add("ICC data missing")
    if (byte1 and 0x10 != 0) violations.add("Card on exception file")
    if (byte1 and 0x08 != 0) violations.add("DDA failed")
    if (byte1 and 0x04 != 0) violations.add("CDA failed")

    return if (violations.isEmpty()) "TVR: OK" else "TVR: [${violations.joinToString(", ")}]"
  }

  private fun parseAfl(afl: String): String {
    val records = mutableListOf<String>()
    var offset = 0

    while (offset + 7 < afl.length) {
      val sfi = afl.substring(offset, offset + 2).toIntOrNull(16)?.shr(3) ?: 0
      val start = afl.substring(offset + 2, offset + 4).toIntOrNull(16) ?: 0
      val end = afl.substring(offset + 4, offset + 6).toIntOrNull(16) ?: 0

      records.add("SFI$sfi:$start-$end")
      offset += 8
    }

    return "AFL: [${records.joinToString(", ")}]"
  }

  private fun parseDol(dol: String): String {
    val tags = mutableListOf<String>()
    var offset = 0

    while (offset + 3 < dol.length) {
      val tag = dol.substring(offset, offset + 2)
      val length = dol.substring(offset + 2, offset + 4).toIntOrNull(16) ?: 0

      val tagName = emvTagMap[tag]?.take(10) ?: tag
      tags.add("$tagName($length)")

      offset += 4
    }

    return "DOL: [${tags.joinToString(", ")}]"
  }

  private fun parseCvmList(cvmList: String): String {
    if (cvmList.length < 16) return "CVM: $cvmList"

    val methods = mutableListOf<String>()
    var offset = 16 // Skip first 8 bytes (X and Y values)

    while (offset + 3 < cvmList.length) {
      val cvmByte = cvmList.substring(offset, offset + 2).toIntOrNull(16) ?: 0
      val conditionByte = cvmList.substring(offset + 2, offset + 4).toIntOrNull(16) ?: 0

      val method =
              when (cvmByte and 0x3F) {
                0x01 -> "PIN"
                0x02 -> "Signature"
                0x03 -> "PIN+Signature"
                0x1E -> "No CVM"
                0x1F -> "No CVM"
                else -> "Unknown"
              }

      methods.add(method)
      offset += 4
    }

    return "CVM: [${methods.take(3).joinToString(", ")}]"
  }

  private fun parseTransactionType(type: String): String {
    val types =
            mapOf(
                    "00" to "Purchase",
                    "01" to "Cash Advance",
                    "09" to "Purchase with Cashback",
                    "20" to "Refund"
            )
    return types[type] ?: "Type $type"
  }

  private fun parseAuc(auc: String): String {
    val byte1 = auc.take(2).toIntOrNull(16) ?: 0
    val usages = mutableListOf<String>()

    if (byte1 and 0x80 != 0) usages.add("Valid domestic")
    if (byte1 and 0x40 != 0) usages.add("Valid international")
    if (byte1 and 0x20 != 0) usages.add("Domestic services")
    if (byte1 and 0x10 != 0) usages.add("International services")

    return "AUC: [${usages.joinToString(", ")}]"
  }

  private fun parseTerminalCapabilities(caps: String): String {
    if (caps.length < 6) return "TC: $caps"

    val byte1 = caps.take(2).toIntOrNull(16) ?: 0
    val features = mutableListOf<String>()

    if (byte1 and 0x80 != 0) features.add("Manual key entry")
    if (byte1 and 0x40 != 0) features.add("Magnetic stripe")
    if (byte1 and 0x20 != 0) features.add("IC with contacts")

    return "TC: [${features.joinToString(", ")}]"
  }

  private fun parseAdditionalTerminalCapabilities(caps: String): String {
    if (caps.length < 10) return "ATC: $caps"

    val byte1 = caps.take(2).toIntOrNull(16) ?: 0
    val features = mutableListOf<String>()

    if (byte1 and 0x80 != 0) features.add("Cash")
    if (byte1 and 0x40 != 0) features.add("Goods")
    if (byte1 and 0x20 != 0) features.add("Services")
    if (byte1 and 0x10 != 0) features.add("Cashback")

    return "ATC: [${features.joinToString(", ")}]"
  }

  private fun parseTtq(ttq: String): String {
    if (ttq.length < 8) return "TTQ: $ttq"

    val byte1 = ttq.take(2).toIntOrNull(16) ?: 0
    val features = mutableListOf<String>()

    if (byte1 and 0x80 != 0) features.add("MSD")
    if (byte1 and 0x40 != 0) features.add("qVSDC")
    if (byte1 and 0x20 != 0) features.add("EMV Contact")
    if (byte1 and 0x10 != 0) features.add("Offline-only")
    if (byte1 and 0x08 != 0) features.add("Online PIN")
    if (byte1 and 0x04 != 0) features.add("Signature")

    return "TTQ: [${features.joinToString(", ")}]"
  }

  private fun parseCid(cid: String): String {
    val cidByte = cid.take(2).toIntOrNull(16) ?: 0

    val cryptogramType =
            when (cidByte and 0xC0) {
              0x00 -> "AAC (Transaction Declined)"
              0x40 -> "TC (Transaction Approved)"
              0x80 -> "ARQC (Online Authorization Required)"
              0xC0 -> "RFU"
              else -> "Unknown"
            }

    return "CID: $cryptogramType"
  }

  /** TLV Entry data class */
  data class TlvEntry(
          val tag: String,
          val length: Int,
          val value: String,
          val description: String,
          val parsedValue: String
  )
}
