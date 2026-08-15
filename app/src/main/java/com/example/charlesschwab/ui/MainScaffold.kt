package com.example.charlesschwab.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.charlesschwab.ui.navigation.MainTab
import com.example.charlesschwab.ui.summary.SummaryScreen
import com.example.charlesschwab.ui.trade.TradeOrderTicketScreen

@Composable
fun MainScaffold() {
    var selectedTab by remember { mutableStateOf<MainTab>(MainTab.Summary) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 0.dp
            ) {
                val tabs = listOf(
                    NavigationItem("Summary", Icons.Outlined.AccountBalance, MainTab.Summary),
                    NavigationItem("Watchlist", Icons.Outlined.StarOutline, MainTab.Watchlist),
                    NavigationItem("Trade", Icons.Outlined.SwapHoriz, MainTab.Trade),
                    NavigationItem("Markets", Icons.Outlined.Public, MainTab.Markets),
                    NavigationItem("More", Icons.Outlined.Menu, MainTab.More)
                )

                tabs.forEach { item ->
                    NavigationBarItem(
                        selected = selectedTab == item.tab,
                        onClick = { 
                            selectedTab = item.tab
                        },
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label, style = MaterialTheme.typography.labelSmall) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = Color.Gray,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Surface(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                MainTab.Summary -> SummaryScreen()
                MainTab.Watchlist -> Text("Watchlist Screen (Implementation Pending)")
                MainTab.Trade -> TradeOrderTicketScreen()
                MainTab.Markets -> Text("Markets Screen (Implementation Pending)")
                MainTab.More -> Text("More Options (Settings, Transfers, Help)")
            }
        }
    }
}

data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val tab: MainTab
)
