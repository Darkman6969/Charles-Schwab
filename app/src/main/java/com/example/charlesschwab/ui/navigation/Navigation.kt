package com.example.charlesschwab.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object Login : Screen
    
    @Serializable
    data object Main : Screen // Parent for the 5-tab scaffold
}

@Serializable
sealed interface MainTab {
    @Serializable
    data object Summary : MainTab
    
    @Serializable
    data object Watchlist : MainTab
    
    @Serializable
    data object Trade : MainTab
    
    @Serializable
    data object Markets : MainTab
    
    @Serializable
    data object More : MainTab
}

@Serializable
data class StockDetail(val symbol: String) : Screen
