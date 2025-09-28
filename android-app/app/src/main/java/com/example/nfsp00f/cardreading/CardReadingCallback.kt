package com.example.nfsp00f.cardreading

/** EMV card reading callback interface */
interface CardReadingCallback {
  fun onProgress(message: String)
  fun onCardRead(cardData: com.example.nfsp00f.data.EmvCardData)
  fun onApduExchanged(logEntry: com.example.nfsp00f.data.ApduLogEntry)
  fun onError(error: String)
}

/** EMV workflow enumeration for different card reading strategies */
enum class EmvWorkflow {
  STANDARD_CONTACTLESS, // Standard contactless workflow
  VISA_PREFERRED, // Prioritize VISA applications
  MASTERCARD_PREFERRED, // Prioritize Mastercard applications
  OFFLINE_FORCED, // Force offline processing
  CVM_REQUIRED, // Require cardholder verification
  ISSUER_AUTH_PATH, // Use issuer authentication path
  ENHANCED_DISCOVERY // Enhanced AID discovery mode
}
