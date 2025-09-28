package com.example.nfsp00f.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.nfsp00f.data.CardProfile
import com.example.nfsp00f.data.EmvCardData

/**
 * Card Database Fragment - EMV Card Database Management Interface Professional CRUD operations with
 * search, filter, and forensic analysis
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDatabaseFragment() {

  var searchQuery by remember { mutableStateOf("") }
  var selectedFilter by remember { mutableStateOf(DatabaseFilter.ALL) }
  var showAddDialog by remember { mutableStateOf(false) }
  var showImportDialog by remember { mutableStateOf(false) }
  var cardProfiles by remember { mutableStateOf(generateSampleCardProfiles()) }

  // Filter cards based on search and filter
  val filteredCards =
          remember(cardProfiles, searchQuery, selectedFilter) {
            cardProfiles.filter { card ->
              val matchesSearch =
                      if (searchQuery.isBlank()) true
                      else {
                        card.cardData.pan.contains(searchQuery, ignoreCase = true) ||
                                card.cardData.cardholderName.contains(
                                        searchQuery,
                                        ignoreCase = true
                                ) ||
                                card.cardData.applicationLabel.contains(
                                        searchQuery,
                                        ignoreCase = true
                                )
                      }

              val matchesFilter =
                      when (selectedFilter) {
                        DatabaseFilter.ALL -> true
                        DatabaseFilter.VISA ->
                                card.cardData.availableAids.any { it.contains("A0000000031010") }
                        DatabaseFilter.MASTERCARD ->
                                card.cardData.availableAids.any { it.contains("A0000000041010") }
                        DatabaseFilter.RECENT ->
                                (System.currentTimeMillis() - card.cardData.timestampFirstSeen) <
                                        86400000 // 24 hours
                        DatabaseFilter.FAVORITES -> false // Not implemented in sample
                      }

              matchesSearch && matchesFilter
            }
          }

  Column(
          modifier = Modifier.fillMaxSize().padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {

    // Header
    Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
                text = "💾 Card Database",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
        )
        Text(
                text = "${cardProfiles.size} cards stored",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }

      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        IconButton(onClick = { showImportDialog = true }) {
          Icon(Icons.Default.Upload, contentDescription = "Import")
        }
        IconButton(onClick = { showAddDialog = true }) {
          Icon(Icons.Default.Add, contentDescription = "Add Card")
        }
      }
    }

    // Search and Filter Row
    SearchAndFilterCard(
            searchQuery = searchQuery,
            onSearchQueryChanged = { searchQuery = it },
            selectedFilter = selectedFilter,
            onFilterSelected = { selectedFilter = it }
    )

    // Statistics Card
    DatabaseStatisticsCard(
            totalCards = cardProfiles.size,
            filteredCards = filteredCards.size,
            recentCards =
                    cardProfiles.count {
                      (System.currentTimeMillis() - it.cardData.timestampFirstSeen) < 86400000
                    }
    )

    // Cards List
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      items(filteredCards) { cardProfile ->
        CardProfileItem(
                cardProfile = cardProfile,
                onCardSelected = { /* Navigate to detail view */},
                onCardDeleted = { cardProfiles = cardProfiles.filter { it != cardProfile } }
        )
      }

      if (filteredCards.isEmpty()) {
        item { EmptyDatabaseCard(searchQuery = searchQuery, selectedFilter = selectedFilter) }
      }
    }
  }

  // Dialogs
  if (showAddDialog) {
    AddCardDialog(
            onDismiss = { showAddDialog = false },
            onCardAdded = { newCard ->
              cardProfiles = cardProfiles + newCard
              showAddDialog = false
            }
    )
  }

  if (showImportDialog) {
    ImportDialog(
            onDismiss = { showImportDialog = false },
            onCardsImported = { importedCards ->
              cardProfiles = cardProfiles + importedCards
              showImportDialog = false
            }
    )
  }
}

