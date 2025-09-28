package com.example.nfsp00f.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nfsp00f.cardreading.*
import com.example.nfsp00f.data.ApduLogEntry
import com.example.nfsp00f.data.EmvCardData
import kotlinx.coroutines.launch

/**
 * Card Reading Fragment - Real-time NFC EMV Card Reading Interface Professional forensic interface
 * with live APDU monitoring
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardReadingFragment() {

  var isReading by remember { mutableStateOf(false) }
  var currentStatus by remember { mutableStateOf("Ready to read") }
  var selectedWorkflow by remember { mutableStateOf(EmvWorkflow.STANDARD_CONTACTLESS) }
  var readCardData by remember { mutableStateOf<EmvCardData?>(null) }
  var apduLog by remember { mutableStateOf<List<ApduLogEntry>>(emptyList()) }
  var showApduLog by remember { mutableStateOf(false) }

  val scope = rememberCoroutineScope()

  Column(
          modifier = Modifier.fillMaxSize().padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {

    // Header
    Text(
            text = "📱 EMV Card Reader",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
    )

    // Workflow Selection
    WorkflowSelectionCard(
            selectedWorkflow = selectedWorkflow,
            onWorkflowSelected = { selectedWorkflow = it },
            enabled = !isReading
    )

    // Reading Controls
    ReadingControlsCard(
            isReading = isReading,
            currentStatus = currentStatus,
            onStartReading = {
              isReading = true
              currentStatus = "Initializing NFC reader..."

              scope.launch {
                try {
                  // Simulate card reading process
                  simulateCardReading(
                          workflow = selectedWorkflow,
                          onProgress = { status -> currentStatus = status },
                          onApduExchanged = { logEntry -> apduLog = apduLog + logEntry },
                          onCardRead = { cardData ->
                            readCardData = cardData
                            currentStatus = "Card read complete"
                            isReading = false
                          },
                          onError = { error ->
                            currentStatus = "Error: $error"
                            isReading = false
                          }
                  )
                } catch (e: Exception) {
                  currentStatus = "Failed: ${e.message}"
                  isReading = false
                }
              }
            },
            onStopReading = {
              isReading = false
              currentStatus = "Stopped by user"
            }
    )

    // APDU Log Toggle
    Card(
            modifier = Modifier.fillMaxWidth(),
            colors =
                    CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
    ) {
      Row(
              modifier = Modifier.fillMaxWidth().padding(16.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
                text = "📊 APDU Log (${apduLog.size} entries)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
        )
        Switch(checked = showApduLog, onCheckedChange = { showApduLog = it })
      }
    }

    // Content Area
    if (showApduLog) {
      ApduLogCard(apduLog = apduLog)
    } else if (readCardData != null) {
      CardDataCard(cardData = readCardData!!)
    } else {
      PlaceholderCard()
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WorkflowSelectionCard(
        selectedWorkflow: EmvWorkflow,
        onWorkflowSelected: (EmvWorkflow) -> Unit,
        enabled: Boolean
) {
  var expanded by remember { mutableStateOf(false) }

  Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(modifier = Modifier.padding(20.dp)) {
      Text(
              text = "🔧 EMV Workflow Configuration",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(12.dp))

      ExposedDropdownMenuBox(
              expanded = expanded,
              onExpandedChange = { expanded = !expanded && enabled }
      ) {
        OutlinedTextField(
                value = getWorkflowDisplayName(selectedWorkflow),
                onValueChange = {},
                readOnly = true,
                label = { Text("Workflow") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                enabled = enabled,
                colors =
                        OutlinedTextFieldDefaults.colors(
                                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                disabledBorderColor =
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                        )
        )

        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
          EmvWorkflow.values().forEach { workflow ->
            DropdownMenuItem(
                    text = {
                      Column {
                        Text(
                                text = getWorkflowDisplayName(workflow),
                                fontWeight = FontWeight.Medium
                        )
                        Text(
                                text = getWorkflowDescription(workflow),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                      }
                    },
                    onClick = {
                      onWorkflowSelected(workflow)
                      expanded = false
                    }
            )
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReadingControlsCard(
        isReading: Boolean,
        currentStatus: String,
        onStartReading: () -> Unit,
        onStopReading: () -> Unit
) {
  Card(
          modifier = Modifier.fillMaxWidth(),
          colors =
                  CardDefaults.cardColors(
                          containerColor =
                                  if (isReading) Color(0xFF1B1B1B)
                                  else MaterialTheme.colorScheme.surface
                  )
  ) {
    Column(modifier = Modifier.padding(20.dp)) {
      Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
                text = if (isReading) "🔄 READING IN PROGRESS" else "⚡ Ready to Read",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = if (isReading) Color.Yellow else MaterialTheme.colorScheme.primary
        )

        if (isReading) {
          Button(
                  onClick = onStopReading,
                  colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
          ) {
            Icon(Icons.Default.Stop, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("STOP", fontWeight = FontWeight.Bold)
          }
        } else {
          Button(
                  onClick = onStartReading,
                  colors =
                          ButtonDefaults.buttonColors(
                                  containerColor = MaterialTheme.colorScheme.primary
                          )
          ) {
            Icon(Icons.Default.Nfc, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("START READING", fontWeight = FontWeight.Bold)
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Status Display
      Row(verticalAlignment = Alignment.CenterVertically) {
        if (isReading) {
          CircularProgressIndicator(
                  modifier = Modifier.size(20.dp),
                  strokeWidth = 2.dp,
                  color = Color.Yellow
          )
          Spacer(modifier = Modifier.width(12.dp))
        }
        Text(
                text = currentStatus,
                style = MaterialTheme.typography.bodyLarge,
                color = if (isReading) Color.White else MaterialTheme.colorScheme.onSurface
        )
      }

      if (!isReading) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
                text = "💡 Place EMV card near device NFC antenna to begin reading",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardDataCard(cardData: EmvCardData) {
  Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(modifier = Modifier.padding(20.dp).verticalScroll(rememberScrollState())) {
      Text(
              text = "💳 EMV Card Data",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.primary
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Core Card Info
      CardInfoSection(
              title = "Card Information",
              fields =
                      listOf(
                              "PAN" to (cardData.pan.ifEmpty { "Not available" }),
                              "Cardholder" to (cardData.cardholderName.ifEmpty { "Not available" }),
                              "Expiry Date" to (cardData.expiryDate.ifEmpty { "Not available" }),
                              "Application Label" to
                                      (cardData.applicationLabel.ifEmpty { "Not available" }),
                              "Preferred Name" to
                                      (cardData.applicationPreferredName.ifEmpty {
                                        "Not available"
                                      })
                      )
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Technical Data
      CardInfoSection(
              title = "EMV Technical Data",
              fields =
                      listOf(
                              "AIP" to
                                      (cardData.applicationInterchangeProfile.ifEmpty {
                                        "Not available"
                                      }),
                              "AFL" to
                                      (cardData.applicationFileLocator.ifEmpty { "Not available" }),
                              "ATC" to
                                      (cardData.applicationTransactionCounter.ifEmpty {
                                        "Not available"
                                      }),
                              "Track2 Data" to
                                      if (cardData.track2Data.isNotEmpty())
                                              "Present (${cardData.track2Data.length} chars)"
                                      else "Not available"
                      )
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Available AIDs
      if (cardData.availableAids.isNotEmpty()) {
        Text(
                text = "Available AIDs",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))

        cardData.availableAids.forEach { aid ->
          Text(
                  text = "• $aid",
                  style = MaterialTheme.typography.bodyMedium,
                  fontFamily = FontFamily.Monospace,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3API::class)
@Composable
private fun ApduLogCard(apduLog: List<ApduLogEntry>) {
  Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Text(
              text = "📊 APDU Communication Log",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = Color.White
      )

      Spacer(modifier = Modifier.height(16.dp))

      LazyColumn(
              modifier = Modifier.height(400.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        items(apduLog) { logEntry -> ApduLogItem(logEntry = logEntry) }

        if (apduLog.isEmpty()) {
          item {
            Text(
                    text = "No APDU exchanges recorded yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 20.dp)
            )
          }
        }
      }
    }
  }
}

@Composable
private fun ApduLogItem(logEntry: ApduLogEntry) {
  Column(
          modifier =
                  Modifier.fillMaxWidth()
                          .background(color = Color(0xFF161B22), shape = MaterialTheme.shapes.small)
                          .padding(12.dp)
  ) {
    // Header
    Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
              text = logEntry.description,
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = Color.White
      )
      Text(
              text = "${logEntry.executionTimeMs}ms",
              style = MaterialTheme.typography.bodySmall,
              color = Color.Gray
      )
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Command
    Text(
            text = "→ ${logEntry.command}",
            style = MaterialTheme.typography.bodySmall,
            fontFamily = FontFamily.Monospace,
            color = Color(0xFF7CB342)
    )

    // Response
    Text(
            text = "← ${logEntry.response}",
            style = MaterialTheme.typography.bodySmall,
            fontFamily = FontFamily.Monospace,
            color = Color(0xFF42A5F5)
    )

    // Status
    val statusColor = if (logEntry.statusWord == "9000") Color.Green else Color.Red
    Text(
            text = "Status: ${logEntry.statusWord}",
            style = MaterialTheme.typography.bodySmall,
            color = statusColor
    )
  }
}

@Composable
private fun CardInfoSection(title: String, fields: List<Pair<String, String>>) {
  Text(
          text = title,
          style = MaterialTheme.typography.titleMedium,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
  )

  Spacer(modifier = Modifier.height(8.dp))

  fields.forEach { (label, value) ->
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
      Text(
              text = label,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              modifier = Modifier.weight(1f)
      )
      Text(
              text = value,
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.Medium,
              color = MaterialTheme.colorScheme.onSurface,
              modifier = Modifier.weight(2f)
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaceholderCard() {
  Card(
          modifier = Modifier.fillMaxWidth(),
          colors =
                  CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
  ) {
    Column(
            modifier = Modifier.fillMaxWidth().padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
      Icon(
              imageVector = Icons.Default.Nfc,
              contentDescription = null,
              modifier = Modifier.size(64.dp),
              tint = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(16.dp))

      Text(
              text = "Ready to Read EMV Cards",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
              text = "Select workflow and tap START READING to begin",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

// Helper functions
private fun getWorkflowDisplayName(workflow: EmvWorkflow): String {
  return when (workflow) {
    EmvWorkflow.STANDARD_CONTACTLESS -> "Standard Contactless"
    EmvWorkflow.VISA_PREFERRED -> "VISA Preferred"
    EmvWorkflow.MASTERCARD_PREFERRED -> "Mastercard Preferred"
    EmvWorkflow.OFFLINE_FORCED -> "Offline Forced"
    EmvWorkflow.CVM_REQUIRED -> "CVM Required"
    EmvWorkflow.ISSUER_AUTH_PATH -> "Issuer Auth Path"
    EmvWorkflow.ENHANCED_DISCOVERY -> "Enhanced Discovery"
  }
}

private fun getWorkflowDescription(workflow: EmvWorkflow): String {
  return when (workflow) {
    EmvWorkflow.STANDARD_CONTACTLESS -> "Standard contactless EMV flow"
    EmvWorkflow.VISA_PREFERRED -> "Prioritize VISA applications"
    EmvWorkflow.MASTERCARD_PREFERRED -> "Prioritize Mastercard applications"
    EmvWorkflow.OFFLINE_FORCED -> "Force offline processing"
    EmvWorkflow.CVM_REQUIRED -> "Require cardholder verification"
    EmvWorkflow.ISSUER_AUTH_PATH -> "Use issuer authentication"
    EmvWorkflow.ENHANCED_DISCOVERY -> "Enhanced AID discovery"
  }
}

// Simulate card reading process for demo
private suspend fun simulateCardReading(
        workflow: EmvWorkflow,
        onProgress: (String) -> Unit,
        onApduExchanged: (ApduLogEntry) -> Unit,
        onCardRead: (EmvCardData) -> Unit,
        onError: (String) -> Unit
) {
  try {
    onProgress("Detecting card...")
    kotlinx.coroutines.delay(1000)

    onProgress("Selecting PPSE...")
    onApduExchanged(
            createSampleApduLog(
                    "SELECT PPSE",
                    "00A404000E325041592E5359532E4444463031",
                    "6F2B840E325041592E5359532E4444463031A5194F07A00000000310109000"
            )
    )
    kotlinx.coroutines.delay(800)

    onProgress("Selecting AID...")
    onApduExchanged(
            createSampleApduLog("SELECT AID", "00A40400A0000000031010", "6F35840A0000000310109000")
    )
    kotlinx.coroutines.delay(800)

    onProgress("Getting processing options...")
    onApduExchanged(
            createSampleApduLog(
                    "GPO",
                    "80A80000238300000000001000000000000000008400000000000009A032509269F3704000000010",
                    "80108000580008010103100201059000"
            )
    )
    kotlinx.coroutines.delay(1000)

    onProgress("Reading records...")
    onApduExchanged(
            createSampleApduLog(
                    "READ RECORD",
                    "00B2011400",
                    "70819F5A084154904674973556575013415490467497355657134154904674973556D29029000"
            )
    )
    kotlinx.coroutines.delay(800)

    onProgress("Processing EMV data...")
    kotlinx.coroutines.delay(500)

    // Create sample card data
    val cardData =
            EmvCardData(
                    cardUid = "EMV_4154904674973556",
                    pan = "4154904674973556",
                    track2Data = "4154904674973556D2902",
                    cardholderName = "CARDHOLDER/TEST",
                    expiryDate = "2902",
                    applicationLabel = "VISA DEBIT",
                    applicationPreferredName = "VISA",
                    applicationInterchangeProfile = "5800",
                    applicationFileLocator = "08010103100201059000",
                    applicationTransactionCounter = "0001",
                    unpredictableNumber = "12345678",
                    terminalVerificationResults = "0000000000",
                    transactionStatusInformation = "0000",
                    applicationCryptogram = "",
                    issuerApplicationData = "",
                    cryptogramInformationData = "",
                    cdol1 = "",
                    cdol2 = "",
                    pdolConstructed = "",
                    emvTags =
                            mapOf(
                                    "5A" to "4154904674973556",
                                    "57" to "4154904674973556D2902",
                                    "5F20" to "CARDHOLDER/TEST",
                                    "5F24" to "2902"
                            ),
                    availableAids = listOf("A0000000031010"),
                    apduLog = emptyList(),
                    timestampFirstSeen = System.currentTimeMillis(),
                    timestampUpdated = System.currentTimeMillis()
            )

    onCardRead(cardData)
  } catch (e: Exception) {
    onError(e.message ?: "Unknown error")
  }
}

private fun createSampleApduLog(
        description: String,
        command: String,
        response: String
): ApduLogEntry {
  return ApduLogEntry(
          command = command,
          response = response,
          statusWord = response.takeLast(4),
          description = description,
          executionTimeMs = (10..150).random().toLong(),
          timestamp = System.currentTimeMillis()
  )
}
