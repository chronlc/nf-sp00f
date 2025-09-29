package com.nf_sp00f.app.data

/**
 * APDU Log Entry for real-time EMV transaction logging
 * Captures command/response pairs with timing and analysis
 */
data class ApduLogEntry(
    val command: String,              // TX APDU hex string
    val response: String,             // RX APDU hex string
    val statusWord: String,           // SW1SW2 (last 4 hex chars of response)
    val description: String,          // Human-readable parsed summary
    val executionTimeMs: Long,        // Execution time in milliseconds
    val timestamp: Long = System.currentTimeMillis()  // Unix timestamp
) {
    
    /**
     * Check if APDU execution was successful (9000)
     */
    fun isSuccess(): Boolean {
        return statusWord == "9000"
    }
    
    /**
     * Get formatted timestamp for UI display
     */
    fun getFormattedTimestamp(): String {
        val date = java.util.Date(timestamp)
        val format = java.text.SimpleDateFormat("HH:mm:ss.SSS", java.util.Locale.getDefault())
        return format.format(date)
    }
    
    /**
     * Get APDU command name for display
     */
    fun getCommandName(): String {
        return when {
            command.startsWith("00A404000E325041592E5359532E4444463031") -> "SELECT PPSE"
            command.startsWith("00A4040007A0000000031010") -> "SELECT VISA"
            command.startsWith("00A4040007A0000000041010") -> "SELECT MASTERCARD"
            command.startsWith("00A404") -> "SELECT AID"
            command.startsWith("80A8") -> "GET PROCESSING OPTIONS"
            command.startsWith("00B2") -> "READ RECORD"
            command.startsWith("80AE") -> "GENERATE AC"
            command.startsWith("84") -> "GET CHALLENGE"
            command.startsWith("00CA") -> "GET DATA"
            else -> "UNKNOWN (${command.take(8)})"
        }
    }
    
    /**
     * Parse status word meaning
     */
    fun getStatusMeaning(): String {
        return when (statusWord.uppercase()) {
            "9000" -> "Success"
            "6282" -> "End of file reached"
            "6283" -> "Selected file invalidated"
            "6300" -> "Authentication failed"
            "6700" -> "Wrong length"
            "6881" -> "Logical channel not supported"
            "6982" -> "Security status not satisfied"
            "6985" -> "Conditions not satisfied"
            "6986" -> "Command not allowed"
            "6A80" -> "Incorrect data field"
            "6A81" -> "Function not supported"
            "6A82" -> "File not found"
            "6A83" -> "Record not found"
            "6A84" -> "Not enough space"
            "6A86" -> "Incorrect parameters P1-P2"
            "6A88" -> "Referenced data not found"
            "6B00" -> "Wrong parameters P1-P2"
            "6C00" -> "Wrong Le field"
            "6D00" -> "Instruction not supported"
            "6E00" -> "Class not supported"
            "6F00" -> "No precise diagnosis"
            else -> "Unknown status: $statusWord"
        }
    }
    
    /**
     * Get response data (excluding status word)
     */
    fun getResponseData(): String {
        return if (response.length > 4) {
            response.substring(0, response.length - 4)
        } else ""
    }
    
    /**
     * Check if this is a critical EMV command
     */
    fun isCriticalCommand(): Boolean {
        return getCommandName().let { name ->
            name.contains("SELECT") || 
            name.contains("GET PROCESSING OPTIONS") || 
            name.contains("GENERATE AC")
        }
    }
    
    /**
     * Get execution time category for UI color coding
     */
    fun getExecutionTimeCategory(): String {
        return when {
            executionTimeMs < 50 -> "FAST"
            executionTimeMs < 200 -> "NORMAL"
            executionTimeMs < 500 -> "SLOW"
            else -> "TIMEOUT"
        }
    }
    
    /**
     * Extract EMV tags from response data
     */
    fun extractEmvTags(): Map<String, String> {
        val tags = mutableMapOf<String, String>()
        val responseData = getResponseData()
        
        if (responseData.isNotEmpty()) {
            // Simple TLV parsing for common single-byte tags
            var i = 0
            while (i < responseData.length - 3) {
                try {
                    val tag = responseData.substring(i, i + 2)
                    val lengthHex = responseData.substring(i + 2, i + 4)
                    val length = lengthHex.toInt(16) * 2  // Convert to hex char count
                    
                    if (i + 4 + length <= responseData.length) {
                        val value = responseData.substring(i + 4, i + 4 + length)
                        tags[tag] = value
                        i += 4 + length
                    } else {
                        break
                    }
                } catch (e: Exception) {
                    break
                }
            }
        }
        
        return tags
    }
    
    /**
     * Generate comprehensive log summary for debugging
     */
    fun generateLogSummary(): String {
        return buildString {
            appendLine("=== APDU Log Entry ===")
            appendLine("Timestamp: ${getFormattedTimestamp()}")
            appendLine("Command: ${getCommandName()}")
            appendLine("TX: $command")
            appendLine("RX: $response")
            appendLine("Status: $statusWord (${getStatusMeaning()})")
            appendLine("Time: ${executionTimeMs}ms (${getExecutionTimeCategory()})")
            appendLine("Success: ${isSuccess()}")
            
            val extractedTags = extractEmvTags()
            if (extractedTags.isNotEmpty()) {
                appendLine("Extracted Tags:")
                extractedTags.forEach { (tag, value) ->
                    appendLine("  $tag: $value")
                }
            }
        }
    }
}