enum class DatabaseFilter {
  ALL,
  VISA,
  MASTERCARD,
  RECENT,
  FAVORITES
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchAndFilterCard(
        searchQuery: String,
        onSearchQueryChanged: (String) -> Unit,
        selectedFilter: DatabaseFilter,
        onFilterSelected: (DatabaseFilter) -> Unit
) {
  Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
      // Search field
      OutlinedTextField(
              value = searchQuery,
              onValueChange = onSearchQueryChanged,
              label = { Text("Search cards...") },
              placeholder = { Text("PAN, cardholder name, or app label") },
              leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
              trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                  IconButton(onClick = { onSearchQueryChanged("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "Clear")
                  }
                }
              },
              modifier = Modifier.fillMaxWidth(),
              singleLine = true
      )

      // Filter chips
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
        DatabaseFilter.values().forEach { filter ->
          FilterChip(
                  onClick = { onFilterSelected(filter) },
                  label = { Text(getFilterDisplayName(filter)) },
                  selected = selectedFilter == filter,
                  leadingIcon = {
                    Icon(
                            imageVector = getFilterIcon(filter),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                    )
                  }
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatabaseStatisticsCard(totalCards: Int, filteredCards: Int, recentCards: Int) {
  Card(
          modifier = Modifier.fillMaxWidth(),
          colors =
                  CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
  ) {
    Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
    ) {
      StatisticItem(
              label = "Total",
              value = totalCards.toString(),
              icon = Icons.Default.CreditCard,
              color = MaterialTheme.colorScheme.primary
      )
      StatisticItem(
              label = "Showing",
              value = filteredCards.toString(),
              icon = Icons.Default.Visibility,
              color = Color.Blue
      )
      StatisticItem(
              label = "Recent",
              value = recentCards.toString(),
              icon = Icons.Default.Schedule,
              color = Color.Green
      )
    }
  }
}

@Composable
private fun StatisticItem(
        label: String,
        value: String,
        icon: androidx.compose.ui.graphics.vector.ImageVector,
        color: Color
) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(24.dp)
    )
    Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CardProfileItem(
        cardProfile: CardProfile,
        onCardSelected: () -> Unit,
        onCardDeleted: () -> Unit
) {
  var showDeleteDialog by remember { mutableStateOf(false) }
  var expanded by remember { mutableStateOf(false) }

  Card(
          onClick = { expanded = !expanded },
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // Header row
      Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
      ) {
        Column(modifier = Modifier.weight(1f)) {
          Text(
                  text = formatPanForDisplay(cardProfile.cardData.pan),
                  style = MaterialTheme.typography.titleMedium,
                  fontWeight = FontWeight.Bold,
                  color = MaterialTheme.colorScheme.onSurface
          )
          Text(
                  text = cardProfile.cardData.applicationLabel.ifEmpty { "Unknown Application" },
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
                  text = formatTimestamp(cardProfile.cardData.timestampFirstSeen),
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
          // Card brand indicator
          CardBrandChip(cardProfile.cardData)

          // Actions
          IconButton(onClick = { showDeleteDialog = true }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
          }

          Icon(
                  imageVector =
                          if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                  contentDescription = if (expanded) "Collapse" else "Expand"
          )
        }
      }

      // Expanded details
      if (expanded) {
        Spacer(modifier = Modifier.height(12.dp))

        CardDetailsSection(cardProfile.cardData)
      }
    }
  }

  // Delete confirmation dialog
  if (showDeleteDialog) {
    AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Card") },
            text = { Text("Are you sure you want to delete this card from the database?") },
            confirmButton = {
              TextButton(
                      onClick = {
                        onCardDeleted()
                        showDeleteDialog = false
                      },
                      colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
              ) { Text("Delete") }
            },
            dismissButton = {
              TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
    )
  }
}

@Composable
private fun CardBrandChip(cardData: EmvCardData) {
  val brandInfo = getBrandInfo(cardData)

  AssistChip(
          onClick = {},
          label = { Text(brandInfo.first, style = MaterialTheme.typography.labelSmall) },
          colors =
                  AssistChipDefaults.assistChipColors(
                          containerColor = brandInfo.second,
                          labelColor = Color.White
                  )
  )
}

@Composable
private fun CardDetailsSection(cardData: EmvCardData) {
  Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
    // Technical details
    if (cardData.cardholderName.isNotEmpty()) {
      DetailRow("Cardholder", cardData.cardholderName)
    }
    if (cardData.expiryDate.isNotEmpty()) {
      DetailRow("Expiry", formatExpiryDate(cardData.expiryDate))
    }
    if (cardData.applicationInterchangeProfile.isNotEmpty()) {
      DetailRow("AIP", cardData.applicationInterchangeProfile)
    }
    if (cardData.availableAids.isNotEmpty()) {
      DetailRow("AIDs", "${cardData.availableAids.size} application(s)")
    }

    // APDU statistics
    DetailRow("APDU Log", "${cardData.apduLog.size} entries")
  }
}

