package com.example.nfsp00f

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { NfSp00fTheme { NfSp00fApp() } }
    }
}

@Composable
fun NfSp00fTheme(content: @Composable () -> Unit) {
    MaterialTheme(
            colorScheme =
                    darkColorScheme(
                            primary = Color(0xFF4CAF50),
                            secondary = Color(0xFF4CAF50),
                            background = Color.Black,
                            surface = Color(0xFF1E1E1E),
                            onSurface = Color(0xFF4CAF50),
                            onBackground = Color(0xFF4CAF50),
                            surfaceVariant = Color(0xFF2D2D2D),
                            onSurfaceVariant = Color(0xFF4CAF50)
                    ),
            typography =
                    Typography(
                            // Headlines: Roboto Medium 24sp/20sp
                            headlineLarge =
                                    MaterialTheme.typography.headlineLarge.copy(
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 24.sp,
                                            lineHeight = 32.sp,
                                            letterSpacing = 0.sp
                                    ),
                            headlineMedium =
                                    MaterialTheme.typography.headlineMedium.copy(
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 20.sp,
                                            lineHeight = 28.sp,
                                            letterSpacing = 0.sp
                                    ),
                            // Titles: Roboto Regular 18sp/16sp
                            titleLarge =
                                    MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 18.sp,
                                            lineHeight = 24.sp,
                                            letterSpacing = 0.sp
                                    ),
                            titleMedium =
                                    MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 16.sp,
                                            lineHeight = 22.sp,
                                            letterSpacing = 0.15.sp
                                    ),
                            // Body: Roboto Regular 14sp
                            bodyLarge =
                                    MaterialTheme.typography.bodyLarge.copy(
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 14.sp,
                                            lineHeight = 20.sp,
                                            letterSpacing = 0.25.sp
                                    ),
                            // Captions: Roboto Regular 12sp
                            labelSmall =
                                    MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 12.sp,
                                            lineHeight = 16.sp,
                                            letterSpacing = 0.5.sp
                                    )
                    ),
            content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NfSp00fApp() {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
            topBar = {
                TopAppBar(
                        title = {
                            Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                        Icons.Default.Security,
                                        contentDescription = "Security Shield",
                                        tint = Color(0xFF4CAF50)
                                )
                                Text(
                                        "nf-sp00f",
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF4CAF50)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
                )
            },
            bottomBar = {
                NavigationBar(containerColor = Color.Black) {
                    val items =
                            listOf(
                                    "Dashboard" to Icons.Default.Dashboard,
                                    "Read" to Icons.Default.Nfc,
                                    "Emulate" to Icons.Default.Security,
                                    "Database" to Icons.Default.Storage,
                                    "Analysis" to Icons.Default.Analytics
                            )

                    items.forEachIndexed { index, (label, icon) ->
                        NavigationBarItem(
                                icon = {
                                    Icon(icon, contentDescription = label, tint = Color(0xFF4CAF50))
                                },
                                label = { Text(label, color = Color(0xFF4CAF50)) },
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                colors =
                                        NavigationBarItemDefaults.colors(
                                                selectedIconColor = Color(0xFF4CAF50),
                                                unselectedIconColor =
                                                        Color(0xFF4CAF50).copy(alpha = 0.6f),
                                                selectedTextColor = Color(0xFF4CAF50),
                                                unselectedTextColor =
                                                        Color(0xFF4CAF50).copy(alpha = 0.6f),
                                                indicatorColor =
                                                        Color(0xFF4CAF50).copy(alpha = 0.2f)
                                        )
                        )
                    }
                }
            }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (selectedTab) {
                0 -> DashboardScreen()
                1 -> CardReadingScreen()
                2 -> EmulationScreen()
                3 -> DatabaseScreen()
                4 -> AnalysisScreen()
            }
        }
    }
}

data class VirtualCard(
        val cardholderName: String,
        val pan: String,
        val expiry: String,
        val apduCount: Int,
        val cardType: String
)

@Composable
fun DashboardScreen() {
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
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
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
                            style = MaterialTheme.typography.headlineMedium, // 20sp Medium
                            color = Color(0xFF4CAF50),
                            textAlign = TextAlign.Center
                    )
                    Text(
                            "RFiD TooLKiT",
                            style = MaterialTheme.typography.titleMedium, // 16sp Regular
                            color = Color(0xFF4CAF50),
                            textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                            "System Status",
                            style =
                                    MaterialTheme.typography
                                            .titleLarge, // 18sp Regular per typography scale
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
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
                style = MaterialTheme.typography.bodyLarge // 14sp Body
        )
        Text(
                status,
                color = if (isGood) Color(0xFF4CAF50) else Color(0xFFcf1b33),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
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
            modifier = modifier,
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
            shape = RoundedCornerShape(8.dp), // 8dp corner radius
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp) // 4dp elevation
    ) {
        Column(
                modifier = Modifier.padding(16.dp), // 16dp padding per design spec
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                    icon,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                    value,
                    style = MaterialTheme.typography.headlineMedium, // 20sp Medium
                    color = Color(0xFF4CAF50)
            )
            Text(
                    title,
                    style = MaterialTheme.typography.labelSmall, // 12sp Regular (Captions)
                    color = Color(0xFF4CAF50),
                    textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun VirtualCardView(card: VirtualCard) {
    Card(
            modifier = Modifier.width(200.dp).height(120.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2E2E2E)),
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

            Column(
                    modifier = Modifier.padding(16.dp), // 16dp padding
                    verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                                card.cardholderName,
                                style = MaterialTheme.typography.labelSmall, // 12sp Captions
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4CAF50)
                        )
                        Text(
                                card.pan,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF4CAF50)
                        )
                        Text(
                                card.expiry,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF4CAF50)
                        )
                    }
                    Text(
                            card.cardType,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50)
                    )
                }

                Text(
                        "${card.apduCount} APDUs",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF4CAF50)
                )
            }
        }
    }
}

@Composable
fun CardReadingScreen() {
    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
    ) {
        Icon(
                Icons.Default.Nfc,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color(0xFF4CAF50)
        )
        Text(
                "NFC Card Reader",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50)
        )
        Text(
                "Ready to read EMV cards",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF4CAF50)
        )
    }
}

@Composable
fun EmulationScreen() {
    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
    ) {
        Icon(
                Icons.Default.Security,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color(0xFF4CAF50)
        )
        Text(
                "HCE Emulation",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50)
        )
        Text(
                "EMV Attack Profiles Ready",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF4CAF50)
        )
    }
}

@Composable
fun DatabaseScreen() {
    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
    ) {
        Icon(
                Icons.Default.Storage,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color(0xFF4CAF50)
        )
        Text(
                "Card Database",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50)
        )
        Text(
                "Manage saved EMV cards",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF4CAF50)
        )
    }
}

@Composable
fun AnalysisScreen() {
    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
    ) {
        Icon(
                Icons.Default.Analytics,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = Color(0xFF4CAF50)
        )
        Text(
                "EMV Analysis",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50)
        )
        Text(
                "Security Research Tools",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFF4CAF50)
        )
    }
}
