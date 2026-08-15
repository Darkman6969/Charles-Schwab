package com.example.charlesschwab.ui.markets

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.charlesschwab.domain.model.StockQuote
import com.example.charlesschwab.ui.summary.formatCurrency
import com.example.charlesschwab.ui.theme.BullishGreen
import com.example.charlesschwab.ui.theme.SchwabNavy

@Composable
fun MarketsScreen(
    viewModel: MarketsViewModel = hiltViewModel()
) {
    val indices by viewModel.marketIndices.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Market Update",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.Bold,
            color = SchwabNavy
        )
        
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(indices) { index ->
                IndexRow(index)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
                MarketNewsSection()
            }
        }
    }
}

@Composable
fun IndexRow(index: StockQuote) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(index.companyName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(index.symbol, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(formatCurrency(index.lastPrice).replace("$", ""), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(
                text = "${if (index.change >= 0) "+" else ""}${String.format("%.2f", index.change)} (${String.format("%.2f", index.changePercent)}%)",
                color = if (index.change >= 0) BullishGreen else Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun MarketNewsSection() {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Top Stories", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = SchwabNavy)
        Spacer(modifier = Modifier.height(16.dp))
        
        repeat(3) { i ->
            NewsItem(
                title = when(i) {
                    0 -> "Markets Rally as Inflation Data Cools More Than Expected"
                    1 -> "Tech Giants Lead Gains; Semiconductor Sector Hits Record High"
                    else -> "Oil Prices Stabilize Amid Global Supply Concerns"
                },
                source = "MarketWatch • 2h ago"
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun NewsItem(title: String, source: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(source, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        }
    }
}