@Composable
private fun DetailRow(label: String, value: String) {
  Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
    Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EmptyDatabaseCard(searchQuery: String, selectedFilter: DatabaseFilter) {
  Card(
          modifier = Modifier.fillMaxWidth(),
          colors =
                  CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
  ) {
    Column(
            modifier = Modifier.fillMaxWidth().padding(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
      Icon(
              imageVector =
                      if (searchQuery.isNotEmpty() || selectedFilter != DatabaseFilter.ALL)
                              Icons.Default.SearchOff
                      else Icons.Default.CreditCardOff,
              contentDescription = null,
              modifier = Modifier.size(64.dp),
              tint = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(16.dp))

      Text(
              text =
                      if (searchQuery.isNotEmpty() || selectedFilter != DatabaseFilter.ALL)
                              "No cards match your search"
                      else "No cards in database",
              style = MaterialTheme.typography.titleMedium,
              fontWeight = FontWeight.Bold,
              color = MaterialTheme.colorScheme.onSurfaceVariant
      )

      Spacer(modifier = Modifier.height(8.dp))

      Text(
              text =
                      if (searchQuery.isNotEmpty() || selectedFilter != DatabaseFilter.ALL)
                              "Try adjusting your search or filter criteria"
                      else "Read your first EMV card or import existing data",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddCardDialog(onDismiss: () -> Unit, onCardAdded: (CardProfile) -> Unit) {
  var pan by remember { mutableStateOf("") }
  var cardholderName by remember { mutableStateOf("") }
  var expiryDate by remember { mutableStateOf("") }
  var applicationLabel by remember { mutableStateOf("") }

  AlertDialog(
          onDismissRequest = onDismiss,
          title = { Text("Add Card Manually") },
          text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
              OutlinedTextField(
                      value = pan,
                      onValueChange = { pan = it },
                      label = { Text("PAN") },
                      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                      modifier = Modifier.fillMaxWidth()
              )

              OutlinedTextField(
                      value = cardholderName,
                      onValueChange = { cardholderName = it },
                      label = { Text("Cardholder Name") },
                      modifier = Modifier.fillMaxWidth()
              )

              OutlinedTextField(
                      value = expiryDate,
                      onValueChange = { expiryDate = it },
                      label = { Text("Expiry (YYMM)") },
                      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                      modifier = Modifier.fillMaxWidth()
              )

              OutlinedTextField(
                      value = applicationLabel,
                      onValueChange = { applicationLabel = it },
                      label = { Text("Application Label") },
                      modifier = Modifier.fillMaxWidth()
              )
            }
          },
          confirmButton = {
            TextButton(
                    onClick = {
                      val cardData =
                              EmvCardData(
                                      cardUid = "MANUAL_$pan",
                                      pan = pan,
                                      track2Data = "",
                                      cardholderName = cardholderName,
                                      expiryDate = expiryDate,
                                      applicationLabel = applicationLabel,
                                      applicationPreferredName = "",
                                      applicationInterchangeProfile = "",
                                      applicationFileLocator = "",
                                      applicationTransactionCounter = "",
                                      unpredictableNumber = "",
                                      terminalVerificationResults = "",
                                      transactionStatusInformation = "",
                                      applicationCryptogram = "",
                                      issuerApplicationData = "",
                                      cryptogramInformationData = "",
                                      cdol1 = "",
                                      cdol2 = "",
                                      pdolConstructed = "",
                                      emvTags = emptyMap(),
                                      availableAids = emptyList(),
                                      apduLog = emptyList(),
                                      timestampFirstSeen = System.currentTimeMillis(),
                                      timestampUpdated = System.currentTimeMillis()
                              )

                      val cardProfile = CardProfile(cardData)
                      onCardAdded(cardProfile)
                    },
                    enabled = pan.isNotEmpty() && cardholderName.isNotEmpty()
            ) { Text("Add") }
          },
          dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImportDialog(onDismiss: () -> Unit, onCardsImported: (List<CardProfile>) -> Unit) {
  AlertDialog(
          onDismissRequest = onDismiss,
          title = { Text("Import Cards") },
          text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
              Text(text = "Select import format:", style = MaterialTheme.typography.bodyMedium)

              listOf("JSON Export", "CSV Format", "EMV Dump").forEach { format ->
                Button(
                        onClick = {
                          // Simulate import
                          val importedCards = generateSampleImportedCards(format)
                          onCardsImported(importedCards)
                        },
                        modifier = Modifier.fillMaxWidth()
                ) { Text(format) }
              }
            }
          },
          confirmButton = {},
          dismissButton = { TextButton(onClick = onDismiss) { Text("Cancel") } }
  )
}

// Helper functions
private fun getFilterDisplayName(filter: DatabaseFilter): String {
  return when (filter) {
    DatabaseFilter.ALL -> "All"
    DatabaseFilter.VISA -> "VISA"
    DatabaseFilter.MASTERCARD -> "Mastercard"
    DatabaseFilter.RECENT -> "Recent"
    DatabaseFilter.FAVORITES -> "Favorites"
  }
}

private fun getFilterIcon(filter: DatabaseFilter): androidx.compose.ui.graphics.vector.ImageVector {
  return when (filter) {
    DatabaseFilter.ALL -> Icons.Default.CreditCard
    DatabaseFilter.VISA -> Icons.Default.Payment
    DatabaseFilter.MASTERCARD -> Icons.Default.Payment
    DatabaseFilter.RECENT -> Icons.Default.Schedule
    DatabaseFilter.FAVORITES -> Icons.Default.Star
  }
}

private fun formatPanForDisplay(pan: String): String {
  if (pan.length < 16) return pan
  return "${pan.substring(0, 4)} **** **** ${pan.takeLast(4)}"
}

private fun formatTimestamp(timestamp: Long): String {
  val date = java.util.Date(timestamp)
  val formatter = java.text.SimpleDateFormat("MMM dd, yyyy HH:mm", java.util.Locale.getDefault())
  return formatter.format(date)
}

private fun formatExpiryDate(expiryDate: String): String {
  if (expiryDate.length >= 4) {
    val month = expiryDate.substring(2, 4)
    val year = "20${expiryDate.substring(0, 2)}"
    return "$month/$year"
  }
  return expiryDate
}

private fun getBrandInfo(cardData: EmvCardData): Pair<String, Color> {
  return when {
    cardData.availableAids.any { it.contains("A0000000031010") } -> "VISA" to Color(0xFF1A1F71)
    cardData.availableAids.any { it.contains("A0000000041010") } -> "MC" to Color(0xFFEB001B)
    cardData.pan.startsWith("4") -> "VISA" to Color(0xFF1A1F71)
    cardData.pan.startsWith("5") -> "MC" to Color(0xFFEB001B)
    else -> "EMV" to Color.Gray
  }
}

private fun generateSampleCardProfiles(): List<CardProfile> {
  return listOf(
          CardProfile(
                  EmvCardData(
                          cardUid = "VISA_4154904674973556",
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
                                          "57" to "4154904674973556D2902"
                                  ),
                          availableAids = listOf("A0000000031010"),
                          apduLog = emptyList(),
                          timestampFirstSeen = System.currentTimeMillis() - 3600000,
                          timestampUpdated = System.currentTimeMillis()
                  )
          ),
          CardProfile(
                  EmvCardData(
                          cardUid = "MC_5555444433331111",
                          pan = "5555444433331111",
                          track2Data = "5555444433331111D2512",
                          cardholderName = "JANE SMITH",
                          expiryDate = "2512",
                          applicationLabel = "MASTERCARD",
                          applicationPreferredName = "MASTERCARD",
                          applicationInterchangeProfile = "1800",
                          applicationFileLocator = "10010103180201059000",
                          applicationTransactionCounter = "0005",
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
                                          "57" to "5555444433331111D2512"
                                  ),
                          availableAids = listOf("A0000000041010"),
                          apduLog = emptyList(),
                          timestampFirstSeen = System.currentTimeMillis() - 86400000,
                          timestampUpdated = System.currentTimeMillis()
                  )
          )
  )
}

private fun generateSampleImportedCards(format: String): List<CardProfile> {
  return listOf(
          CardProfile(
                  EmvCardData(
                          cardUid = "IMPORTED_$format",
                          pan = "4000000000000002",
                          track2Data = "4000000000000002D2512",
                          cardholderName = "IMPORTED USER",
                          expiryDate = "2512",
                          applicationLabel = "IMPORTED CARD",
                          applicationPreferredName = "IMPORT",
                          applicationInterchangeProfile = "0000",
                          applicationFileLocator = "",
                          applicationTransactionCounter = "",
                          unpredictableNumber = "",
                          terminalVerificationResults = "",
                          transactionStatusInformation = "",
                          applicationCryptogram = "",
                          issuerApplicationData = "",
                          cryptogramInformationData = "",
                          cdol1 = "",
                          cdol2 = "",
                          pdolConstructed = "",
                          emvTags = emptyMap(),
                          availableAids = emptyList(),
                          apduLog = emptyList(),
                          timestampFirstSeen = System.currentTimeMillis(),
                          timestampUpdated = System.currentTimeMillis()
                  )
          )
  )
}
