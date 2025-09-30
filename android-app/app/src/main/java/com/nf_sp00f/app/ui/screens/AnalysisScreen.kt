package com.nf_sp00f.app.ui.screens
import com.nf_sp00f.app.data.*
import com.nf_sp00f.app.R

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
@Composable
fun analysisScreen() {
        var selectedAnalysisTab by remember { mutableStateOf(0) }
        var selectedCard by remember { mutableStateOf("Select Card...") }
        var tlvInput by remember { mutableStateOf("") }
        var fuzzerRunning by remember { mutableStateOf(false) }
        // Sample analysis data
        val recentAnalysis =
                listOf(
                        AnalysisResult(
                                "VISA Track2 Analysis",
                                "4154****3556",
                                "PASSED",
                                95,
                                "2m ago"
                        ),
                                "Cryptogram Validation",
                                "5555****4444",
                                "WARNING",
                                72,
                                "5m ago"
                        AnalysisResult("TTQ Workflow Test", "3782****1007", "FAILED", 34, "1h ago"),
                        AnalysisResult("APDU Flow Analysis", "4000****0002", "PASSED", 88, "3h ago")
                )
        val analysisTools =
                        AnalysisTool(
                                "TLV Browser",
                                "Interactive EMV tag exploration",
                                Icons.Default.Code,
                                true
                                "Cryptogram Lab",
                                "ARQC/TC/AAC validation suite",
                                Icons.Default.Security,
                                "Workflow Analyzer",
                                "TTQ/TVR/TSI deep analysis",
                                Icons.Default.Timeline,
                                false
                                "APDU Dissector",
                                "Transaction flow inspection",
                                Icons.Default.Analytics,
                                "Fuzzer Engine",
                                "Attack vector generation",
                                Icons.Default.BugReport,
                                "BER-TLV Parser",
                                "Raw data decoding utilities",
                                Icons.Default.DataObject,
                        )
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
                // Header Card
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
                                Column(
                                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                        Text(
                                                "EMV ANALYSIS LAB",
                                                style =
                                                        MaterialTheme.typography.headlineLarge.copy(
                                                                fontWeight = FontWeight.Bold
                                                        ),
                                                color = Color(0xFF4CAF50),
                                                textAlign = TextAlign.Center
                                                "Security Research & Forensic Tools",
                                                        MaterialTheme.typography.titleMedium.copy(
                                                                textDecoration =
                                                                        androidx.compose.ui.text
                                                                                .style
                                                                                .TextDecoration
                                                                                .Underline
                                                color = Color(0xFFFFFFFF),
                                }
                        }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Analysis Tools Grid
                Text(
                        "Analysis Tools",
                        style =
                                MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold
                                ),
                        color = Color(0xFF4CAF50),
                        modifier = Modifier.padding(bottom = 8.dp)
                LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.height(200.dp)
                        items(analysisTools) { tool ->
                                AnalysisToolCard(
                                        tool = tool,
                                        onClick = { /* Handle tool selection */}
                // Tab Navigation for Analysis Sections
                ScrollableTabRow(
                        selectedTabIndex = selectedAnalysisTab,
                        containerColor = Color.Transparent,
                        contentColor = Color(0xFF4CAF50),
                        indicator = { tabPositions ->
                                TabRowDefaults.Indicator(
                                        modifier =
                                                Modifier.tabIndicatorOffset(
                                                        tabPositions[selectedAnalysisTab]
                                                ),
                                        color = Color(0xFF4CAF50)
                        val tabs = listOf("TLV Parser", "Cryptogram", "APDU Flow", "Live Monitor")
                        tabs.forEachIndexed { index, title ->
                                Tab(
                                        selected = selectedAnalysisTab == index,
                                        onClick = { selectedAnalysisTab = index },
                                        text = {
                                                Text(
                                                        title,
                                                        color =
                                                                if (selectedAnalysisTab == index)
                                                                        Color(0xFF4CAF50)
                                                                else Color(0xFFAAAAAA)
                                                )
                                        }
                // Tab Content
                when (selectedAnalysisTab) {
                        0 -> TlvParserContent(tlvInput) { tlvInput = it }
                        1 -> CryptogramAnalysisContent()
                        2 -> ApduFlowContent(recentAnalysis)
                        3 -> LiveMonitorContent(fuzzerRunning) { fuzzerRunning = it }
        }
}
fun AnalysisToolCard(tool: AnalysisTool, onClick: () -> Unit) {
        Card(
                        Modifier.fillMaxWidth().height(80.dp).clickable(enabled = tool.enabled) {
                                onClick()
                        },
                colors =
                        CardDefaults.cardColors(
                                containerColor =
                                        if (tool.enabled) Color(0xFF121717) else Color(0xFF0A0A0A)
                shape = RoundedCornerShape(8.dp),
                elevation =
                        CardDefaults.cardElevation(
                                defaultElevation = if (tool.enabled) 4.dp else 1.dp
                Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                                painter = painterResource(id = R.drawable.nfspoof_logo),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                                alpha = 0.05f
                        Column(
                                modifier = Modifier.padding(8.dp).fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                                Icon(
                                        tool.icon,
                                        tint =
                                                if (tool.enabled) Color(0xFF4CAF50)
                                                else Color(0xFF666666),
                                        modifier = Modifier.size(24.dp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                        tool.title,
                                        style =
                                                MaterialTheme.typography.bodySmall.copy(
                                                        fontWeight = FontWeight.Bold
                                        color =
                                                if (tool.enabled) Color.White
                                        textAlign = TextAlign.Center,
                                        maxLines = 1
                                if (!tool.enabled) {
                                                "Coming Soon",
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color(0xFF666666)
fun TlvParserContent(tlvInput: String, onTlvInputChange: (String) -> Unit) {
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF121717)),
                shape = RoundedCornerShape(8.dp)
                Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                                style =
                                        MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold
                                        ),
                                color = Color(0xFF4CAF50)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                                value = tlvInput,
                                onValueChange = onTlvInputChange,
                                label = {
                                        Text("Enter TLV hex data...", color = Color(0xFF4CAF50))
                                },
                                placeholder = {
                                                "e.g., 5A084154904674973556",
                                                color = Color(0xFFAAAAAA)
                                modifier = Modifier.fillMaxWidth(),
                                colors =
                                        OutlinedTextFieldDefaults.colors(
                                                focusedBorderColor = Color(0xFF4CAF50),
                                                unfocusedBorderColor =
                                                        Color(0xFF4CAF50).copy(alpha = 0.5f),
                                                focusedTextColor = Color.White,
                                                unfocusedTextColor = Color.White
                                singleLine = false,
                                minLines = 3
                        Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                Button(
                                        onClick = { /* Parse TLV */},
                                        colors =
                                                ButtonDefaults.buttonColors(
                                                        containerColor = Color(0xFF4CAF50)
                                        modifier = Modifier.weight(1f)
                                        Icon(Icons.Default.PlayArrow, contentDescription = null)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Parse", color = Color.Black)
                                OutlinedButton(
                                        onClick = { onTlvInputChange("") },
                                                ButtonDefaults.outlinedButtonColors(
                                                        contentColor = Color(0xFF4CAF50)
                                        border = BorderStroke(1.dp, Color(0xFF4CAF50))
                                ) { Text("Clear") }
                        // Sample parsed output
                        if (tlvInput.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                        "Parsed Tags:",
                                                MaterialTheme.typography.bodyMedium.copy(
                                Card(
                                        modifier = Modifier.fillMaxWidth(),
                                                CardDefaults.cardColors(
                                                        containerColor = Color(0xFF0A0A0A)
                                        Column(modifier = Modifier.padding(8.dp)) {
                                                        "5A (PAN): 4154904674973556",
                                                        style =
                                                                MaterialTheme.typography.bodySmall
                                                                        .copy(
                                                                                fontFamily =
                                                                                        FontFamily
                                                                                                .Monospace
                                                                        ),
                                                        color = Color(0xFFAAAAAA)
                                                        "57 (Track2): 4154904674973556D29...",
                                                        "5F20 (Cardholder): JOHN DOE",
fun CryptogramAnalysisContent() {
                                "Cryptogram Analysis Lab",
                        Spacer(modifier = Modifier.height(12.dp))
                                CryptogramMetricCard(
                                        "ARQC",
                                        "12",
                                        Color(0xFF2196F3),
                                        Modifier.weight(1f)
                                        "TC",
                                        "8",
                                        Color(0xFF4CAF50),
                                        "AAC",
                                        "3",
                                        Color(0xFFFF5722),
                                "Recent Cryptogram Analysis:",
                                        MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White
                        LazyColumn(
                                modifier = Modifier.height(120.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                                items(5) { index ->
                                        Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                        ) {
                                                        "9F26: A1B2C3D4E5F6\${index}78",
                                                        if (index % 3 == 0) "ARQC"
                                                        else if (index % 3 == 1) "TC" else "AAC",
                                                                MaterialTheme.typography.labelSmall
                                                                                fontWeight =
                                                                                        FontWeight
                                                                                                .Bold
                                                                if (index % 3 == 0)
                                                                        Color(0xFF2196F3)
                                                                else if (index % 3 == 1)
                                                                else Color(0xFFFF5722)
fun ApduFlowContent(recentAnalysis: List<AnalysisResult>) {
                                "APDU Flow Analysis",
                                modifier = Modifier.height(200.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) { items(recentAnalysis) { analysis -> AnalysisResultCard(analysis) } }
fun LiveMonitorContent(fuzzerRunning: Boolean, onFuzzerToggle: (Boolean) -> Unit) {
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                                        "Live Security Monitor",
                                                MaterialTheme.typography.titleMedium.copy(
                                Switch(
                                        checked = fuzzerRunning,
                                        onCheckedChange = onFuzzerToggle,
                                                SwitchDefaults.colors(
                                                        checkedThumbColor = Color(0xFF4CAF50),
                                                        checkedTrackColor =
                                                                Color(0xFF4CAF50).copy(alpha = 0.5f)
                        if (fuzzerRunning) {
                                Column {
                                                "🔍 Scanning for vulnerabilities...",
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = Color(0xFF4CAF50)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        LinearProgressIndicator(
                                                trackColor = Color(0xFF4CAF50).copy(alpha = 0.3f)
                                                "• Testing PPSE poisoning vectors\n• Analyzing AIP bypass methods\n• Fuzzing cryptogram validation\n• Monitoring CVM bypass attempts",
                                                style = MaterialTheme.typography.bodySmall,
                        } else {
                                        "Enable live monitoring to start real-time security analysis",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color(0xFFAAAAAA)
fun CryptogramMetricCard(type: String, count: String, color: Color, modifier: Modifier = Modifier) {
                modifier = modifier,
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0A0A0A)),
                Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                                count,
                                        MaterialTheme.typography.headlineSmall.copy(
                                color = color
                                type,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFAAAAAA)
fun AnalysisResultCard(analysis: AnalysisResult) {
                Row(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                        Column(modifier = Modifier.weight(1f)) {
                                        analysis.title,
                                        color = Color.White
                                        analysis.cardNumber,
                                                        fontFamily = FontFamily.Monospace
                        Column(horizontalAlignment = Alignment.End) {
                                val statusColor =
                                        when (analysis.status) {
                                                "PASSED" -> Color(0xFF4CAF50)
                                                "WARNING" -> Color(0xFFFF9800)
                                                "FAILED" -> Color(0xFFFF5722)
                                                else -> Color(0xFFAAAAAA)
                                        analysis.status,
                                                MaterialTheme.typography.labelSmall.copy(
                                        color = statusColor
                                        "\${analysis.score}%",
                                        style = MaterialTheme.typography.bodySmall,
                                        analysis.timestamp,
                                        style = MaterialTheme.typography.labelSmall,
