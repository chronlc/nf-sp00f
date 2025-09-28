package com.example.nfsp00f.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nfsp00f.data.EmvCardData

/**
 * Analysis Fragment - EMV Security Analysis and Vulnerability Assessment Professional forensic
 * interface with comprehensive EMV security evaluation
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalysisFragment() {

  var selectedCard by remember { mutableStateOf<EmvCardData?>(null) }
  var analysisResults by remember { mutableStateOf<AnalysisResults?>(null) }
  var isAnalyzing by remember { mutableStateOf(false) }
  var showDetailedReport by remember { mutableStateOf(false) }

  val sampleCards = remember { generateSampleCardsForAnalysis() }

  Column(
          modifier = Modifier.fillMaxSize().padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {

    // Header
    Text(
            text = "🔍 EMV Security Analysis",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
    )

    // Card Selection
    CardSelectionCard(
            availableCards = sampleCards,
            selectedCard = selectedCard,
            onCardSelected = { card ->
              selectedCard = card
              analysisResults = null
            }
    )

    // Analysis Control
    if (selectedCard != null) {
      AnalysisControlCard(
              isAnalyzing = isAnalyzing,
              onStartAnalysis = {
                isAnalyzing = true
                // Simulate analysis process
                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).launch {
                  kotlinx.coroutines.delay(3000)
                  analysisResults = performSecurityAnalysis(selectedCard!!)
                  isAnalyzing = false
                }
              }
      )
    }

    // Analysis Results
    analysisResults?.let { results ->
      if (showDetailedReport) {
        DetailedAnalysisReport(results = results, onCloseReport = { showDetailedReport = false })
      } else {
        AnalysisResultsCard(results = results, onViewDetailedReport = { showDetailedReport = true })
      }
    }

    // Security Recommendations
    if (analysisResults != null && !showDetailedReport) {
      SecurityRecommendationsCard(analysisResults!!)
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardSelectionCard(
        availableCards: List<EmvCardData>,
        selectedCard: EmvCardData?,
        onCardSelected: (EmvCardData) -> Unit
) {
  Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(modifier = Modifier.padding(20.dp)) {
      Text(
              text = "📋 Select Card for Analysis",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(16.dp))

      availableCards.forEach { card ->
        CardSelectionItem(
                card = card,
                isSelected = selectedCard == card,
                onSelected = { onCardSelected(card) }
        )
        if (card != availableCards.last()) {
          Spacer(modifier = Modifier.height(8.dp))
        }
      }

      if (availableCards.isEmpty()) {
        Text(
                text = "No cards available for analysis. Read cards first.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardSelectionItem(card: EmvCardData, isSelected: Boolean, onSelected: () -> Unit) {
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
            verticalAlignment = Alignment.CenterVertically
    ) {
      RadioButton(selected = isSelected, onClick = onSelected)

      Spacer(modifier = Modifier.width(12.dp))

      Column {
        Text(
                text = formatPanForDisplay(card.pan),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color =
                        if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSurface
        )
        Text(
                text = "${card.applicationLabel} - ${card.cardholderName}",
                style = MaterialTheme.typography.bodyMedium,
                color =
                        if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnalysisControlCard(isAnalyzing: Boolean, onStartAnalysis: () -> Unit) {
  Card(
          modifier = Modifier.fillMaxWidth(),
          colors =
                  CardDefaults.cardColors(
                          containerColor =
                                  if (isAnalyzing) Color(0xFF1B1B1B)
                                  else MaterialTheme.colorScheme.surface
                  )
  ) {
    Column(modifier = Modifier.padding(20.dp)) {
      if (isAnalyzing) {
        Text(
                text = "🔄 ANALYSIS IN PROGRESS",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.Yellow
        )

        Spacer(modifier = Modifier.height(16.dp))

        LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = Color.Yellow)

        Spacer(modifier = Modifier.height(12.dp))

        val analysisSteps =
                listOf(
                        "Analyzing EMV protocol compliance...",
                        "Checking security features...",
                        "Evaluating cryptographic strength...",
                        "Assessing attack surface...",
                        "Generating recommendations..."
                )

        var currentStep by remember { mutableStateOf(0) }
        LaunchedEffect(isAnalyzing) {
          while (isAnalyzing && currentStep < analysisSteps.size - 1) {
            kotlinx.coroutines.delay(600)
            currentStep = (currentStep + 1) % analysisSteps.size
          }
        }

        Text(
                text = analysisSteps[currentStep],
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
        )
      } else {
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
                    text = "⚡ Security Analysis Ready",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
            )
            Text(
                    text = "Comprehensive EMV security evaluation",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Button(
                  onClick = onStartAnalysis,
                  colors =
                          ButtonDefaults.buttonColors(
                                  containerColor = MaterialTheme.colorScheme.primary
                          )
          ) {
            Icon(Icons.Default.Security, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("START ANALYSIS", fontWeight = FontWeight.Bold)
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnalysisResultsCard(results: AnalysisResults, onViewDetailedReport: () -> Unit) {
  Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(modifier = Modifier.padding(20.dp)) {
      Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
                text = "🛡️ Security Analysis Results",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
        )

        TextButton(onClick = onViewDetailedReport) {
          Text("Detailed Report")
          Icon(Icons.Default.ArrowForward, contentDescription = null)
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Overall Security Score
      Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
        SecurityScoreItem(
                label = "Overall Score",
                score = results.overallScore,
                maxScore = 100,
                color = getScoreColor(results.overallScore)
        )
        SecurityScoreItem(
                label = "Vulnerabilities",
                score = results.vulnerabilities.size,
                maxScore = 10,
                color = Color.Red,
                suffix = " found"
        )
        SecurityScoreItem(
                label = "Attack Surface",
                score = results.attackSurfaceScore,
                maxScore = 100,
                color = Color.Orange
        )
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Quick Vulnerability Summary
      Text(
              text = "Critical Issues Found:",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(8.dp))

      results.vulnerabilities.take(3).forEach { vulnerability ->
        VulnerabilityItem(vulnerability = vulnerability, compact = true)
      }

      if (results.vulnerabilities.size > 3) {
        Text(
                text = "... and ${results.vulnerabilities.size - 3} more issues",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
  }
}

@Composable
private fun SecurityScoreItem(
        label: String,
        score: Int,
        maxScore: Int,
        color: Color,
        suffix: String = ""
) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
            text = "$score$suffix",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = color,
            fontSize = 28.sp
    )
    Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    if (suffix.isEmpty()) {
      Text(
              text = "/$maxScore",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@Composable
private fun VulnerabilityItem(vulnerability: SecurityVulnerability, compact: Boolean = false) {
  Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
    Box(
            modifier =
                    Modifier.size(8.dp)
                            .clip(CircleShape)
                            .background(getSeverityColor(vulnerability.severity))
    )

    Spacer(modifier = Modifier.width(12.dp))

    Column(modifier = Modifier.weight(1f)) {
      Text(
              text = vulnerability.title,
              style = MaterialTheme.typography.bodyMedium,
              fontWeight = FontWeight.Medium,
              color = MaterialTheme.colorScheme.onSurface
      )
      if (!compact) {
        Text(
                text = vulnerability.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    Text(
            text = vulnerability.severity.name,
            style = MaterialTheme.typography.labelSmall,
            color = getSeverityColor(vulnerability.severity),
            fontWeight = FontWeight.Bold
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailedAnalysisReport(results: AnalysisResults, onCloseReport: () -> Unit) {
  Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = Color(0xFF0D1117))
  ) {
    Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
      // Header
      Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
                text = "📋 Detailed Security Report",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
        )

        IconButton(onClick = onCloseReport) {
          Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Analysis Summary
      Text(
              text = "ANALYSIS SUMMARY",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = Color.Green
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
              text =
                      "Timestamp: ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(java.util.Date())}",
              style = MaterialTheme.typography.bodySmall,
              fontFamily = FontFamily.Monospace,
              color = Color.Gray
      )

      Text(
              text = "Analysis Duration: ${results.analysisTimeMs}ms",
              style = MaterialTheme.typography.bodySmall,
              fontFamily = FontFamily.Monospace,
              color = Color.Gray
      )

      Text(
              text = "EMV Standard: 4.3 Compliance Check",
              style = MaterialTheme.typography.bodySmall,
              fontFamily = FontFamily.Monospace,
              color = Color.Gray
      )

      Spacer(modifier = Modifier.height(16.dp))

      // Security Scores Breakdown
      Text(
              text = "SECURITY METRICS",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = Color.Green
      )

      Spacer(modifier = Modifier.height(8.dp))

      results.securityMetrics.forEach { (metric, score) ->
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
          Text(
                  text = metric,
                  style = MaterialTheme.typography.bodyMedium,
                  fontFamily = FontFamily.Monospace,
                  color = Color.White
          )
          Text(
                  text = "${score}/100",
                  style = MaterialTheme.typography.bodyMedium,
                  fontFamily = FontFamily.Monospace,
                  color = getScoreColor(score)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Vulnerabilities Detail
      Text(
              text = "VULNERABILITIES DETECTED (${results.vulnerabilities.size})",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = Color.Red
      )

      Spacer(modifier = Modifier.height(8.dp))

      results.vulnerabilities.forEach { vulnerability ->
        VulnerabilityDetailItem(vulnerability)
        Spacer(modifier = Modifier.height(8.dp))
      }

      // Technical Details
      Spacer(modifier = Modifier.height(16.dp))

      Text(
              text = "TECHNICAL ANALYSIS",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = Color.Green
      )

      Spacer(modifier = Modifier.height(8.dp))

      results.technicalDetails.forEach { detail ->
        Text(
                text = "• $detail",
                style = MaterialTheme.typography.bodySmall,
                fontFamily = FontFamily.Monospace,
                color = Color.Gray
        )
      }
    }
  }
}

@Composable
private fun VulnerabilityDetailItem(vulnerability: SecurityVulnerability) {
  Column(
          modifier =
                  Modifier.fillMaxWidth()
                          .background(color = Color(0xFF161B22), shape = MaterialTheme.shapes.small)
                          .padding(12.dp)
  ) {
    Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
              text = vulnerability.title,
              style = MaterialTheme.typography.titleSmall,
              fontWeight = FontWeight.Bold,
              color = Color.White
      )
      Text(
              text = vulnerability.severity.name,
              style = MaterialTheme.typography.labelSmall,
              color = getSeverityColor(vulnerability.severity),
              fontWeight = FontWeight.Bold
      )
    }

    Spacer(modifier = Modifier.height(4.dp))

    Text(
            text = vulnerability.description,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
    )

    Text(
            text = "Impact: ${vulnerability.impact}",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Yellow
    )

    Text(
            text = "Recommendation: ${vulnerability.recommendation}",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Green
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SecurityRecommendationsCard(results: AnalysisResults) {
  Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(modifier = Modifier.padding(20.dp)) {
      Text(
              text = "💡 Security Recommendations",
              style = MaterialTheme.typography.titleLarge,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurface
      )

      Spacer(modifier = Modifier.height(16.dp))

      results.recommendations.forEach { recommendation ->
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
          Icon(
                  imageVector = Icons.Default.Lightbulb,
                  contentDescription = null,
                  tint = Color.Orange,
                  modifier = Modifier.size(20.dp)
          )

          Spacer(modifier = Modifier.width(12.dp))

          Text(
                  text = recommendation,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurface
          )
        }

        if (recommendation != results.recommendations.last()) {
          Spacer(modifier = Modifier.height(12.dp))
        }
      }
    }
  }
}

// Data classes and helper functions
data class AnalysisResults(
        val overallScore: Int,
        val attackSurfaceScore: Int,
        val vulnerabilities: List<SecurityVulnerability>,
        val securityMetrics: Map<String, Int>,
        val recommendations: List<String>,
        val technicalDetails: List<String>,
        val analysisTimeMs: Long
)

data class SecurityVulnerability(
        val title: String,
        val description: String,
        val severity: Severity,
        val impact: String,
        val recommendation: String
)

enum class Severity {
  LOW,
  MEDIUM,
  HIGH,
  CRITICAL
}

private fun formatPanForDisplay(pan: String): String {
  if (pan.length < 16) return pan
  return "${pan.substring(0, 4)} **** **** ${pan.takeLast(4)}"
}

private fun getScoreColor(score: Int): Color {
  return when {
    score >= 80 -> Color.Green
    score >= 60 -> Color.Yellow
    score >= 40 -> Color.Orange
    else -> Color.Red
  }
}

private fun getSeverityColor(severity: Severity): Color {
  return when (severity) {
    Severity.LOW -> Color.Green
    Severity.MEDIUM -> Color.Yellow
    Severity.HIGH -> Color.Orange
    Severity.CRITICAL -> Color.Red
  }
}

private fun performSecurityAnalysis(card: EmvCardData): AnalysisResults {
  val vulnerabilities = mutableListOf<SecurityVulnerability>()

  // Simulate vulnerability detection based on card data
  if (card.applicationInterchangeProfile.isEmpty() || card.applicationInterchangeProfile == "0000"
  ) {
    vulnerabilities.add(
            SecurityVulnerability(
                    title = "AIP Security Features Disabled",
                    description =
                            "Application Interchange Profile indicates disabled security features",
                    severity = Severity.HIGH,
                    impact = "All EMV security mechanisms bypassed",
                    recommendation = "Enable SDA/DDA and cardholder verification"
            )
    )
  }

  if (card.track2Data.isNotEmpty() && card.applicationInterchangeProfile.contains("18")) {
    vulnerabilities.add(
            SecurityVulnerability(
                    title = "Magnetic Stripe Fallback Enabled",
                    description = "Card supports magnetic stripe mode which can be exploited",
                    severity = Severity.MEDIUM,
                    impact = "Vulnerable to skimming and track data cloning",
                    recommendation = "Disable magnetic stripe fallback for EMV transactions"
            )
    )
  }

  if (card.applicationTransactionCounter.isEmpty() || card.applicationTransactionCounter == "0000"
  ) {
    vulnerabilities.add(
            SecurityVulnerability(
                    title = "Weak Transaction Counter",
                    description = "Application Transaction Counter not properly implemented",
                    severity = Severity.MEDIUM,
                    impact = "Replay attacks possible",
                    recommendation = "Implement proper ATC incrementation"
            )
    )
  }

  val overallScore = maxOf(20, 100 - (vulnerabilities.size * 15))

  return AnalysisResults(
          overallScore = overallScore,
          attackSurfaceScore = maxOf(10, 90 - (vulnerabilities.size * 20)),
          vulnerabilities = vulnerabilities,
          securityMetrics =
                  mapOf(
                          "EMV Compliance" to
                                  (100 -
                                          vulnerabilities.count {
                                            it.severity == Severity.CRITICAL
                                          } * 30),
                          "Cryptographic Strength" to 75,
                          "Protocol Security" to (90 - vulnerabilities.size * 10),
                          "Authentication Methods" to
                                  if (card.applicationInterchangeProfile.contains("10")) 80 else 40
                  ),
          recommendations =
                  listOf(
                          "Enable strong EMV authentication methods",
                          "Disable magnetic stripe fallback where possible",
                          "Implement proper cryptographic validation",
                          "Regular security audits and updates",
                          "Monitor for unusual transaction patterns"
                  ),
          technicalDetails =
                  listOf(
                          "AIP Analysis: ${card.applicationInterchangeProfile}",
                          "Available AIDs: ${card.availableAids.size}",
                          "APDU Commands Analyzed: ${card.apduLog.size}",
                          "EMV Tags Extracted: ${card.emvTags.size}",
                          "Track2 Data Present: ${if (card.track2Data.isNotEmpty()) "Yes" else "No"}"
                  ),
          analysisTimeMs = 2847
  )
}

private fun generateSampleCardsForAnalysis(): List<EmvCardData> {
  return listOf(
          EmvCardData(
                  cardUid = "ANALYSIS_VISA_001",
                  pan = "4154904674973556",
                  track2Data = "4154904674973556D2902",
                  cardholderName = "JOHN DOE",
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
                                  "82" to "5800",
                                  "94" to "08010103100201059000"
                          ),
                  availableAids = listOf("A0000000031010"),
                  apduLog = emptyList(),
                  timestampFirstSeen = System.currentTimeMillis() - 3600000,
                  timestampUpdated = System.currentTimeMillis()
          ),
          EmvCardData(
                  cardUid = "ANALYSIS_MC_001",
                  pan = "5555444433331111",
                  track2Data = "5555444433331111D2512",
                  cardholderName = "JANE SMITH",
                  expiryDate = "2512",
                  applicationLabel = "MASTERCARD",
                  applicationPreferredName = "MASTERCARD",
                  applicationInterchangeProfile = "0000", // Vulnerable: all security disabled
                  applicationFileLocator = "10010103180201059000",
                  applicationTransactionCounter = "0000", // Vulnerable: weak counter
                  unpredictableNumber = "87654321",
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
                                  "5A" to "5555444433331111",
                                  "57" to "5555444433331111D2512",
                                  "82" to "0000",
                                  "94" to "10010103180201059000"
                          ),
                  availableAids = listOf("A0000000041010"),
                  apduLog = emptyList(),
                  timestampFirstSeen = System.currentTimeMillis() - 86400000,
                  timestampUpdated = System.currentTimeMillis()
          )
  )
}
