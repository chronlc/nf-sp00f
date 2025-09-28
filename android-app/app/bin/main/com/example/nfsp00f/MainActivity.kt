package com.example.nfsp00f

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.nfsp00f.ui.*
import com.example.nfsp00f.ui.theme.NfSp00fTheme

/**
 * Main Activity for nf-sp00f EMV Security Platform Professional Material3 interface with bottom
 * navigation
 */
class MainActivity : ComponentActivity() {

  @OptIn(ExperimentalMaterial3Api::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      NfSp00fTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
          MainScreen()
        }
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
  val navController = rememberNavController()

  Scaffold(
          topBar = {
            TopAppBar(
                    title = {
                      Text(
                              text = "🏴‍☠️ nf-sp00f",
                              fontWeight = FontWeight.Bold,
                              color = MaterialTheme.colorScheme.onPrimary
                      )
                    },
                    colors =
                            TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.primary,
                                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                            )
            )
          },
          bottomBar = {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceVariant) {
              val navBackStackEntry by navController.currentBackStackEntryAsState()
              val currentDestination = navBackStackEntry?.destination

              navigationItems.forEach { item ->
                NavigationBarItem(
                        icon = {
                          Icon(
                                  item.icon,
                                  contentDescription = item.title,
                                  tint =
                                          if (currentDestination?.hierarchy?.any {
                                                    it.route == item.route
                                                  } == true
                                          ) {
                                            MaterialTheme.colorScheme.primary
                                          } else {
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                          }
                          )
                        },
                        label = {
                          Text(
                                  item.title,
                                  color =
                                          if (currentDestination?.hierarchy?.any {
                                                    it.route == item.route
                                                  } == true
                                          ) {
                                            MaterialTheme.colorScheme.primary
                                          } else {
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                          }
                          )
                        },
                        selected =
                                currentDestination?.hierarchy?.any { it.route == item.route } ==
                                        true,
                        onClick = {
                          navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                              saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                          }
                        },
                        colors =
                                NavigationBarItemDefaults.colors(
                                        selectedIconColor = MaterialTheme.colorScheme.primary,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        unselectedIconColor =
                                                MaterialTheme.colorScheme.onSurfaceVariant,
                                        unselectedTextColor =
                                                MaterialTheme.colorScheme.onSurfaceVariant,
                                        indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                )
                )
              }
            }
          }
  ) { innerPadding ->
    NavHost(
            navController = navController,
            startDestination = NavigationItem.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
    ) {
      composable(NavigationItem.Dashboard.route) { DashboardFragment() }
      composable(NavigationItem.CardReading.route) { CardReadingFragment() }
      composable(NavigationItem.Emulation.route) { EmulationFragment() }
      composable(NavigationItem.Database.route) { CardDatabaseFragment() }
      composable(NavigationItem.Analysis.route) { AnalysisFragment() }
    }
  }
}

sealed class NavigationItem(
        val route: String,
        val title: String,
        val icon: androidx.compose.ui.graphics.vector.ImageVector
) {
  object Dashboard : NavigationItem("dashboard", "Dashboard", Icons.Default.Dashboard)
  object CardReading : NavigationItem("card_reading", "Read", Icons.Default.Nfc)
  object Emulation : NavigationItem("emulation", "Emulate", Icons.Default.Security)
  object Database : NavigationItem("database", "Database", Icons.Default.Storage)
  object Analysis : NavigationItem("analysis", "Analysis", Icons.Default.Analytics)
}

private val navigationItems =
        listOf(
                NavigationItem.Dashboard,
                NavigationItem.CardReading,
                NavigationItem.Emulation,
                NavigationItem.Database,
                NavigationItem.Analysis
        )

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
  NfSp00fTheme { MainScreen() }
}
