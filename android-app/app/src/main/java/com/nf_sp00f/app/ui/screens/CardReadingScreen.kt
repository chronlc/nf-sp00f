package com.nf_sp00f.app.ui.screens
<<<<<<< HEAD
import com.nf_sp00f.app.data.*
import com.nf_sp00f.app.R

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
=======

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
>>>>>>> 52c0655 (🎯 Complete Priority 1-3: Production-grade CardReadingScreen with EmvWorkflowProcessor)
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
<<<<<<< HEAD
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
=======
>>>>>>> 52c0655 (🎯 Complete Priority 1-3: Production-grade CardReadingScreen with EmvWorkflowProcessor)
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
<<<<<<< HEAD
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
                modifier =
                        Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState()),
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
                                style =
                                        MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold
                                        ),
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
                                                        if (selectedDevice == null)
                                                                Color(0xFFFFFFFF)
                                                        else Color(0xFF4CAF50)
                                        )
                                        Icon(
                                                Icons.Default.ArrowDropDown,
                                                contentDescription = "Dropdown",
                                                tint = Color(0xFF4CAF50)
                                }
                                DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false }
                                        NfcDevice.values().forEach { device ->
                                                DropdownMenuItem(
                                                        text = {
                                                                Text(
                                                                        device.displayName,
                                                                        color = Color(0xFF4CAF50)
                                                                )
                                                        },
                                                        onClick = {
                                                                selectedDevice = device
                                                                expanded = false
                                                                // Simulate connection attempt
                                                                deviceState = DeviceState.CONNECTING
                                                                deviceStatusText =
                                                                        "Connecting to ${device.displayName}..."
                                                                // Simulate connection success after
                                                                // delay
                                                                GlobalScope.launch {
                                                                        delay(2000)
                                                                        deviceState =
                                                                                DeviceState
                                                                                        .CONNECTED
                                                                        deviceStatusText =
                                                                                "${device.displayName} Connected"
                                                                }
                                                        }
                                                )
                                        }
                        }
                }
                // Device Status
                        deviceStatusText,
                        style = MaterialTheme.typography.bodyLarge,
                        color =
                                when (deviceState) {
                                        DeviceState.CONNECTED -> Color(0xFF4CAF50)
                                        DeviceState.CONNECTING -> Color(0xFFFF9800)
                                        DeviceState.ERROR -> Color(0xFFCF1B33)
                                        DeviceState.NOT_SELECTED ->
                                                Color(0xFF4CAF50).copy(alpha = 0.7f)
                                },
                        fontWeight = FontWeight.Medium
                // Reading Options Checkboxes - Dashboard Style
                Card(
                        colors =
                                CardDefaults.cardColors(
                                        containerColor = Color(0xFF121717)
                                ), // Same as dashboard
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                                Text(
                                        "Reading Options",
                                        style =
                                                MaterialTheme.typography.titleMedium.copy(
                                                        textDecoration =
                                                                androidx.compose.ui.text.style
                                                                        .TextDecoration.Underline
                                        color = Color(0xFF4CAF50),
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                )
                                // First row: Single Card and Multi Card
                                Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                        CheckboxRow(
                                                "Single Card",
                                                singleCard,
                                                Modifier.weight(1f)
                                        ) {
                                                singleCard = it
                                                if (it) multiCard = false
                                        CheckboxRow("Multi Card", multiCard, Modifier.weight(1f)) {
                                                multiCard = it
                                                if (it) singleCard = false
                                // Second row: Stealth and EMV Dump
                                        CheckboxRow("Stealth", stealth, Modifier.weight(1f)) {
                                                stealth = it
                                        CheckboxRow("EMV Dump", emvDump, Modifier.weight(1f)) {
                                                emvDump = it
                // Control Buttons
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                        Button(
                                onClick = {
                                        if (deviceState == DeviceState.CONNECTED) {
                                                isReading = true
                                                // Simulate card reading
                                                simulateCardRead { card ->
                                                        readCard = card
                                                        isReading = false
                                                }
                                enabled = deviceState == DeviceState.CONNECTED && !isReading,
                                colors =
                                        ButtonDefaults.buttonColors(
                                                containerColor = Color(0xFF4CAF50),
                                                contentColor = Color.Black
                                modifier = Modifier.weight(1f)
                                if (isReading) {
                                        CircularProgressIndicator(
                                                modifier = Modifier.size(16.dp),
                                                color = Color.Black,
                                                strokeWidth = 2.dp
                                        Spacer(modifier = Modifier.width(8.dp))
                                Text("Read Card(s)")
                                        isReading = false
                                        readCard = null
                                        apduLog = emptyList()
                                enabled = isReading,
                                                containerColor = Color(0xFFCF1B33),
                                                contentColor = Color.White
                        ) { Text("Stop") }
                // Virtual Card Display - Dashboard Style
                if (readCard != null) {
                        Card(
                                modifier =
                                        Modifier.fillMaxWidth(0.75f)
                                                .height(120.dp), // Smaller width
                                        CardDefaults.cardColors(
                                                containerColor = Color(0xFF121717)
                                        ), // Same dark background
                                shape = RoundedCornerShape(8.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                                Box(modifier = Modifier.fillMaxSize()) {
                                        // Subtle background - same as dashboard
                                        Image(
                                                painter =
                                                        painterResource(
                                                                id = R.drawable.nfspoof_logo
                                                        ),
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize(),
                                                alpha = 0.1f
                                        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                                                // APDU count in upper left (same as dashboard)
                                                Text(
                                                        "${readCard!!.apduCount} APDUs",
                                                        style =
                                                                MaterialTheme.typography.labelSmall
                                                                        .copy(
                                                                                fontWeight =
                                                                                        FontWeight
                                                                                                .Bold
                                                                        ),
                                                        color =
                                                                Color(
                                                                        0xFF4a4f54
                                                                ), // Gray like dashboard
                                                        modifier =
                                                                Modifier.align(Alignment.TopStart)
                                                // Card brand in upper right (same as dashboard)
                                                        readCard!!.cardType,
                                                        style = MaterialTheme.typography.labelSmall,
                                                        fontWeight = FontWeight.Bold,
                                                                        0xFFFFFFFF
                                                                ), // White like dashboard
                                                        modifier = Modifier.align(Alignment.TopEnd)
                                                // Cardholder info in bottom left corner (same as
                                                // dashboard)
                                                Column(
                                                                Modifier.align(
                                                                        Alignment.BottomStart
                                                ) {
                                                        Text(
                                                                readCard!!.cardholderName,
                                                                style =
                                                                        MaterialTheme.typography
                                                                                .labelSmall,
                                                                fontWeight = FontWeight.Bold,
                                                                color =
                                                                        Color(
                                                                                0xFFFFFFFF
                                                                        ) // White like dashboard
                                                        )
                                                                readCard!!.pan,
                                                                                .labelSmall.copy(
                                                                                0xFF4CAF50
                                                                        ) // Green like dashboard
                                                                readCard!!.expiry,
                                                                                0xFF4a4f54
                                                                        ) // Gray like dashboard
                } else {
                        // Blank card placeholder - Dashboard Style
                                        // Add background image like dashboard cards
                                        Box(
                                                contentAlignment = Alignment.Center
                                                        "No Card Read",
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        color = Color(0xFF4CAF50).copy(alpha = 0.6f)
                // Live APDU Traffic
                        modifier = Modifier.fillMaxWidth().height(300.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.Black),
                        Column(modifier = Modifier.padding(16.dp)) {
                                        "Live APDU Traffic",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                Spacer(modifier = Modifier.height(8.dp))
                                LazyColumn(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                        items(apduLog) { entry -> ApduLogItem(entry) }
                                        if (apduLog.isEmpty()) {
                                                item {
                                                                "Waiting for APDU traffic...",
                                                                                .bodyLarge,
                                                                        Color(0xFF4CAF50)
                                                                                .copy(alpha = 0.6f),
                                                                fontFamily = FontFamily.Monospace
        }
}
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
                Checkbox(
                        checked = checked,
                        onCheckedChange = onCheckedChange,
                                CheckboxDefaults.colors(
                                        checkedColor = Color(0xFF4CAF50),
                                        uncheckedColor = Color(0xFF4CAF50),
                                        checkmarkColor = Color.Black
                                ),
                        modifier = Modifier.size(18.dp)
                        label,
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp),
                        color = Color(0xFF4CAF50)
fun ApduLogItem(entry: ApduLogEntry) {
        Column(modifier = Modifier.fillMaxWidth()) {
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                entry.getFormattedTimestamp(),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF4CAF50).copy(alpha = 0.7f),
                                fontFamily = FontFamily.Monospace
                                "→",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color(0xFF4CAF50),
                                entry.getCommandName(),
                if (entry.command.isNotEmpty()) {
                                "CMD: ${entry.command}",
                                color = Color(0xFF4CAF50).copy(alpha = 0.8f),
                                fontFamily = FontFamily.Monospace,
                                modifier = Modifier.padding(start = 16.dp)
                if (entry.response.isNotEmpty()) {
                                "RSP: ${entry.response}",
                                color = Color(0xFFFF9800).copy(alpha = 0.8f),
                if (entry.statusWord.isNotEmpty()) {
                                "SW: ${entry.statusWord} (${entry.getStatusMeaning()})",
                                color =
                                        if (entry.isSuccess()) Color(0xFF4CAF50)
                                        else Color(0xFFFF5722),
                if (entry.description.isNotEmpty()) {
                                "→ ${entry.description}",
                                color = Color(0xFFFF9800),
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
                onCardRead(simulatedCard)
=======
import com.nf_sp00f.app.data.NfcDevice
import com.nf_sp00f.app.data.EmvCardData
import com.nf_sp00f.app.data.CardVendor
import com.nf_sp00f.app.hardware.NfcAdapterManager
import com.nf_sp00f.app.hardware.PermissionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class EmvWorkflowProcessor(
    private val nfcAdapterManager: NfcAdapterManager,
    private val onLogEntry: (String, String) -> Unit
) {
    suspend fun performPn532EmvWorkflow(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                onLogEntry("Beginning EMV card analysis", "INFO")
                
                val ppseSuccess = performPpseDiscovery()
                
                if (!ppseSuccess) {
                    onLogEntry("PPSE failed, attempting direct AID search", "INFO")
                    performDirectAidSearch()
                } else {
                    onLogEntry("PPSE workflow completed successfully", "SUCCESS")
                    true
                }
                
            } catch (e: Exception) {
                onLogEntry("PN532 EMV workflow error: ${e.message}", "ERROR")
                false
            }
        }
    }

    private suspend fun performPpseDiscovery(): Boolean {
        val ppseCommand = byteArrayOf(
            0x00, 0xA4.toByte(), 0x04, 0x00, 0x0E,
            0x32, 0x50, 0x41, 0x59, 0x2E, 0x53, 0x59, 0x53, 0x2E, 0x44, 0x44, 0x46, 0x30, 0x31,
            0x00
        )
        
        onLogEntry("SELECT PPSE: ${ppseCommand.joinToString(" ") { "%02X".format(it) }}", "TX")
        val ppseResponse = nfcAdapterManager.sendApduCommand(ppseCommand)
        
        if (ppseResponse != null && ppseResponse.size >= 2) {
            val sw = ppseResponse.copyOfRange(ppseResponse.size - 2, ppseResponse.size)
            onLogEntry("PPSE Response: ${ppseResponse.joinToString(" ") { "%02X".format(it) }}", "RX")
            onLogEntry("Status: ${parseStatusWord(sw)}", "INFO")
            
            if (parseStatusWord(sw) == "Success") {
                val aids = extractAidsFromPpseResponse(ppseResponse)
                onLogEntry("Found ${aids.size} AID(s) in PPSE", "SUCCESS")
                
                if (aids.isNotEmpty()) {
                    return processSelectedAid(aids.first())
                }
            }
        } else {
            onLogEntry("PPSE selection failed - no response", "ERROR")
        }
        
        return false
    }

    private fun extractAidsFromPpseResponse(response: ByteArray): List<ByteArray> {
        val aids = mutableListOf<ByteArray>()
        var i = 0
        
        while (i < response.size - 2) {
            if (response[i] == 0x4F.toByte()) {
                val length = response[i + 1].toInt() and 0xFF
                if (i + 2 + length <= response.size - 2) {
                    val aid = response.copyOfRange(i + 2, i + 2 + length)
                    aids.add(aid)
                    onLogEntry("Extracted AID: ${aid.joinToString("") { "%02X".format(it) }}", "INFO")
                }
                i += 2 + length
            } else {
                i++
            }
        }
        
        return aids
    }

    private suspend fun performDirectAidSearch(): Boolean {
        val knownAids = listOf(
            "A0000000031010",    // VISA Classic
            "A0000000041010",    // MasterCard
            "A000000025",        // American Express
            "A0000001523010",    // Discover
            "A0000000651010",    // JCB
            "A0000000980840",    // US Debit
            "A0000000421010",    // CB (France)
            "A0000001410001",    // PagoBANCOMAT
            "A0000002771010",    // INTERAC
            "A0000005241010"     // RuPay
        )
        
        onLogEntry("Attempting direct AID search (${knownAids.size} AIDs)", "INFO")
        
        for (aidHex in knownAids) {
            try {
                val aid = aidHex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
                val selectCommand = byteArrayOf(0x00, 0xA4.toByte(), 0x04, 0x00, aid.size.toByte()) + aid + byteArrayOf(0x00)
                
                onLogEntry("Trying AID: $aidHex", "INFO")
                val response = nfcAdapterManager.sendApduCommand(selectCommand)
                
                if (response != null && response.size >= 2) {
                    val sw = response.copyOfRange(response.size - 2, response.size)
                    if (parseStatusWord(sw) == "Success") {
                        onLogEntry("AID $aidHex selected successfully", "SUCCESS")
                        return processSelectedAid(aid)
                    }
                }
            } catch (e: Exception) {
                onLogEntry("AID $aidHex failed: ${e.message}", "ERROR")
            }
        }
        
        return false
    }

    private suspend fun processSelectedAid(aid: ByteArray): Boolean {
        try {
            val selectAidCommand = byteArrayOf(0x00, 0xA4.toByte(), 0x04, 0x00, aid.size.toByte()) + aid + byteArrayOf(0x00)
            onLogEntry("SELECT AID: ${selectAidCommand.joinToString(" ") { "%02X".format(it) }}", "TX")
            
            val aidResponse = nfcAdapterManager.sendApduCommand(selectAidCommand)
            if (aidResponse != null) {
                val sw = aidResponse.copyOfRange(aidResponse.size - 2, aidResponse.size)
                onLogEntry("AID Response: ${aidResponse.joinToString(" ") { "%02X".format(it) }}", "RX")
                onLogEntry("Status: ${parseStatusWord(sw)}", "INFO")
                
                if (parseStatusWord(sw) == "Success") {
                    return performGpoAndRecordReading(aid, aidResponse)
                }
            }
        } catch (e: Exception) {
            onLogEntry("AID selection error: ${e.message}", "ERROR")
        }
        
        return false
    }

    private suspend fun performGpoAndRecordReading(aid: ByteArray, selectResponse: ByteArray): Boolean {
        try {
            val pdolData = extractPdolFromResponse(selectResponse)
            val gpoCommand = byteArrayOf(0x80.toByte(), 0xA8.toByte(), 0x00, 0x00, pdolData.size.toByte()) + pdolData + byteArrayOf(0x00)
            onLogEntry("GPO Command: ${gpoCommand.joinToString(" ") { "%02X".format(it) }}", "TX")
            
            val gpoResponse = nfcAdapterManager.sendApduCommand(gpoCommand)
            if (gpoResponse != null) {
                val sw = gpoResponse.copyOfRange(gpoResponse.size - 2, gpoResponse.size)
                onLogEntry("GPO Response: ${gpoResponse.joinToString(" ") { "%02X".format(it) }}", "RX")
                onLogEntry("Status: ${parseStatusWord(sw)}", "INFO")
                
                if (parseStatusWord(sw) == "Success") {
                    val cardData = parseEmvCardData(aid, selectResponse, gpoResponse)
                    
                    onLogEntry("Card Analysis Complete:", "SUCCESS")
                    onLogEntry("Vendor: ${cardData.cardVendor}", "INFO")
                    cardData.primaryAccountNumber?.let { 
                        onLogEntry("PAN: ${it.take(6)}****${it.takeLast(4)}", "INFO") 
                    }
                    cardData.cardholderName?.let { onLogEntry("Name: $it", "INFO") }
                    cardData.applicationExpirationDate?.let { onLogEntry("Expiry: $it", "INFO") }
                    cardData.applicationLabel?.let { onLogEntry("Label: $it", "INFO") }
                    
                    return true
                }
            }
            
        } catch (e: Exception) {
            onLogEntry("GPO/Record reading error: ${e.message}", "ERROR")
        }
        
        return false
    }

    private fun extractPdolFromResponse(response: ByteArray): ByteArray {
        val pdol = extractTlvValue(response, 0x9F38)
        return if (pdol != null && pdol.isNotEmpty()) {
            val pdolData = buildPdolData(pdol)
            byteArrayOf(0x83.toByte(), pdolData.size.toByte()) + pdolData
        } else {
            byteArrayOf(0x83.toByte(), 0x00)
        }
    }

    private fun buildPdolData(pdol: ByteArray): ByteArray {
        val pdolData = mutableListOf<Byte>()
        var i = 0
        
        while (i < pdol.size - 1) {
            val tag = pdol[i].toInt() and 0xFF
            val length = pdol[i + 1].toInt() and 0xFF
            
            val data = when (tag) {
                0x9F, 0x37 -> ByteArray(length) { 0x12 }  // Unpredictable Number
                0x9A -> byteArrayOf(0x25, 0x09, 0x30)     // Transaction Date
                0x9C -> byteArrayOf(0x00)                  // Transaction Type
                0x9F, 0x02 -> ByteArray(length) { 0x00 }  // Amount
                0x5F, 0x2A -> byteArrayOf(0x09, 0x78)     // Currency Code
                else -> ByteArray(length) { 0x00 }
            }
            
            pdolData.addAll(data.take(length))
            i += 2
        }
        
        return pdolData.toByteArray()
    }

    private fun parseEmvCardData(aid: ByteArray, selectResponse: ByteArray, gpoResponse: ByteArray): EmvCardData {
        val vendor = parseEmvAid(aid)
        
        return EmvCardData(
            applicationIdentifier = aid,
            cardVendor = vendor,
            readTimestamp = Date(),
            applicationInterchangeProfile = extractTlvValue(gpoResponse, 0x82),
            applicationFileLocator = extractTlvValue(gpoResponse, 0x94),
            primaryAccountNumber = extractPanFromResponse(selectResponse),
            applicationLabel = extractApplicationLabel(selectResponse),
            applicationExpirationDate = extractExpiryDate(selectResponse),
            cardholderName = extractCardholderName(selectResponse),
            track2EquivalentData = extractTlvValue(selectResponse, 0x57),
            applicationVersionNumber = extractTlvValue(selectResponse, 0x9F08),
            applicationUsageControl = extractTlvValue(selectResponse, 0x9F07),
            processingDataObjectList = extractTlvValue(selectResponse, 0x9F38),
            cardRiskManagementDOL = extractTlvValue(selectResponse, 0x8C),
            issuerAuthenticationDOL = extractTlvValue(selectResponse, 0x8D)
        )
    }

    private fun extractTlvValue(data: ByteArray, tag: Int): ByteArray? {
        var i = 0
        while (i < data.size - 3) {
            val currentTag = data[i].toInt() and 0xFF
            if (currentTag == tag) {
                val length = data[i + 1].toInt() and 0xFF
                if (i + 2 + length <= data.size) {
                    return data.copyOfRange(i + 2, i + 2 + length)
                }
            }
            i++
        }
        return null
    }

    private fun extractPanFromResponse(response: ByteArray): String? {
        val panData = extractTlvValue(response, 0x5A)
        return panData?.joinToString("") { "%02X".format(it) }?.replace("F", "")
    }

    private fun extractApplicationLabel(response: ByteArray): String? {
        val labelData = extractTlvValue(response, 0x50)
        return labelData?.let { String(it, Charsets.UTF_8) }
    }

    private fun extractExpiryDate(response: ByteArray): String? {
        val expiryData = extractTlvValue(response, 0x5F24)
        return expiryData?.joinToString("") { "%02X".format(it) }
    }

    private fun extractCardholderName(response: ByteArray): String? {
        val nameData = extractTlvValue(response, 0x5F20)
        return nameData?.let { String(it, Charsets.UTF_8).trim() }
    }

    private fun parseEmvAid(aid: ByteArray): CardVendor {
        val aidHex = aid.joinToString("") { "%02X".format(it) }
        return when {
            aidHex.startsWith("A0000000031010") -> CardVendor.VISA
            aidHex.startsWith("A0000000041010") -> CardVendor.MASTERCARD
            aidHex.startsWith("A000000025") -> CardVendor.AMERICAN_EXPRESS
            aidHex.startsWith("A0000001523010") -> CardVendor.DISCOVER
            aidHex.startsWith("A0000000651010") -> CardVendor.JCB
            aidHex.startsWith("A0000000980840") -> CardVendor.VISA
            aidHex.startsWith("A0000000421010") -> CardVendor.CB
            aidHex.startsWith("A0000001410001") -> CardVendor.BANCOMAT
            aidHex.startsWith("A0000002771010") -> CardVendor.INTERAC
            aidHex.startsWith("A0000005241010") -> CardVendor.RUPAY
            else -> CardVendor.UNKNOWN
        }
    }

    private fun parseStatusWord(sw: ByteArray): String {
        if (sw.size < 2) return "Unknown SW"
        val statusWord = ((sw[0].toInt() and 0xFF) shl 8) or (sw[1].toInt() and 0xFF)
        return when (statusWord) {
            0x9000 -> "Success"
            0x6283 -> "Selected file deactivated"
            0x6300 -> "Authentication failed"
            0x6700 -> "Wrong length"
            0x6982 -> "Security status not satisfied"
            0x6985 -> "Conditions not satisfied"
            0x6A82 -> "File not found"
            0x6A83 -> "Record not found"
            0x6A86 -> "Incorrect parameters P1-P2"
            0x6A87 -> "Lc inconsistent with P1-P2"
            0x6A88 -> "Referenced data not found"
            else -> String.format("SW: %04X", statusWord)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun cardReadingScreen(nfcAdapterManager: NfcAdapterManager, permissionManager: PermissionManager) {
    var selectedDevice by remember { mutableStateOf(NfcDevice.NONE) }
    var deviceExpanded by remember { mutableStateOf(false) }
    var deviceStatus by remember { mutableStateOf("No Device Selected") }
    var apduLog by remember { mutableStateOf<List<String>>(emptyList()) }
    var isReading by remember { mutableStateOf(false) }
    var isConnecting by remember { mutableStateOf(false) }
    var currentCard by remember { mutableStateOf<EmvCardData?>(null) }
    var emvWorkflowStatus by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val connectionStatus by nfcAdapterManager.connectionStatus.collectAsState()
    val availableAdapters by nfcAdapterManager.availableAdapters.collectAsState()

    val availableDevices = remember {
        listOf(NfcDevice.INTERNAL_NFC, NfcDevice.PN532_BLUETOOTH, NfcDevice.PN532_USB)
    }

    val emvProcessor = remember {
        EmvWorkflowProcessor(nfcAdapterManager) { entry, direction ->
            val timestamp = System.currentTimeMillis() % 100000
            val colorPrefix = when (direction) {
                "TX" -> "TX:"
                "RX" -> "RX:" 
                "ERROR" -> "ERROR:"
                "SUCCESS" -> "SUCCESS:"
                else -> "INFO:"
            }
            apduLog = apduLog + "[$timestamp] $colorPrefix $entry"
        }
    }

    fun startEmvCardReading() {
        scope.launch {
            isReading = true
            
            try {
                when (selectedDevice) {
                    NfcDevice.INTERNAL_NFC -> {
                        emvWorkflowStatus = "Waiting for NFC card"
                        emvProcessor.run { 
                            val timestamp = System.currentTimeMillis() % 100000
                            apduLog = apduLog + "[$timestamp] INFO: NFC reader mode enabled - present card"
                        }
                    }
                    NfcDevice.PN532_BLUETOOTH -> {
                        emvWorkflowStatus = "Scanning for cards via PN532"
                        val success = emvProcessor.performPn532EmvWorkflow()
                        emvWorkflowStatus = if (success) "EMV extraction complete" else "Workflow failed"
                    }
                    else -> {
                        val timestamp = System.currentTimeMillis() % 100000
                        apduLog = apduLog + "[$timestamp] ERROR: Invalid device selected"
                        isReading = false
                    }
                }
            } catch (e: Exception) {
                val timestamp = System.currentTimeMillis() % 100000
                apduLog = apduLog + "[$timestamp] ERROR: EMV workflow error: ${e.message}"
                emvWorkflowStatus = "Error: ${e.message}"
                isReading = false
            }
        }
    }

    LaunchedEffect(selectedDevice) {
        when (selectedDevice) {
            NfcDevice.INTERNAL_NFC -> {
                scope.launch {
                    val timestamp = System.currentTimeMillis() % 100000
                    apduLog = apduLog + "[$timestamp] INFO: Initializing Android NFC adapter"
                    try {
                        val connected = nfcAdapterManager.connectToAdapter("internal_nfc")
                        if (connected) {
                            deviceStatus = "Android Internal NFC - Ready"
                            apduLog = apduLog + "[$timestamp] INFO: NFC adapter connected successfully"
                        } else {
                            deviceStatus = "Android Internal NFC - Disabled"
                            apduLog = apduLog + "[$timestamp] INFO: NFC connection failed"
                        }
                    } catch (e: Exception) {
                        deviceStatus = "Android Internal NFC - Error"
                        apduLog = apduLog + "[$timestamp] ERROR: NFC error: ${e.message}"
                    }
                }
            }
            NfcDevice.PN532_BLUETOOTH -> {
                scope.launch {
                    isConnecting = true
                    deviceStatus = "Scanning for PN532 devices"
                    val timestamp = System.currentTimeMillis() % 100000
                    apduLog = apduLog + "[$timestamp] INFO: Starting PN532 Bluetooth connection"
                    
                    try {
                        val connected = nfcAdapterManager.connectToAdapter("pn532_bluetooth")
                        if (connected) {
                            deviceStatus = "PN532 Bluetooth - Connected"
                            apduLog = apduLog + "[$timestamp] SUCCESS: PN532 connected successfully"
                        } else {
                            deviceStatus = "PN532 Bluetooth - Connection failed"
                            apduLog = apduLog + "[$timestamp] ERROR: PN532 connection failed"
                        }
                    } catch (e: Exception) {
                        deviceStatus = "PN532 Bluetooth - Error"
                        apduLog = apduLog + "[$timestamp] ERROR: PN532 error: ${e.message}"
                    } finally {
                        isConnecting = false
                    }
                }
            }
            NfcDevice.PN532_USB -> {
                deviceStatus = "PN532 USB - Not implemented"
                val timestamp = System.currentTimeMillis() % 100000
                apduLog = apduLog + "[$timestamp] INFO: USB not yet implemented"
            }
            else -> {
                deviceStatus = "No Device Selected"
            }
        }
    }

    val listState = rememberLazyListState()

    LaunchedEffect(apduLog.size) {
        if (apduLog.isNotEmpty()) {
            listState.animateScrollToItem(apduLog.size - 1)
        }
    }

    Column(
            modifier = Modifier.fillMaxSize().background(Color.Black).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
        ) {
            Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                        text = "Device Selection",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                )

                ExposedDropdownMenuBox(
                        expanded = deviceExpanded,
                        onExpandedChange = { deviceExpanded = it }
                ) {
                    OutlinedTextField(
                            value = selectedDevice.displayName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("NFC Adapter", color = Color.Gray) },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = deviceExpanded)
                            },
                            colors =
                                    OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = Color.White,
                                            unfocusedTextColor = Color.White,
                                            focusedBorderColor = Color(0xFF4CAF50),
                                            unfocusedBorderColor = Color.Gray
                                    ),
                            modifier = Modifier.fillMaxWidth().menuAnchor()
                    )

                    ExposedDropdownMenu(
                            expanded = deviceExpanded,
                            onDismissRequest = { deviceExpanded = false },
                            modifier = Modifier.background(Color(0xFF2A2A2A))
                    ) {
                        availableDevices.forEach { device ->
                            DropdownMenuItem(
                                    text = { Text(text = device.displayName, color = Color.White) },
                                    onClick = {
                                        selectedDevice = device
                                        deviceExpanded = false
                                    },
                                    colors = MenuDefaults.itemColors(textColor = Color.White)
                            )
                        }
                    }
                }

                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (isConnecting) {
                        CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = Color(0xFF4CAF50),
                                strokeWidth = 2.dp
                        )
                    }

                    val deviceParts = deviceStatus.split(" - ")
                    if (deviceParts.size >= 2) {
                        Text(
                                text = deviceParts[0],
                                fontSize = 14.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                        )
                        Text(text = " - ", fontSize = 14.sp, color = Color.Gray)
                        Text(
                                text = deviceParts[1],
                                fontSize = 14.sp,
                                color =
                                        when {
                                            deviceParts[1].contains("Ready") ||
                                                    deviceParts[1].contains("Connected") ->
                                                    Color(0xFF4CAF50)
                                            deviceParts[1].contains("failed") ||
                                                    deviceParts[1].contains("Error") ||
                                                    deviceParts[1].contains("not found") ->
                                                    Color.Red
                                            isConnecting -> Color.Yellow
                                            else -> Color.Gray
                                        },
                                fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                                text = deviceStatus,
                                fontSize = 14.sp,
                                color =
                                        when {
                                            deviceStatus.contains("Connected") ||
                                                    deviceStatus.contains("Ready") ->
                                                    Color(0xFF4CAF50)
                                            deviceStatus.contains("failed") ||
                                                    deviceStatus.contains("Error") ||
                                                    deviceStatus.contains("not found") -> Color.Red
                                            isConnecting -> Color.Yellow
                                            else -> Color.Gray
                                        },
                                fontWeight = FontWeight.Medium
                        )
                    }
                }

                if (emvWorkflowStatus.isNotEmpty()) {
                    Text(
                            text = "EMV Status: $emvWorkflowStatus",
                            fontSize = 12.sp,
                            color = Color.Cyan,
                            fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                    onClick = {
                        if (selectedDevice != NfcDevice.NONE &&
                                        (deviceStatus.contains("Ready") ||
                                                deviceStatus.contains("Connected"))
                        ) {
                            if (isReading) {
                                isReading = false
                                emvWorkflowStatus = ""
                                val timestamp = System.currentTimeMillis() % 100000
                                apduLog = apduLog + "[$timestamp] INFO: Card reading session stopped"
                            } else {
                                startEmvCardReading()
                            }
                        }
                    },
                    enabled =
                            selectedDevice != NfcDevice.NONE &&
                                    (deviceStatus.contains("Ready") ||
                                            deviceStatus.contains("Connected")) &&
                                    !isConnecting,
                    colors =
                            ButtonDefaults.buttonColors(
                                    containerColor =
                                            if (isReading) Color.Red else Color(0xFF4CAF50),
                                    contentColor = Color.Black,
                                    disabledContainerColor = Color.Gray
                            ),
                    modifier = Modifier.weight(1f)
            ) {
                Icon(
                        imageVector =
                                if (isReading) Icons.Default.Stop else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = if (isReading) "STOP" else "START", fontWeight = FontWeight.Bold)
            }

            Button(
                    onClick = { 
                        apduLog = emptyList()
                        currentCard = null
                        emvWorkflowStatus = ""
                    },
                    colors =
                            ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF333333),
                                    contentColor = Color.White
                            ),
                    modifier = Modifier.weight(1f)
            ) {
                Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("CLEAR", fontWeight = FontWeight.Bold)
            }
        }

        Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                        text = "Live EMV APDU Traffic & Analysis",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.padding(bottom = 12.dp)
                )

                if (apduLog.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                                text =
                                        when {
                                            selectedDevice == NfcDevice.NONE ->
                                                    "Select a device to see EMV traffic"
                                            isConnecting -> "Connecting to device"
                                            !deviceStatus.contains("Ready") &&
                                                    !deviceStatus.contains("Connected") ->
                                                    "Device not ready for communication"
                                            !isReading -> "Press START to begin EMV card reading"
                                            else -> "Present card to reader"
                                        },
                                color = Color.Gray,
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp
                        )
                    }
                } else {
                    LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(apduLog) { logEntry ->
                            Text(
                                    text = logEntry,
                                    fontSize = 12.sp,
                                    color =
                                            when {
                                                logEntry.contains("TX:") -> Color(0xFFFFAA00)
                                                logEntry.contains("RX:") -> Color(0xFF00AAFF)
                                                logEntry.contains("ERROR:") -> Color.Red
                                                logEntry.contains("SUCCESS:") -> Color(0xFF4CAF50)
                                                logEntry.contains("Extracted AID:") ||
                                                logEntry.contains("Card Analysis") -> Color.Cyan
                                                logEntry.contains("PAN:") ||
                                                logEntry.contains("Name:") ||
                                                logEntry.contains("Expiry:") ||
                                                logEntry.contains("Label:") ||
                                                logEntry.contains("Vendor:") -> Color(0xFFFFD700)
                                                logEntry.contains("Status:") -> Color(0xFF90EE90)
                                                else -> Color.White
                                            },
                                    modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
>>>>>>> 52c0655 (🎯 Complete Priority 1-3: Production-grade CardReadingScreen with EmvWorkflowProcessor)
