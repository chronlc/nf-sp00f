package com.example.nfsp00f.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nfsp00f.data.ApduLogEntry
import com.example.nfsp00f.emulation.EmulationProfile
import com.example.nfsp00f.emulation.EmvAttackEmulationManager
import com.example.nfsp00f.emulation.EnhancedHceService
import kotlinx.coroutines.launch

/**
 * Emulation Fragment - EMV Attack Emulation Control Interface Professional HCE management with
 * real-time attack monitoring
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmulationFragment() {

  var isEmulating by remember { mutableStateOf(false) }
  var activeProfile by remember { mutableStateOf<EmulationProfile.ProfileType?>(null) }
  var selectedProfile by remember { mutableStateOf<EmulationProfile.ProfileType?>(null) }
  var emulationLog by remember { mutableStateOf<List<ApduLogEntry>>(emptyList()) }
  var showAdvancedSettings by remember { mutableStateOf(false) }

  val emulationManager = EmvAttackEmulationManager.getInstance()
  val scope = rememberCoroutineScope()

  // Setup HCE callback
  LaunchedEffect(Unit) {
    EnhancedHceService.setCallback(
            object : EnhancedHceService.Companion.HceServiceCallback {
              override fun onApduReceived(logEntry: ApduLogEntry) {
                emulationLog = emulationLog + logEntry
              }

              override fun onEmulationStarted(profileName: String) {
                isEmulating = true
              }

              override fun onEmulationStopped() {
                isEmulating = false
                activeProfile = null
              }

              override fun onError(error: String) {
                // Handle error
              }
            }
    )
  }

  Column(
          modifier = Modifier.fillMaxSize().padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {

    // Header
    Text(
            text = "⚔️ EMV Attack Emulation",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
    )

    // Current Status Card
    EmulationStatusCard(
            isEmulating = isEmulating,
            activeProfile = activeProfile,
            onStopEmulation = {
              scope.launch {
                emulationManager.stopEmulation()
                isEmulating = false
                activeProfile = null
              }
            }
    )

    // Attack Profile Selection
    if (!isEmulating) {
      AttackProfileSelectionCard(
              selectedProfile = selectedProfile,
              onProfileSelected = { selectedProfile = it },
              onStartEmulation = { profile ->
                scope.launch {
                  if (emulationManager.startEmulation(profile)) {
                    isEmulating = true
                    activeProfile = profile
                    emulationLog = emptyList() // Clear previous log
                  }
                }
              }
      )
    }

    // Advanced Settings Toggle
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
                text = "🔧 Advanced Settings",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
        )
        Switch(checked = showAdvancedSettings, onCheckedChange = { showAdvancedSettings = it })
      }
    }

    // Advanced Settings Panel
    if (showAdvancedSettings) {
      AdvancedSettingsCard()
    }

    // Real-time Emulation Log
    if (isEmulating || emulationLog.isNotEmpty()) {
      EmulationLogCard(
              emulationLog = emulationLog,
              isActive = isEmulating,
              onClearLog = { emulationLog = emptyList() }
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmulationStatusCard(
        isEmulating: Boolean,
        activeProfile: EmulationProfile.ProfileType?,
        onStopEmulation: () -> Unit
) {
  Card(
          modifier = Modifier.fillMaxWidth(),
          colors =
                  CardDefaults.cardColors(
                          containerColor =
                                  if (isEmulating) Color(0xFF1B1B1B)
                                  else MaterialTheme.colorScheme.surface
                  )
  ) {
    Column(modifier = Modifier.padding(20.dp)) {
      if (isEmulating && activeProfile != null) {
        // Active emulation header
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
                    text = "🚨 ATTACK IN PROGRESS",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
            )
            Text(
                    text = getProfileDisplayName(activeProfile),
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
            )
            Text(
                    text = getProfileDescription(activeProfile),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
            )
          }

          Button(
                  onClick = onStopEmulation,
                  colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
          ) {
            Icon(Icons.Default.Stop, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("STOP", fontWeight = FontWeight.Bold)
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Active indicators
        var blink by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
          while (isEmulating) {
            blink = !blink
            kotlinx.coroutines.delay(500)
          }
        }

        Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          Icon(
                  imageVector = Icons.Default.RadioButtonChecked,
                  contentDescription = null,
                  tint = if (blink) Color.Red else Color.Transparent,
                  modifier = Modifier.size(16.dp)
          )
          Text(
                  text = "HCE service active - intercepting EMV transactions",
                  style = MaterialTheme.typography.bodyMedium,
                  color = Color.Gray
          )
        }

        Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          Icon(
                  imageVector = Icons.Default.Security,
                  contentDescription = null,
                  tint = Color.Orange,
                  modifier = Modifier.size(16.dp)
          )
          Text(
                  text = "Attack modules loaded and ready",
                  style = MaterialTheme.typography.bodyMedium,
                  color = Color.Gray
          )
        }
      } else {
        // Idle status
        Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Icon(
                  imageVector = Icons.Default.Shield,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(32.dp)
          )
          Column {
            Text(
                    text = "Emulation System Ready",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                    text = "Select attack profile to begin emulation",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AttackProfileSelectionCard(
        selectedProfile: EmulationProfile.ProfileType?,
        onProfileSelected: (EmulationProfile.ProfileType) -> Unit,
        onStartEmulation: (EmulationProfile.ProfileType) -> Unit
) {
  Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(modifier = Modifier.padding(20.dp)) {
      Text(
              text = "🎯 Attack Profile Selection",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Available profiles
      EmulationProfile.ProfileType.values().forEach { profile ->
        ProfileSelectionItem(
                profileType = profile,
                isSelected = selectedProfile == profile,
                onSelected = { onProfileSelected(profile) }
        )
        if (profile != EmulationProfile.ProfileType.values().last()) {
          Spacer(modifier = Modifier.height(8.dp))
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Start button
      Button(
              onClick = { selectedProfile?.let { onStartEmulation(it) } },
              enabled = selectedProfile != null,
              modifier = Modifier.fillMaxWidth(),
              colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
      ) {
        Icon(Icons.Default.PlayArrow, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "START ATTACK EMULATION", fontWeight = FontWeight.Bold)
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProfileSelectionItem(
        profileType: EmulationProfile.ProfileType,
        isSelected: Boolean,
        onSelected: () -> Unit
) {
  Card(
          onClick = onSelected,
          modifier = Modifier.fillMaxWidth(),
          colors =
                  CardDefaults.cardColors(
                          containerColor =
                                  if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                  else MaterialTheme.colorScheme.surfaceVariant
                  )
  ) {
    Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
      RadioButton(selected = isSelected, onClick = onSelected)

      Column(modifier = Modifier.weight(1f)) {
        Text(
                text = getProfileDisplayName(profileType),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color =
                        if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSurface
        )
        Text(
                text = getProfileDescription(profileType),
                style = MaterialTheme.typography.bodyMedium,
                color =
                        if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
                text = getProfileTechnicalDetails(profileType),
                style = MaterialTheme.typography.bodySmall,
                color =
                        if (isSelected)
                                MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
        )
      }

      Icon(
              imageVector = getProfileIcon(profileType),
              contentDescription = null,
              tint = Color.Red,
              modifier = Modifier.size(24.dp)
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdvancedSettingsCard() {
  Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(modifier = Modifier.padding(20.dp)) {
      Text(
              text = "⚙️ Advanced Configuration",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Settings options
      val settings =
              listOf(
                      "Response Delay" to "50ms",
                      "Logging Level" to "Verbose",
                      "Auto-Restart" to "Enabled",
                      "Terminal Detection" to "Enhanced"
              )

      settings.forEach { (setting, value) ->
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
                  text = setting,
                  style = MaterialTheme.typography.bodyLarge,
                  color = MaterialTheme.colorScheme.onSurface
          )
          Text(
                  text = value,
                  style = MaterialTheme.typography.bodyMedium,
                  fontWeight = FontWeight.Medium,
                  color = MaterialTheme.colorScheme.primary
          )
        }
        if (setting != settings.last().first) {
          Spacer(modifier = Modifier.height(12.dp))
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmulationLogCard(
        emulationLog: List<ApduLogEntry>,
        isActive: Boolean,
        onClearLog: () -> Unit
) {
  Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
                text = "📊 Emulation Log (${emulationLog.size} entries)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          if (isActive) {
            var blink by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
              while (isActive) {
                blink = !blink
                kotlinx.coroutines.delay(500)
              }
            }
            Icon(
                    imageVector = Icons.Default.Circle,
                    contentDescription = null,
                    tint = if (blink) Color.Red else Color.Transparent,
                    modifier = Modifier.size(12.dp)
            )
          }

          TextButton(
                  onClick = onClearLog,
                  colors = ButtonDefaults.textButtonColors(contentColor = Color.Gray)
          ) { Text("Clear", fontSize = MaterialTheme.typography.bodySmall.fontSize) }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      LazyColumn(
              modifier = Modifier.height(300.dp),
              verticalArrangement = Arrangement.spacedBy(4.dp)
      ) {
        items(emulationLog.takeLast(50)) { logEntry -> EmulationLogItem(logEntry = logEntry) }

        if (emulationLog.isEmpty()) {
          item {
            Text(
                    text =
                            if (isActive) "Waiting for terminal interactions..."
                            else "No emulation activity recorded",
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
private fun EmulationLogItem(logEntry: ApduLogEntry) {
  Column(
          modifier =
                  Modifier.fillMaxWidth()
                          .background(
                                  color = Color(0xFF161B22),
                                  shape = MaterialTheme.shapes.extraSmall
                          )
                          .padding(8.dp)
  ) {
    // Timestamp and description
    Text(
            text = "${logEntry.description} (${logEntry.executionTimeMs}ms)",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = Color.White
    )

    // Status indicator
    val statusColor =
            when (logEntry.statusWord) {
              "9000" -> Color.Green
              "6A82" -> Color.Orange
              else -> Color.Red
            }

    Text(
            text = "Status: ${logEntry.statusWord}",
            style = MaterialTheme.typography.bodySmall,
            color = statusColor
    )
  }
}

// Helper functions
private fun getProfileDisplayName(profileType: EmulationProfile.ProfileType): String {
  return when (profileType) {
    EmulationProfile.ProfileType.PPSE_POISONING -> "PPSE Poisoning Attack"
    EmulationProfile.ProfileType.AIP_BYPASS -> "AIP Bypass Attack"
    EmulationProfile.ProfileType.TRACK2_SPOOFING -> "Track2 Spoofing Attack"
    EmulationProfile.ProfileType.CRYPTOGRAM_ATTACK -> "Cryptogram Attack"
    EmulationProfile.ProfileType.CVM_BYPASS -> "CVM Bypass Attack"
  }
}

private fun getProfileDescription(profileType: EmulationProfile.ProfileType): String {
  return when (profileType) {
    EmulationProfile.ProfileType.PPSE_POISONING ->
            "Redirects payment system selection to malicious AID"
    EmulationProfile.ProfileType.AIP_BYPASS ->
            "Disables EMV security features through AIP manipulation"
    EmulationProfile.ProfileType.TRACK2_SPOOFING ->
            "Injects malicious Track2 data for magnetic stripe fallback"
    EmulationProfile.ProfileType.CRYPTOGRAM_ATTACK ->
            "Forces AAC→TC conversion, bypasses online authorization"
    EmulationProfile.ProfileType.CVM_BYPASS -> "Eliminates PIN/signature requirements completely"
  }
}

private fun getProfileTechnicalDetails(profileType: EmulationProfile.ProfileType): String {
  return when (profileType) {
    EmulationProfile.ProfileType.PPSE_POISONING ->
            "Modifies PPSE response, injects attacker AID, high priority routing"
    EmulationProfile.ProfileType.AIP_BYPASS ->
            "Sets AIP=0000, disables SDA/DDA/CVM/Terminal Risk/Issuer Auth/CDA"
    EmulationProfile.ProfileType.TRACK2_SPOOFING ->
            "Replaces Track2 equivalent data, enables magnetic stripe mode"
    EmulationProfile.ProfileType.CRYPTOGRAM_ATTACK ->
            "AAC→TC, ARQC→TC, predictable cryptograms, empty IAD"
    EmulationProfile.ProfileType.CVM_BYPASS ->
            "CVM List=No CVM Required, CVM Results=bypassed, AIP CVM disabled"
  }
}

private fun getProfileIcon(
        profileType: EmulationProfile.ProfileType
): androidx.compose.ui.graphics.vector.ImageVector {
  return when (profileType) {
    EmulationProfile.ProfileType.PPSE_POISONING -> Icons.Default.Poisoning
    EmulationProfile.ProfileType.AIP_BYPASS -> Icons.Default.Security
    EmulationProfile.ProfileType.TRACK2_SPOOFING -> Icons.Default.CreditCard
    EmulationProfile.ProfileType.CRYPTOGRAM_ATTACK -> Icons.Default.Lock
    EmulationProfile.ProfileType.CVM_BYPASS -> Icons.Default.NoAccounts
  }
}

// Extension for missing icons - use available alternatives
private val Icons.Default.Poisoning
  get() = Icons.Default.Dangerous
private val Icons.Default.NoAccounts
  get() = Icons.Default.Block
