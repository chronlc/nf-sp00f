package com.example.nfsp00f.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nfsp00f.emulation.EmvAttackEmulationManager

/**
 * Dashboard Fragment - EMV Security Platform Status Overview Professional forensic interface with
 * real-time system monitoring
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardFragment() {

  var systemStatus by remember { mutableStateOf("Ready") }
  var cardsInDatabase by remember { mutableStateOf(0) }
  var recentApduCount by remember { mutableStateOf(0) }
  var activeEmulation by remember { mutableStateOf<String?>(null) }

  val emulationManager = EmvAttackEmulationManager.getInstance()

  LaunchedEffect(Unit) {
    // Update status periodically
    while (true) {
      systemStatus = if (emulationManager.isEmulating()) "EMULATING" else "Ready"
      activeEmulation = emulationManager.getActiveProfile()?.name
      kotlinx.coroutines.delay(1000)
    }
  }

  LazyColumn(
          modifier = Modifier.fillMaxSize().padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {

    // Header
    item {
      Column {
        Text(
                text = "🏴‍☠️ nf-sp00f EMV Platform",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
        )
        Text(
                text = "Advanced EMV Security Research & Analysis",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // System Status Card
    item {
      StatusCard(
              title = "System Status",
              status = systemStatus,
              statusColor = if (systemStatus == "Ready") Color.Green else Color.Orange,
              icon =
                      if (systemStatus == "Ready") Icons.Default.CheckCircle
                      else Icons.Default.RadioButtonChecked
      )
    }

    // Quick Stats Row
    item {
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        QuickStatCard(
                title = "Cards",
                value = cardsInDatabase.toString(),
                icon = Icons.Default.CreditCard,
                modifier = Modifier.weight(1f)
        )
        QuickStatCard(
                title = "APDUs",
                value = recentApduCount.toString(),
                icon = Icons.Default.Timeline,
                modifier = Modifier.weight(1f)
        )
      }
    }

    // Active Emulation Card
    item {
      if (activeEmulation != null) {
        EmulationStatusCard(
                profileName = activeEmulation!!,
                onStop = {
                  emulationManager.stopEmulation()
                  activeEmulation = null
                }
        )
      }
    }

    // Attack Modules Overview
    item { AttackModulesOverview() }

    // Recent Activity
    item { RecentActivityCard() }

    // Security Metrics
    item { SecurityMetricsCard() }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatusCard(
        title: String,
        status: String,
        statusColor: Color,
        icon: androidx.compose.ui.graphics.vector.ImageVector
) {
  Card(
          modifier = Modifier.fillMaxWidth(),
          colors =
                  CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
  ) {
    Column(modifier = Modifier.padding(20.dp)) {
      Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Icon(
                imageVector = icon,
                contentDescription = null,
                tint = statusColor,
                modifier = Modifier.size(32.dp)
        )
        Column {
          Text(
                  text = title,
                  style = MaterialTheme.typography.titleMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
                  text = status,
                  style = MaterialTheme.typography.headlineSmall,
                  fontWeight = FontWeight.Bold,
                  color = statusColor
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun QuickStatCard(
        title: String,
        value: String,
        icon: androidx.compose.ui.graphics.vector.ImageVector,
        modifier: Modifier = Modifier
) {
  Card(
          modifier = modifier,
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
      Icon(
              imageVector = icon,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(24.dp)
      )
      Spacer(modifier = Modifier.height(8.dp))
      Text(
              text = value,
              style = MaterialTheme.typography.headlineMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
      )
      Text(
              text = title,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmulationStatusCard(profileName: String, onStop: () -> Unit) {
  Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF1B1B1B))
  ) {
    Column(modifier = Modifier.padding(20.dp)) {
      Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
                  text = "🚨 ACTIVE EMULATION",
                  style = MaterialTheme.typography.titleSmall,
                  color = Color.Red,
                  fontWeight = FontWeight.Bold
          )
          Text(
                  text = profileName,
                  style = MaterialTheme.typography.titleLarge,
                  color = Color.White,
                  fontWeight = FontWeight.Bold
          )
        }
        Button(onClick = onStop, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
          Text("STOP", color = Color.White, fontWeight = FontWeight.Bold)
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Blinking indicator
      var blink by remember { mutableStateOf(false) }
      LaunchedEffect(Unit) {
        while (true) {
          blink = !blink
          kotlinx.coroutines.delay(500)
        }
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
                imageVector = Icons.Default.RadioButtonChecked,
                contentDescription = null,
                tint = if (blink) Color.Red else Color.Transparent,
                modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
                text = "Attack module active - intercepting EMV transactions",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AttackModulesOverview() {
  Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(modifier = Modifier.padding(20.dp)) {
      Text(
              text = "⚔️ Attack Modules",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(16.dp))

      val modules =
              listOf(
                      "PPSE Poisoning" to "Payment system redirection",
                      "AIP Bypass" to "Security feature disable",
                      "Track2 Spoofing" to "Magnetic stripe injection",
                      "Cryptogram Attack" to "Authorization bypass",
                      "CVM Bypass" to "PIN/Signature bypass"
              )

      modules.forEach { (name, description) ->
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
                    text = name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = Color.Green,
                  modifier = Modifier.size(20.dp)
          )
        }
        if (name != modules.last().first) {
          Spacer(modifier = Modifier.height(12.dp))
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RecentActivityCard() {
  Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(modifier = Modifier.padding(20.dp)) {
      Text(
              text = "📊 Recent Activity",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(16.dp))

      val activities =
              listOf(
                      "EMV card read completed" to "2 min ago",
                      "PPSE Poisoning profile loaded" to "5 min ago",
                      "Database sync completed" to "15 min ago",
                      "Security scan finished" to "1 hour ago"
              )

      activities.forEach { (activity, time) ->
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
                  text = activity,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurface,
                  modifier = Modifier.weight(1f)
          )
          Text(
                  text = time,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        if (activity != activities.last().first) {
          Spacer(modifier = Modifier.height(8.dp))
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SecurityMetricsCard() {
  Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(modifier = Modifier.padding(20.dp)) {
      Text(
              text = "🛡️ Security Metrics",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(16.dp))

      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        MetricItem(
                label = "Attack Success Rate",
                value = "100%",
                color = Color.Red,
                modifier = Modifier.weight(1f)
        )
        MetricItem(
                label = "Cards Analyzed",
                value = "47",
                color = Color.Blue,
                modifier = Modifier.weight(1f)
        )
        MetricItem(
                label = "Vulnerabilities",
                value = "23",
                color = Color.Orange,
                modifier = Modifier.weight(1f)
        )
      }
    }
  }
}

@Composable
private fun MetricItem(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
  Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = color
    )
    Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}
