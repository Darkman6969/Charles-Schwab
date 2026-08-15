package com.example.charlesschwab.ui.quote

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.charlesschwab.domain.model.MarketStatus
import com.example.charlesschwab.domain.model.StockQuote
import com.example.charlesschwab.ui.theme.BullishGreen
import com.example.charlesschwab.ui.theme.SchwabNavy
import java.text.NumberFormat
import java.util.*

@Composable
fun StockDetailScreen(
    viewModel: StockDetailViewModel = hiltViewModel()
) {
    val quote by viewModel.stockQuote.collectAsState()

    quote?.let { q ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            StockHeader(q)
            Spacer(modifier = Modifier.height(24.dp))
            TechnicalChart(
                points = q.history,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            QuoteStatistics(q)
            Spacer(modifier = Modifier.height(32.dp))
            TradeButtons()
        }
    }
}

@Composable
fun StockHeader(quote: StockQuote) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val statusColor = when(quote.marketStatus) {
                MarketStatus.OPEN -> BullishGreen
                else -> Color.Gray
            }
            Surface(modifier = Modifier.size(8.dp), shape = MaterialTheme.shapes.small, color = statusColor) {}
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (quote.marketStatus == MarketStatus.OPEN) "Market Open" else "Market Closed",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
        Text(quote.symbol, style = MaterialTheme.typography.headlineMedium)
        Text(quote.companyName, style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = formatCurrency(quote.lastPrice),
            style = MaterialTheme.typography.headlineMedium,
            fontSize = 36.sp
        )
        Text(
            text = "${formatCurrency(quote.change)} (${String.format("%.2f", quote.changePercent)}%)",
            color = if (quote.change >= 0) BullishGreen else Color.Red,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun QuoteStatistics(quote: StockQuote) {
    Column {
        Text("Quote Detail", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            StatItem("Bid", formatCurrency(quote.bid))
            StatItem("Ask", formatCurrency(quote.ask))
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            StatItem("Volume", quote.volume.toString())
            StatItem("Market Cap", "2.8T")
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun TradeButtons() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Button(
            onClick = {},
            modifier = Modifier.weight(1f).height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BullishGreen),
            shape = MaterialTheme.shapes.extraSmall
        ) {
            Text("Buy", fontWeight = FontWeight.Bold)
        }
        Button(
            onClick = {},
            modifier = Modifier.weight(1f).height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            shape = MaterialTheme.shapes.extraSmall
        ) {
            Text("Sell", fontWeight = FontWeight.Bold)
        }
    }
}

private fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale.US)
    return format.format(amount)
}
