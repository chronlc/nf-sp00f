package com.example.nfsp00f.data

data class VirtualCard(
        val cardholderName: String,
        val pan: String,
        val expiry: String,
        val apduCount: Int,
        val cardType: String
)

// Device connection states
enum class DeviceState {
  NOT_SELECTED,
  CONNECTING,
  CONNECTED,
  ERROR
}

// NFC Device types
enum class NfcDevice(val displayName: String) {
  INTERNAL("Internal NFC"),
  PN532_BLUETOOTH("PN532 BLUETOOTH"),
  PN532_USB("PN532 USB")
}

// Note: ApduLogEntry is defined in ApduLogEntry.kt

data class DatabaseCard(
        val cardholderName: String,
        val pan: String,
        val expiry: String,
        val apduCount: Int,
        val cardType: String,
        val category: String,
        val isEncrypted: Boolean,
        val lastUsed: String
)

data class AnalysisResult(
        val title: String,
        val cardNumber: String,
        val status: String,
        val score: Int,
        val timestamp: String
)

data class AnalysisTool(
        val title: String,
        val description: String,
        val icon: androidx.compose.ui.graphics.vector.ImageVector,
        val enabled: Boolean = true
)
