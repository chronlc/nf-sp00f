package com.example.nfsp00f

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

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
                                    Icon(
                                            icon,
                                            contentDescription = label,
                                            tint =
                                                    if (selectedTab == index) Color(0xFF4CAF50)
                                                    else Color(0xFF4CAF50).copy(alpha = 0.6f),
                                            modifier =
                                                    Modifier.then(
                                                            if (selectedTab == index) Modifier
                                                            else Modifier
                                                    )
                                    )
                                },
                                label = {
                                    Text(
                                            label,
                                            color =
                                                    if (selectedTab == index) Color(0xFF4CAF50)
                                                    else Color(0xFF4CAF50).copy(alpha = 0.6f),
                                            fontWeight =
                                                    if (selectedTab == index) FontWeight.Bold
                                                    else FontWeight.Normal
                                    )
                                },
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
                                                        Color.Transparent // Remove green oval
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
                                                    androidx.compose.ui.text.style.TextDecoration
                                                            .Underline
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
                                                    androidx.compose.ui.text.style.TextDecoration
                                                            .Underline
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
                        style =
                                MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold
                                ),
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
                            style =
                                    MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold
                                    ),
                            color = Color(0xFF4CAF50) // Bold PAN
                    )
                    Text(
                            card.expiry,
                            style =
                                    MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold
                                    ),
                            color = Color(0xFF4a4f54) // Bold expiry in gray
                    )
                }
            }
        }
    }
}

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

// APDU Log Entry for live traffic
data class ApduLogEntry(
        val timestamp: String,
        val direction: String, // "→" or "←"
        val command: String,
        val data: String,
        val statusWord: String = "",
        val parsed: String = ""
)

