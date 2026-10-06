package com.example.charlesschwab.ui.watchlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.charlesschwab.domain.model.StockQuote
import com.example.charlesschwab.ui.summary.formatCurrency
import com.example.charlesschwab.ui.theme.BullishGreen
import com.example.charlesschwab.ui.theme.SchwabNavy

@Composable
fun WatchlistScreen(
    viewModel: WatchlistViewModel = hiltViewModel(),
    onStockClick: (String) -> Unit = {}
) {
    val stocks by viewModel.watchlist.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF4F6F8))) {
        Surface(modifier = Modifier.fillMaxWidth(), color = Color.White, shadowElevation = 1.dp) {
            Text(
                text = "Watchlist",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold,
                color = SchwabNavy
            )
        }
        
        // --- Table Header ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("SYMBOL / NAME", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text("LAST / CHANGE", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
        HorizontalDivider(thickness = 0.5.dp)

        LazyColumn(modifier = Modifier.fillMaxSize().background(Color.White)) {
            items(stocks) { stock ->
                WatchlistRow(stock, onClick = { onStockClick(stock.symbol) })
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
            }
        }
    }
}

@Composable
fun WatchlistRow(stock: StockQuote, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(stock.symbol, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(stock.companyName, style = MaterialTheme.typography.bodySmall, color = Color.Gray, maxLines = 1)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = formatCurrency(stock.lastPrice),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontFeatureSettings = "tnum"
                )
            )
            Text(
                text = "${if (stock.change >= 0) "+" else ""}${formatCurrency(stock.change)} (${String.format("%.2f", stock.changePercent)}%)",
                color = if (stock.change >= 0) BullishGreen else Color.Red,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFeatureSettings = "tnum"
                )
            )
        }
    }
}
