package com.example.nfsp00f

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nfsp00f.ui.theme.nfSp00fTheme
import com.example.nfsp00f.ui.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { 
            nfSp00fTheme { 
                nfSp00fApp() 
            } 
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun nfSp00fApp() {
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
                                                    else Color(0xFF4CAF50).copy(alpha = 0.6f)
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
                                                indicatorColor = Color.Transparent
                                        )
                        )
                    }
                }
            }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            when (selectedTab) {
                0 -> dashboardScreen()
                1 -> cardReadingScreen()
                2 -> emulationScreen()
                3 -> databaseScreen()
                4 -> analysisScreen()
            }
        }
    }
}