@Composable
fun CardReadingScreen() {
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
                        colors =
                                ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color(0xFF4CAF50)
                                ),
                        border = BorderStroke(1.dp, Color(0xFF4CAF50)),
                        modifier = Modifier.width(200.dp)
                ) {
                    Text(
                            selectedDevice?.displayName ?: "No Device Selected",
                            color =
                                    if (selectedDevice == null) Color(0xFFFFFFFF)
                                    else Color(0xFF4CAF50)
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
                                    kotlinx.coroutines.GlobalScope.launch {
                                        kotlinx.coroutines.delay(2000)
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
            Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                        "Reading Options",
                        style =
                                MaterialTheme.typography.titleMedium.copy(
                                        textDecoration =
                                                androidx.compose.ui.text.style.TextDecoration
                                                        .Underline
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
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
                                style =
                                        MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold
                                        ),
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
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                    entry.timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF4CAF50).copy(alpha = 0.7f),
                    fontFamily = FontFamily.Monospace
            )
            Text(
                    entry.direction,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (entry.direction == "→") Color(0xFF4CAF50) else Color(0xFFFF9800),
                    fontFamily = FontFamily.Monospace
            )
            Text(
                    entry.command,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color(0xFF4CAF50),
                    fontFamily = FontFamily.Monospace
            )
        }

        if (entry.data.isNotEmpty()) {
            Text(
                    "Data: ${entry.data}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF4CAF50).copy(alpha = 0.8f),
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(start = 16.dp)
            )
        }

        if (entry.statusWord.isNotEmpty()) {
            Text(
                    "SW: ${entry.statusWord}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF4CAF50).copy(alpha = 0.8f),
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(start = 16.dp)
            )
        }

        if (entry.parsed.isNotEmpty()) {
            Text(
                    "→ ${entry.parsed}",
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
    kotlinx.coroutines.GlobalScope.launch {
        kotlinx.coroutines.delay(3000) // Simulate reading time
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

@Composable
fun EmulationScreen() {
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
                                                    androidx.compose.ui.text.style.TextDecoration
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
                            color = if (isEmulating) Color(0xFF4CAF50) else Color(0xFFFF9800),
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
                                                androidx.compose.ui.text.style.TextDecoration
                                                        .Underline
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
                                            Modifier.weight(1f).clickable {
                                                selectedAttack = profile
                                            },
                                    colors =
                                            CardDefaults.cardColors(
                                                    containerColor =
                                                            if (selectedAttack == profile)
                                                                    Color(0xFF4CAF50)
                                                                            .copy(alpha = 0.3f)
                                                            else Color(0xFF2A2A2A)
                                            ),
                                    shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                        profile,
                                        style =
                                                MaterialTheme.typography.bodySmall.copy(
                                                        fontWeight = FontWeight.Bold
                                                ),
                                        color =
                                                if (selectedAttack == profile) Color(0xFFFFFFFF)
                                                else Color(0xFF4CAF50),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.padding(8.dp).fillMaxWidth(),
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
                                                androidx.compose.ui.text.style.TextDecoration
                                                        .Underline
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
                                        Modifier.width(160.dp).height(100.dp).clickable {
                                            selectedCard = card
                                        },
                                colors =
                                        CardDefaults.cardColors(
                                                containerColor =
                                                        if (selectedCard == card)
                                                                Color(0xFF4CAF50).copy(alpha = 0.3f)
                                                        else Color(0xFF2A2A2A)
                                        ),
                                shape = RoundedCornerShape(8.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                Image(
                                        painter = painterResource(id = R.drawable.nfspoof_logo),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize(),
                                        alpha = 0.05f
                                )

                                Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                                    Text(
                                            card.cardType,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFFFFFFF),
                                            modifier = Modifier.align(Alignment.TopEnd)
                                    )

                                    Column(modifier = Modifier.align(Alignment.BottomStart)) {
                                        Text(
                                                card.cardholderName,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFFFFFFF),
                                                maxLines = 1
                                        )
                                        Text(
                                                card.pan,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color(0xFF4CAF50),
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
                                            "Emulating ${selectedCard!!.cardType} - ${selectedAttack}"
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
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF121717)),
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                            painter = painterResource(id = R.drawable.nfspoof_logo),
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
                                            MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Bold
                                            ),
                                    color = Color(0xFFFFFFFF)
                            )
                            Text(
                                    "Profile: $selectedAttack",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF4CAF50)
                            )
                        }

                        Text(
                                selectedCard!!.cardType,
                                style =
                                        MaterialTheme.typography.titleLarge.copy(
                                                fontWeight = FontWeight.Bold
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
                                                androidx.compose.ui.text.style.TextDecoration
                                                        .Underline
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
                                                "12:34:56",
                                                "←",
                                                "SELECT PPSE",
                                                "00A404000E325041592E5359532E444446303100",
                                                "9000",
                                                "OK"
                                        ),
                                        ApduLogEntry(
                                                "12:34:57",
                                                "→",
                                                "RESPONSE",
                                                "6F2A840E325041592E5359532E4444463031A518BF0C1561104F07A0000000031010870101",
                                                "",
                                                "PPSE"
                                        ),
                                        ApduLogEntry(
                                                "12:34:58",
                                                "←",
                                                "SELECT AID",
                                                "00A4040007A000000003101000",
                                                "9000",
                                                "OK"
                                        ),
                                        ApduLogEntry(
                                                "12:34:59",
                                                "→",
                                                "RESPONSE",
                                                "6F3B840EA0000000031010A523BF0C20611E4F07A0000000031010870101",
                                                "",
                                                "VISA"
                                        ),
                                        ApduLogEntry(
                                                "12:35:00",
                                                "←",
                                                "GPO",
                                                "80A80000028300",
                                                "9000",
                                                "OK"
                                        ),
                                        ApduLogEntry(
                                                "12:35:01",
                                                "→",
                                                "RESPONSE",
                                                "77288202200094081801010210010101",
                                                "",
                                                "Track2"
                                        )
                                )
                        ) { entry -> ApduLogItem(entry) }
                    } else {
                        item {
                            Text(
                                    "Start emulation to see APDU traffic...",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color(0xFF4CAF50).copy(alpha = 0.6f),
                                    fontFamily = FontFamily.Monospace,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth().padding(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DatabaseScreen() {
    var searchText by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("All") }
    var showAddCardDialog by remember { mutableStateOf(false) }

    val databaseCards =
            listOf(
                    DatabaseCard(
                            "JOHN DOE",
                            "4154 **** **** 3556",
                            "02/29",
                            47,
                            "VISA",
                            "Real",
                            true,
                            "2h ago"
                    ),
                    DatabaseCard(
                            "TEST CARD",
                            "4000 **** **** 0002",
                            "12/25",
                            12,
                            "VISA",
                            "Test",
                            false,
                            "1d ago"
                    ),
                    DatabaseCard(
                            "ALICE CRYPTO",
                            "5555 **** **** 4444",
                            "06/27",
                            156,
                            "MC",
                            "Attack",
                            true,
                            "5m ago"
                    ),
                    DatabaseCard(
                            "BOB RESEARCH",
                            "3782 **** **** 1007",
                            "03/28",
                            89,
                            "AMEX",
                            "Real",
                            true,
                            "1h ago"
                    ),
                    DatabaseCard(
                            "FUZZER PROFILE",
                            "6011 **** **** 0004",
                            "09/26",
                            234,
                            "DISC",
                            "Attack",
                            false,
                            "30m ago"
                    ),
                    DatabaseCard(
                            "JANE SMITH",
                            "4111 **** **** 1111",
                            "11/29",
                            67,
                            "VISA",
                            "Test",
                            true,
                            "3h ago"
                    )
            )

    val filteredCards =
            if (selectedFilter == "All") {
                databaseCards.filter {
                    it.cardholderName.contains(searchText, ignoreCase = true) ||
                            it.pan.contains(searchText, ignoreCase = true)
                }
            } else {
                databaseCards.filter {
                    it.category == selectedFilter &&
                            (it.cardholderName.contains(searchText, ignoreCase = true) ||
                                    it.pan.contains(searchText, ignoreCase = true))
                }
            }

    Column(
            modifier =
                    Modifier.fillMaxSize()
                            .background(
                                    Brush.verticalGradient(
                                            colors =
                                                    listOf(
                                                            Color(0xFF0F0F0F),
                                                            Color(0xFF1A1A1A),
                                                            Color(0xFF0F0F0F)
                                                    )
                                    )
                            )
                            .padding(16.dp)
    ) {
        // Header with nfspoof background
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
                        modifier = Modifier.fillMaxWidth().height(80.dp),
                        alpha = 0.1f
                )

                Column(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                            "CARD DATABASE",
                            style =
                                    MaterialTheme.typography.headlineLarge.copy(
                                            fontWeight = FontWeight.Bold
                                    ),
                            color = Color(0xFF4CAF50),
                            textAlign = TextAlign.Center
                    )
                    Text(
                            "EMV Security Research Archive",
                            style =
                                    MaterialTheme.typography.titleMedium.copy(
                                            textDecoration =
                                                    androidx.compose.ui.text.style.TextDecoration
                                                            .Underline
                                    ),
                            color = Color(0xFFFFFFFF),
                            textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search and Filter Row
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Search Field
            OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    label = { Text("Search cards...", color = Color(0xFF4CAF50)) },
                    leadingIcon = {
                        Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = Color(0xFF4CAF50)
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors =
                            OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF4CAF50),
                                    unfocusedBorderColor = Color(0xFF4CAF50).copy(alpha = 0.5f),
                                    focusedTextColor = Color.White,
                                    unfocusedTextColor = Color.White
                            )
            )

            // Filter Dropdown
            var expanded by remember { mutableStateOf(false) }
            val filters = listOf("All", "Real", "Test", "Attack")

            Box {
                OutlinedButton(
                        onClick = { expanded = true },
                        colors =
                                ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color(0xFF4CAF50)
                                ),
                        border = BorderStroke(1.dp, Color(0xFF4CAF50))
                ) {
                    Text(selectedFilter)
                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                }

                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    filters.forEach { filter ->
                        DropdownMenuItem(
                                text = { Text(filter, color = Color.White) },
                                onClick = {
                                    selectedFilter = filter
                                    expanded = false
                                }
                        )
                    }
                }
            }

            // Add Card Button
            Button(
                    onClick = { showAddCardDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text("Add Card", color = Color.Black)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Statistics Row
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DatabaseStatCard(
                    "Total Cards",
                    databaseCards.size.toString(),
                    Icons.Default.CreditCard,
                    Modifier.weight(1f)
            )
            DatabaseStatCard(
                    "Encrypted",
                    databaseCards.count { it.isEncrypted }.toString(),
                    Icons.Default.Security,
                    Modifier.weight(1f)
            )
            DatabaseStatCard(
                    "Categories",
                    databaseCards.map { it.category }.distinct().size.toString(),
                    Icons.Default.Category,
                    Modifier.weight(1f)
            )
            DatabaseStatCard(
                    "Total APDUs",
                    databaseCards.sumOf { it.apduCount }.toString(),
                    Icons.Default.DataArray,
                    Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Cards Grid
        LazyVerticalGrid(
                columns =
                        GridCells.Fixed(
                                if (LocalConfiguration.current.screenWidthDp > 600) 2 else 1
                        ),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
        ) {
            items(filteredCards) { card ->
                DatabaseVirtualCard(
                        card = card,
                        onEdit = { /* Handle edit */},
                        onClone = { /* Handle clone */},
                        onDelete = { /* Handle delete */},
                        onExport = { /* Handle export */},
                        onViewHistory = { /* Handle view APDU history */}
                )
            }
        }
    }

    // Add Card Dialog (placeholder for now)
    if (showAddCardDialog) {
        AlertDialog(
                onDismissRequest = { showAddCardDialog = false },
                title = { Text("Add New Card", color = Color.White) },
                text = { Text("Card creation dialog would go here", color = Color.White) },
                confirmButton = {
                    TextButton(onClick = { showAddCardDialog = false }) {
                        Text("OK", color = Color(0xFF4CAF50))
                    }
                }
        )
    }
}

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

@Composable
fun DatabaseStatCard(
        title: String,
        value: String,
        icon: androidx.compose.ui.graphics.vector.ImageVector,
        modifier: Modifier = Modifier
) {
    Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(containerColor = Color(0xFF121717)),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
                modifier = Modifier.padding(12.dp),
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
                    style =
                            MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold
                            ),
                    color = Color(0xFF4CAF50)
            )
            Text(
                    title,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFAAAAAA),
                    textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DatabaseVirtualCard(
        card: DatabaseCard,
        onEdit: () -> Unit,
        onClone: () -> Unit,
        onDelete: () -> Unit,
        onExport: () -> Unit,
        onViewHistory: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF121717)),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background logo
            Image(
                    painter = painterResource(id = R.drawable.nfspoof_logo),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize(),
                    alpha = 0.1f
            )

            // Card content
            Column(
                    modifier = Modifier.padding(16.dp).fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header with menu
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                                card.cardType,
                                style =
                                        MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold
                                        ),
                                color = Color(0xFF4CAF50)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Category badge
                            val categoryColor =
                                    when (card.category) {
                                        "Real" -> Color(0xFF4CAF50)
                                        "Test" -> Color(0xFF2196F3)
                                        "Attack" -> Color(0xFFFF5722)
                                        else -> Color(0xFF9E9E9E)
                                    }
                            Box(
                                    modifier =
                                            Modifier.background(
                                                            categoryColor,
                                                            RoundedCornerShape(4.dp)
                                                    )
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                        card.category,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            if (card.isEncrypted) {
                                Icon(
                                        Icons.Default.Security,
                                        contentDescription = "Encrypted",
                                        tint = Color(0xFF4CAF50),
                                        modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    // Menu button
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                    Icons.Default.MoreVert,
                                    contentDescription = "Menu",
                                    tint = Color(0xFF4CAF50)
                            )
                        }

                        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                            DropdownMenuItem(
                                    text = { Text("Edit", color = Color.White) },
                                    leadingIcon = {
                                        Icon(
                                                Icons.Default.Edit,
                                                contentDescription = null,
                                                tint = Color(0xFF4CAF50)
                                        )
                                    },
                                    onClick = {
                                        onEdit()
                                        showMenu = false
                                    }
                            )
                            DropdownMenuItem(
                                    text = { Text("Clone", color = Color.White) },
                                    leadingIcon = {
                                        Icon(
                                                Icons.Default.ContentCopy,
                                                contentDescription = null,
                                                tint = Color(0xFF4CAF50)
                                        )
                                    },
                                    onClick = {
                                        onClone()
                                        showMenu = false
                                    }
                            )
                            DropdownMenuItem(
                                    text = { Text("APDU History", color = Color.White) },
                                    leadingIcon = {
                                        Icon(
                                                Icons.Default.History,
                                                contentDescription = null,
                                                tint = Color(0xFF4CAF50)
                                        )
                                    },
                                    onClick = {
                                        onViewHistory()
                                        showMenu = false
                                    }
                            )
                            DropdownMenuItem(
                                    text = { Text("Export", color = Color.White) },
                                    leadingIcon = {
                                        Icon(
                                                Icons.Default.FileDownload,
                                                contentDescription = null,
                                                tint = Color(0xFF4CAF50)
                                        )
                                    },
                                    onClick = {
                                        onExport()
                                        showMenu = false
                                    }
                            )
                            DropdownMenuItem(
                                    text = { Text("Delete", color = Color(0xFFFF5722)) },
                                    leadingIcon = {
                                        Icon(
                                                Icons.Default.Delete,
                                                contentDescription = null,
                                                tint = Color(0xFFFF5722)
                                        )
                                    },
                                    onClick = {
                                        onDelete()
                                        showMenu = false
                                    }
                            )
                        }
                    }
                }

                // Card details
                Column {
                    Text(
                            card.cardholderName,
                            style =
                                    MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold
                                    ),
                            color = Color.White
                    )
                    Text(
                            card.pan,
                            style =
                                    MaterialTheme.typography.titleLarge.copy(
                                            fontFamily = FontFamily.Monospace,
                                            letterSpacing = 2.sp
                                    ),
                            color = Color(0xFF4CAF50)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                    "EXP",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFFAAAAAA)
                            )
                            Text(
                                    card.expiry,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                    "${card.apduCount} APDUs",
                                    style =
                                            MaterialTheme.typography.bodySmall.copy(
                                                    fontWeight = FontWeight.Bold
                                            ),
                                    color = Color(0xFF4CAF50)
                            )
                            Text(
                                    "Last used: ${card.lastUsed}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFFAAAAAA)
                            )
                        }
                    }
                }
            }
        }
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
