package com.example.nfsp00f.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nfsp00f.R
import com.example.nfsp00f.data.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun cardReadingScreen() {
  var selectedDevice by remember { mutableStateOf<NfcDevice?>(null) }
  var deviceState by remember { mutableStateOf(DeviceState.NOT_SELECTED) }
  var deviceStatusText by remember { mutableStateOf("No Device Selected") }

  // Checkboxes state
  var singleCard by remember { mutableStateOf(true) }
  var multiCard by remember { mutableStateOf(false) }
  var stealth by remember { mutableStateOf(false) }
  var emvDump by remember { mutableStateOf(false) }

  // Reading state
  var isReading by remember { mutableStateOf(false) }

  // Card data
  var readCard by remember { mutableStateOf<VirtualCard?>(null) }

  // APDU traffic
  var apduLog by remember { mutableStateOf(listOf<ApduLogEntry>()) }

  Column(
          modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header - RoGuE TeRMiNAL
    Text(
            "RoGuE TeRMiNAL",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF4CAF50),
            textAlign = TextAlign.Center,
            letterSpacing = 2.sp
    )

    // Device Selection Row
    Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      Text(
              "NFC Device:",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = Color(0xFF4CAF50)
      )

      // Dropdown Menu
      var expanded by remember { mutableStateOf(false) }

      Box {
        OutlinedButton(
                onClick = { expanded = true },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF4CAF50)),
                border = BorderStroke(1.dp, Color(0xFF4CAF50)),
                modifier = Modifier.width(200.dp)
        ) {
          Text(
                  selectedDevice?.displayName ?: "No Device Selected",
                  color = if (selectedDevice == null) Color(0xFFFFFFFF) else Color(0xFF4CAF50)
          )
          Icon(
                  Icons.Default.ArrowDropDown,
                  contentDescription = "Dropdown",
                  tint = Color(0xFF4CAF50)
          )
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
          NfcDevice.values().forEach { device ->
            DropdownMenuItem(
                    text = { Text(device.displayName, color = Color(0xFF4CAF50)) },
                    onClick = {
                      selectedDevice = device
                      expanded = false
                      // Simulate connection attempt
                      deviceState = DeviceState.CONNECTING
                      deviceStatusText = "Connecting to ${device.displayName}..."

                      // Simulate connection success after delay
                      GlobalScope.launch {
                        delay(2000)
                        deviceState = DeviceState.CONNECTED
                        deviceStatusText = "${device.displayName} Connected"
                      }
                    }
            )
          }
        }
      }
    }

    // Device Status
    Text(
            deviceStatusText,
            style = MaterialTheme.typography.bodyLarge,
            color =
                    when (deviceState) {
                      DeviceState.CONNECTED -> Color(0xFF4CAF50)
                      DeviceState.CONNECTING -> Color(0xFFFF9800)
                      DeviceState.ERROR -> Color(0xFFCF1B33)
                      DeviceState.NOT_SELECTED -> Color(0xFF4CAF50).copy(alpha = 0.7f)
                    },
            fontWeight = FontWeight.Medium
    )

    // Reading Options Checkboxes - Dashboard Style
    Card(
            modifier = Modifier.fillMaxWidth(),
            colors =
                    CardDefaults.cardColors(
                            containerColor = Color(0xFF121717)
                    ), // Same as dashboard
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
      Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
                "Reading Options",
                style =
                        MaterialTheme.typography.titleMedium.copy(
                                textDecoration =
                                        androidx.compose.ui.text.style.TextDecoration.Underline
                        ),
                color = Color(0xFF4CAF50),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
        )

        // First row: Single Card and Multi Card
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          CheckboxRow("Single Card", singleCard, Modifier.weight(1f)) {
            singleCard = it
            if (it) multiCard = false
          }
          CheckboxRow("Multi Card", multiCard, Modifier.weight(1f)) {
            multiCard = it
            if (it) singleCard = false
          }
        }

        // Second row: Stealth and EMV Dump
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          CheckboxRow("Stealth", stealth, Modifier.weight(1f)) { stealth = it }
          CheckboxRow("EMV Dump", emvDump, Modifier.weight(1f)) { emvDump = it }
        }
      }
    }

    // Control Buttons
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
      Button(
              onClick = {
                if (deviceState == DeviceState.CONNECTED) {
                  isReading = true
                  // Simulate card reading
                  simulateCardRead { card ->
                    readCard = card
                    isReading = false
                  }
                }
              },
              enabled = deviceState == DeviceState.CONNECTED && !isReading,
              colors =
                      ButtonDefaults.buttonColors(
                              containerColor = Color(0xFF4CAF50),
                              contentColor = Color.Black
                      ),
              modifier = Modifier.weight(1f)
      ) {
        if (isReading) {
          CircularProgressIndicator(
                  modifier = Modifier.size(16.dp),
                  color = Color.Black,
                  strokeWidth = 2.dp
          )
          Spacer(modifier = Modifier.width(8.dp))
        }
        Text("Read Card(s)")
      }

      Button(
              onClick = {
                isReading = false
                readCard = null
                apduLog = emptyList()
              },
              enabled = isReading,
              colors =
                      ButtonDefaults.buttonColors(
                              containerColor = Color(0xFFCF1B33),
                              contentColor = Color.White
                      ),
              modifier = Modifier.weight(1f)
      ) { Text("Stop") }
    }

    // Virtual Card Display - Dashboard Style
    if (readCard != null) {
      Card(
              modifier = Modifier.fillMaxWidth(0.75f).height(120.dp), // Smaller width
              colors =
                      CardDefaults.cardColors(
                              containerColor = Color(0xFF121717)
                      ), // Same dark background
              shape = RoundedCornerShape(8.dp),
              elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
      ) {
        Box(modifier = Modifier.fillMaxSize()) {
          // Subtle background - same as dashboard
          Image(
                  painter = painterResource(id = R.drawable.nfspoof_logo),
                  contentDescription = null,
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.fillMaxSize(),
                  alpha = 0.1f
          )

          Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // APDU count in upper left (same as dashboard)
            Text(
                    "${readCard!!.apduCount} APDUs",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF4a4f54), // Gray like dashboard
                    modifier = Modifier.align(Alignment.TopStart)
            )

            // Card brand in upper right (same as dashboard)
            Text(
                    readCard!!.cardType,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFFFFF), // White like dashboard
                    modifier = Modifier.align(Alignment.TopEnd)
            )

            // Cardholder info in bottom left corner (same as dashboard)
            Column(modifier = Modifier.align(Alignment.BottomStart)) {
              Text(
                      readCard!!.cardholderName,
                      style = MaterialTheme.typography.labelSmall,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFFFFFFFF) // White like dashboard
              )
              Text(
                      readCard!!.pan,
                      style =
                              MaterialTheme.typography.labelSmall.copy(
                                      fontWeight = FontWeight.Bold
                              ),
                      color = Color(0xFF4CAF50) // Green like dashboard
              )
              Text(
                      readCard!!.expiry,
                      style =
                              MaterialTheme.typography.labelSmall.copy(
                                      fontWeight = FontWeight.Bold
                              ),
                      color = Color(0xFF4a4f54) // Gray like dashboard
              )
            }
          }
        }
      }
    } else {
      // Blank card placeholder - Dashboard Style
      Card(
              modifier = Modifier.fillMaxWidth(0.75f).height(120.dp), // Smaller width
              colors =
                      CardDefaults.cardColors(
                              containerColor = Color(0xFF121717)
                      ), // Same dark background
              shape = RoundedCornerShape(8.dp),
              elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
      ) {
        Box(modifier = Modifier.fillMaxSize()) {
          // Add background image like dashboard cards
          Image(
                  painter = painterResource(id = R.drawable.nfspoof_logo),
                  contentDescription = null,
                  contentScale = ContentScale.Crop,
                  modifier = Modifier.fillMaxSize(),
                  alpha = 0.1f
          )

          Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                    "No Card Read",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF4CAF50).copy(alpha = 0.6f)
            )
          }
        }
      }
    }

    // Live APDU Traffic
    Card(
            modifier = Modifier.fillMaxWidth().height(300.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Black),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
                "Live APDU Traffic",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF4CAF50),
                fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          items(apduLog) { entry -> ApduLogItem(entry) }

          if (apduLog.isEmpty()) {
            item {
              Text(
                      "Waiting for APDU traffic...",
                      style = MaterialTheme.typography.bodyLarge,
                      color = Color(0xFF4CAF50).copy(alpha = 0.6f),
                      fontFamily = FontFamily.Monospace
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun CheckboxRow(
        label: String,
        checked: Boolean,
        modifier: Modifier = Modifier,
        onCheckedChange: (Boolean) -> Unit
) {
  Row(
          modifier = modifier,
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(6.dp)
  ) {
    Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors =
                    CheckboxDefaults.colors(
                            checkedColor = Color(0xFF4CAF50),
                            uncheckedColor = Color(0xFF4CAF50),
                            checkmarkColor = Color.Black
                    ),
            modifier = Modifier.size(18.dp)
    )
    Text(
            label,
            style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp),
            color = Color(0xFF4CAF50)
    )
  }
}

@Composable
fun ApduLogItem(entry: ApduLogEntry) {
  Column(modifier = Modifier.fillMaxWidth()) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      Text(
              entry.getFormattedTimestamp(),
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFF4CAF50).copy(alpha = 0.7f),
              fontFamily = FontFamily.Monospace
      )
      Text(
              "→",
              style = MaterialTheme.typography.bodyLarge,
              color = Color(0xFF4CAF50),
              fontFamily = FontFamily.Monospace
      )
      Text(
              entry.getCommandName(),
              style = MaterialTheme.typography.bodyLarge,
              color = Color(0xFF4CAF50),
              fontFamily = FontFamily.Monospace
      )
    }

    if (entry.command.isNotEmpty()) {
      Text(
              "CMD: ${entry.command}",
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFF4CAF50).copy(alpha = 0.8f),
              fontFamily = FontFamily.Monospace,
              modifier = Modifier.padding(start = 16.dp)
      )
    }

    if (entry.response.isNotEmpty()) {
      Text(
              "RSP: ${entry.response}",
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFFFF9800).copy(alpha = 0.8f),
              fontFamily = FontFamily.Monospace,
              modifier = Modifier.padding(start = 16.dp)
      )
    }

    if (entry.statusWord.isNotEmpty()) {
      Text(
              "SW: ${entry.statusWord} (${entry.getStatusMeaning()})",
              style = MaterialTheme.typography.labelSmall,
              color = if (entry.isSuccess()) Color(0xFF4CAF50) else Color(0xFFFF5722),
              fontFamily = FontFamily.Monospace,
              modifier = Modifier.padding(start = 16.dp)
      )
    }

    if (entry.description.isNotEmpty()) {
      Text(
              "→ ${entry.description}",
              style = MaterialTheme.typography.labelSmall,
              color = Color(0xFFFF9800),
              fontFamily = FontFamily.Monospace,
              modifier = Modifier.padding(start = 16.dp)
      )
    }
  }
}

// Simulate card reading function
fun simulateCardRead(onCardRead: (VirtualCard) -> Unit) {
  GlobalScope.launch {
    delay(3000) // Simulate reading time
    val simulatedCard =
            VirtualCard(
                    cardholderName = "JOHN DOE",
                    pan = "4154 **** **** 3556",
                    expiry = "02/29",
                    apduCount = 47,
                    cardType = "VISA"
            )
    onCardRead(simulatedCard)
  }
}
