package com.example.nfsp00f.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.nfsp00f.R
import com.example.nfsp00f.data.*

@Composable
fun emulationScreen() {
        var selectedCard by remember { mutableStateOf<VirtualCard?>(null) }
        var selectedAttack by remember { mutableStateOf("Standard Emulation") }
        var isEmulating by remember { mutableStateOf(false) }
        var hceStatus by remember { mutableStateOf("HCE Service Ready") }

        val attackProfiles =
                listOf(
                        "Standard Emulation",
                        "PPSE Poisoning",
                        "AIP Force Offline",
                        "Track2 Spoofing",
                        "Cryptogram Downgrade",
                        "CVM Bypass"
                )

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
                // HCE Status Card
                Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121717)),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                                Image(
                                        painter = painterResource(id = R.drawable.nfspoof_logo),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxWidth().height(120.dp),
                                        alpha = 0.1f
                                )

                                Column(
                                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                        Text(
                                                "EMV HCE EmULaToR",
                                                style =
                                                        MaterialTheme.typography.headlineLarge.copy(
                                                                fontWeight = FontWeight.Bold
                                                        ),
                                                color = Color(0xFF4CAF50),
                                                textAlign = TextAlign.Center
                                        )
                                        Text(
                                                "Card Attack Profiles",
                                                style =
                                                        MaterialTheme.typography.titleMedium.copy(
                                                                textDecoration =
                                                                        androidx.compose.ui.text
                                                                                .style
                                                                                .TextDecoration
                                                                                .Underline
                                                        ),
                                                color = Color(0xFFFFFFFF),
                                                textAlign = TextAlign.Center
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text(
                                                hceStatus,
                                                style =
                                                        MaterialTheme.typography.bodyLarge.copy(
                                                                fontWeight = FontWeight.Bold
                                                        ),
                                                color =
                                                        if (isEmulating) Color(0xFF4CAF50)
                                                        else Color(0xFFFF9800),
                                                textAlign = TextAlign.Center
                                        )
                                }
                        }
                }

                // Attack Profile Selection
                Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121717)),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                        "Attack Profile",
                                        style =
                                                MaterialTheme.typography.titleMedium.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        textDecoration =
                                                                androidx.compose.ui.text.style
                                                                        .TextDecoration.Underline
                                                ),
                                        color = Color(0xFF4CAF50),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Attack profile grid - 2 columns
                                attackProfiles.chunked(2).forEach { rowProfiles ->
                                        Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                                rowProfiles.forEach { profile ->
                                                        Card(
                                                                modifier =
                                                                        Modifier.weight(1f)
                                                                                .clickable {
                                                                                        selectedAttack =
                                                                                                profile
                                                                                },
                                                                colors =
                                                                        CardDefaults.cardColors(
                                                                                containerColor =
                                                                                        if (selectedAttack ==
                                                                                                        profile
                                                                                        )
                                                                                                Color(
                                                                                                                0xFF4CAF50
                                                                                                        )
                                                                                                        .copy(
                                                                                                                alpha =
                                                                                                                        0.3f
                                                                                                        )
                                                                                        else
                                                                                                Color(
                                                                                                        0xFF2A2A2A
                                                                                                )
                                                                        ),
                                                                shape = RoundedCornerShape(6.dp)
                                                        ) {
                                                                Text(
                                                                        profile,
                                                                        style =
                                                                                MaterialTheme
                                                                                        .typography
                                                                                        .bodySmall
                                                                                        .copy(
                                                                                                fontWeight =
                                                                                                        FontWeight
                                                                                                                .Bold
                                                                                        ),
                                                                        color =
                                                                                if (selectedAttack ==
                                                                                                profile
                                                                                )
                                                                                        Color(
                                                                                                0xFFFFFFFF
                                                                                        )
                                                                                else
                                                                                        Color(
                                                                                                0xFF4CAF50
                                                                                        ),
                                                                        textAlign =
                                                                                TextAlign.Center,
                                                                        modifier =
                                                                                Modifier.padding(
                                                                                                8.dp
                                                                                        )
                                                                                        .fillMaxWidth(),
                                                                        maxLines = 2
                                                                )
                                                        }
                                                }
                                                // Fill empty space if odd number
                                                if (rowProfiles.size == 1) {
                                                        Spacer(modifier = Modifier.weight(1f))
                                                }
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                }
                        }
                }

                // Card Selection
                Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF121717)),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                        "Card Selection",
                                        style =
                                                MaterialTheme.typography.titleMedium.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        textDecoration =
                                                                androidx.compose.ui.text.style
                                                                        .TextDecoration.Underline
                                                ),
                                        color = Color(0xFF4CAF50),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                        items(sampleCards) { card ->
                                                Card(
                                                        modifier =
                                                                Modifier.width(160.dp)
                                                                        .height(100.dp)
                                                                        .clickable {
                                                                                selectedCard = card
                                                                        },
                                                        colors =
                                                                CardDefaults.cardColors(
                                                                        containerColor =
                                                                                if (selectedCard ==
                                                                                                card
                                                                                )
                                                                                        Color(
                                                                                                        0xFF4CAF50
                                                                                                )
                                                                                                .copy(
                                                                                                        alpha =
                                                                                                                0.3f
                                                                                                )
                                                                                else
                                                                                        Color(
                                                                                                0xFF2A2A2A
                                                                                        )
                                                                ),
                                                        shape = RoundedCornerShape(8.dp)
                                                ) {
                                                        Box(modifier = Modifier.fillMaxSize()) {
                                                                Image(
                                                                        painter =
                                                                                painterResource(
                                                                                        id =
                                                                                                R.drawable
                                                                                                        .nfspoof_logo
                                                                                ),
                                                                        contentDescription = null,
                                                                        contentScale =
                                                                                ContentScale.Crop,
                                                                        modifier =
                                                                                Modifier.fillMaxSize(),
                                                                        alpha = 0.05f
                                                                )

                                                                Box(
                                                                        modifier =
                                                                                Modifier.fillMaxSize()
                                                                                        .padding(
                                                                                                8.dp
                                                                                        )
                                                                ) {
                                                                        Text(
                                                                                card.cardType,
                                                                                style =
                                                                                        MaterialTheme
                                                                                                .typography
                                                                                                .labelSmall,
                                                                                fontWeight =
                                                                                        FontWeight
                                                                                                .Bold,
                                                                                color =
                                                                                        Color(
                                                                                                0xFFFFFFFF
                                                                                        ),
                                                                                modifier =
                                                                                        Modifier.align(
                                                                                                Alignment
                                                                                                        .TopEnd
                                                                                        )
                                                                        )

                                                                        Column(
                                                                                modifier =
                                                                                        Modifier.align(
                                                                                                Alignment
                                                                                                        .BottomStart
                                                                                        )
                                                                        ) {
                                                                                Text(
                                                                                        card.cardholderName,
                                                                                        style =
                                                                                                MaterialTheme
                                                                                                        .typography
                                                                                                        .labelSmall,
                                                                                        fontWeight =
                                                                                                FontWeight
                                                                                                        .Bold,
                                                                                        color =
                                                                                                Color(
                                                                                                        0xFFFFFFFF
                                                                                                ),
                                                                                        maxLines = 1
                                                                                )
                                                                                Text(
                                                                                        card.pan,
                                                                                        style =
                                                                                                MaterialTheme
                                                                                                        .typography
                                                                                                        .labelSmall,
                                                                                        color =
                                                                                                Color(
                                                                                                        0xFF4CAF50
                                                                                                ),
                                                                                        maxLines = 1
                                                                                )
                                                                        }
                                                                }
                                                        }
                                                }
                                        }
                                }
                        }
                }

                // Control Buttons
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                        Button(
                                onClick = {
                                        if (selectedCard != null) {
                                                isEmulating = !isEmulating
                                                hceStatus =
                                                        if (isEmulating)
                                                                "Emulating \${selectedCard!!.cardType} - \$selectedAttack"
                                                        else "HCE Service Ready"
                                        }
                                },
                                enabled = selectedCard != null,
                                colors =
                                        ButtonDefaults.buttonColors(
                                                containerColor =
                                                        if (isEmulating) Color(0xFFCF1B33)
                                                        else Color(0xFF4CAF50),
                                                contentColor = Color.White
                                        ),
                                modifier = Modifier.weight(1f)
                        ) {
                                if (isEmulating) {
                                        Icon(
                                                Icons.Default.Stop,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(if (isEmulating) "Stop Emulation" else "Start Emulation")
                        }

                        Button(
                                onClick = {
                                        selectedCard = null
                                        selectedAttack = "Standard Emulation"
                                        isEmulating = false
                                        hceStatus = "HCE Service Ready"
                                },
                                colors =
                                        ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF444444),
                                                contentColor = Color.White
                                        ),
                                modifier = Modifier.weight(1f)
                        ) { Text("Reset") }
                }

                // Selected card preview
                if (selectedCard != null) {
                        Card(
                                modifier = Modifier.fillMaxWidth().height(80.dp),
                                colors =
                                        CardDefaults.cardColors(containerColor = Color(0xFF121717)),
                                shape = RoundedCornerShape(8.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                        Image(
                                                painter =
                                                        painterResource(
                                                                id = R.drawable.nfspoof_logo
                                                        ),
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize(),
                                                alpha = 0.1f
                                        )

                                        Row(
                                                modifier = Modifier.fillMaxSize().padding(16.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                                Column {
                                                        Text(
                                                                "Loaded: ${selectedCard!!.cardholderName}",
                                                                style =
                                                                        MaterialTheme.typography
                                                                                .bodyMedium.copy(
                                                                                fontWeight =
                                                                                        FontWeight
                                                                                                .Bold
                                                                        ),
                                                                color = Color(0xFFFFFFFF)
                                                        )
                                                        Text(
                                                                "Profile: $selectedAttack",
                                                                style =
                                                                        MaterialTheme.typography
                                                                                .bodySmall,
                                                                color = Color(0xFF4CAF50)
                                                        )
                                                }

                                                Text(
                                                        selectedCard!!.cardType,
                                                        style =
                                                                MaterialTheme.typography.titleLarge
                                                                        .copy(
                                                                                fontWeight =
                                                                                        FontWeight
                                                                                                .Bold
                                                                        ),
                                                        color = Color(0xFF4CAF50)
                                                )
                                        }
                                }
                        }
                }

                // Live APDU Traffic
                Card(
                        modifier = Modifier.fillMaxWidth().height(200.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Black),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                        "Live APDU Traffic",
                                        style =
                                                MaterialTheme.typography.titleMedium.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        textDecoration =
                                                                androidx.compose.ui.text.style
                                                                        .TextDecoration.Underline
                                                ),
                                        color = Color(0xFF4CAF50),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                LazyColumn(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                        if (isEmulating) {
                                                // Sample emulation APDU traffic
                                                items(
                                                        listOf(
                                                                ApduLogEntry(
                                                                        command =
                                                                                "00A404000E325041592E5359532E444446303100",
                                                                        response =
                                                                                "6F2A840E325041592E5359532E4444463031A518BF0C1561104F07A0000000031010870101",
                                                                        statusWord = "9000",
                                                                        description = "SELECT PPSE",
                                                                        executionTimeMs = 15L
                                                                ),
                                                                ApduLogEntry(
                                                                        command =
                                                                                "00A4040007A000000003101000",
                                                                        response =
                                                                                "6F3A840E325041592E5359532E4444463031A528501A325041592E5359532E44444630314F07A0000000031010870101",
                                                                        statusWord = "9000",
                                                                        description = "SELECT AID",
                                                                        executionTimeMs = 12L
                                                                ),
                                                                ApduLogEntry(
                                                                        command =
                                                                                "80A8000002830000",
                                                                        response =
                                                                                "770A82021080940800010A01000000000000",
                                                                        statusWord = "9000",
                                                                        description =
                                                                                "GET PROCESSING OPTIONS",
                                                                        executionTimeMs = 18L
                                                                ),
                                                                ApduLogEntry(
                                                                        command = "00B2010C00",
                                                                        response =
                                                                                "70815A0842154154012345678D1212201201000000000000000F",
                                                                        statusWord = "9000",
                                                                        description = "READ RECORD",
                                                                        executionTimeMs = 22L
                                                                ),
                                                                ApduLogEntry(
                                                                        command =
                                                                                "80AE8000230000000100000000000000001A0342031E031F024C",
                                                                        response =
                                                                                "9F2701809F3602008C9F26088B15625643F21598",
                                                                        statusWord = "9000",
                                                                        description = "GENERATE AC",
                                                                        executionTimeMs = 45L
                                                                ),
                                                                ApduLogEntry(
                                                                        command = "00CA9F1300",
                                                                        response = "9F13024003",
                                                                        statusWord = "9000",
                                                                        description =
                                                                                "GET DATA - Last Online ATC",
                                                                        executionTimeMs = 8L
                                                                )
                                                        )
                                                ) { entry -> ApduLogItem(entry) }
                                        } else {
                                                item {
                                                        Text(
                                                                "Start emulation to see APDU traffic...",
                                                                style =
                                                                        MaterialTheme.typography
                                                                                .bodyLarge,
                                                                color =
                                                                        Color(0xFF4CAF50)
                                                                                .copy(alpha = 0.6f),
                                                                fontFamily = FontFamily.Monospace,
                                                                textAlign = TextAlign.Center,
                                                                modifier =
                                                                        Modifier.fillMaxWidth()
                                                                                .padding(32.dp)
                                                        )
                                                }
                                        }
                                }
                        }
                }
        }
}
