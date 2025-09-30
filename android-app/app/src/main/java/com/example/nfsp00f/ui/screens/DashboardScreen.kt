package com.example.nfsp00f.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.nfsp00f.R
import com.example.nfsp00f.data.VirtualCard

@Composable
fun dashboardScreen() {
  val sampleCards =
          listOf(
                  VirtualCard("JOHN DOE", "4154 **** **** 3556", "02/29", 47, "VISA"),
                  VirtualCard("JANE SMITH", "5555 **** **** 4444", "12/28", 23, "MC"),
                  VirtualCard("ALICE WILSON", "3782 **** **** 1007", "05/27", 89, "AMEX")
          )

  Column(
          modifier = Modifier.fillMaxSize().padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // System Status Card with proper design specs
    Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF121717)),
            shape = RoundedCornerShape(8.dp), // 8dp corner radius
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // 4dp elevation
    ) {
      Box(modifier = Modifier.fillMaxWidth()) {
        // Background image
        Image(
                painter = painterResource(id = R.drawable.nfspoof_logo),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(200.dp),
                alpha = 0.2f
        )

        Column(
                modifier = Modifier.padding(16.dp), // 16dp padding per design spec
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Text(
                  "NFC PhreaK BoX",
                  style =
                          MaterialTheme.typography.headlineLarge.copy(
                                  fontWeight = FontWeight.Bold
                          ), // Larger and bold
                  color = Color(0xFF4CAF50),
                  textAlign = TextAlign.Center
          )
          Text(
                  "RFiD TooLKiT",
                  style =
                          MaterialTheme.typography.titleMedium.copy(
                                  textDecoration =
                                          androidx.compose.ui.text.style.TextDecoration.Underline
                          ), // 16sp Regular with underline
                  color = Color(0xFFFFFFFF), // White color
                  textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(16.dp))

          Text(
                  "System Status",
                  style =
                          MaterialTheme.typography.titleLarge.copy(
                                  fontWeight = FontWeight.Bold,
                                  textDecoration =
                                          androidx.compose.ui.text.style.TextDecoration.Underline
                          ), // 18sp Bold with underline
                  color = Color(0xFF4CAF50),
                  textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Status indicators
          Column(
                  modifier = Modifier.fillMaxWidth(),
                  verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            StatusRow("NFC Hardware", "Available", true)
            StatusRow("HCE Service", "Ready", true)
            StatusRow("Bluetooth", "Not Connected", false)
            StatusRow("PN532", "Not Ready", false)
          }
        }
      }
    }

    // Stats Cards Row
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      StatsCard(
              modifier = Modifier.weight(1f),
              title = "Total Cards",
              value = "3",
              icon = Icons.Default.CreditCard
      )
      StatsCard(
              modifier = Modifier.weight(1f),
              title = "Active Sessions",
              value = "1",
              icon = Icons.Default.Wifi
      )
      StatsCard(
              modifier = Modifier.weight(1f),
              title = "Success Rate",
              value = "95%",
              icon = Icons.Default.CheckCircle
      )
    }

    // Recent Cards Section
    Text(
            "Recent Cards",
            style = MaterialTheme.typography.titleLarge, // 18sp Title
            color = Color(0xFF4CAF50)
    )

    LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
    ) { items(sampleCards) { card -> VirtualCardView(card) } }
  }
}

@Composable
fun StatusRow(label: String, status: String, isGood: Boolean) {
  Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
            label,
            color = Color(0xFF4CAF50),
            style =
                    MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                    ) // Bold labels
    )
    Text(
            status,
            color = if (isGood) Color(0xFF4CAF50) else Color(0xFFcf1b33),
            style =
                    MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold
                    ) // Bold status
    )
  }
}

@Composable
fun StatsCard(
        modifier: Modifier = Modifier,
        title: String,
        value: String,
        icon: androidx.compose.ui.graphics.vector.ImageVector
) {
  Card(
          modifier = modifier.height(100.dp), // Fixed height for stats cards
          colors = CardDefaults.cardColors(containerColor = Color(0xFF121717)),
          shape = RoundedCornerShape(8.dp), // 8dp corner radius
          elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // 4dp elevation
  ) {
    Column(
            modifier = Modifier.padding(8.dp).fillMaxSize(), // Less padding, allow full height
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
      Icon(
              icon,
              contentDescription = null,
              tint = Color(0xFF4CAF50),
              modifier = Modifier.size(20.dp) // Slightly smaller icon
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
              value,
              style = MaterialTheme.typography.headlineSmall, // Smaller number text
              color = Color(0xFFFFFFFF) // White for numbers
      )
      Text(
              title,
              style =
                      MaterialTheme.typography.bodySmall.copy(
                              fontWeight = FontWeight.Bold
                      ), // Smaller text that fits better
              color = Color(0xFF4a4f54), // Gray for titles
              textAlign = TextAlign.Center,
              maxLines = 2 // Allow 2 lines
      )
    }
  }
}

@Composable
fun VirtualCardView(card: VirtualCard) {
  Card(
          modifier = Modifier.width(200.dp).height(120.dp),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF121717)),
          shape = RoundedCornerShape(8.dp), // 8dp corner radius
          elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // 4dp elevation
  ) {
    Box(modifier = Modifier.fillMaxSize()) {
      // Subtle nfspoof.png background
      Image(
              painter = painterResource(id = R.drawable.nfspoof_logo),
              contentDescription = null,
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize(),
              alpha = 0.1f // Subtle shadow with alpha 0.1
      )

      Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Card brand in upper right corner (swapped)
        Text(
                card.cardType,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFFFFF), // White for card brand
                modifier = Modifier.align(Alignment.TopEnd)
        )

        // APDU count in upper left (swapped)
        Text(
                "${card.apduCount} APDUs",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF4a4f54), // Gray for APDU count
                modifier = Modifier.align(Alignment.TopStart)
        )

        // Cardholder info in bottom left corner
        Column(modifier = Modifier.align(Alignment.BottomStart)) {
          Text(
                  card.cardholderName,
                  style = MaterialTheme.typography.labelSmall, // 12sp Captions
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFFFFFFFF) // White for cardholder name
          )
          Text(
                  card.pan,
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                  color = Color(0xFF4CAF50) // Bold PAN
          )
          Text(
                  card.expiry,
                  style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                  color = Color(0xFF4a4f54) // Bold expiry in gray
          )
        }
      }
    }
  }
}
