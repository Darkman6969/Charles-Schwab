package com.example.charlesschwab.ui.summary

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
import com.example.charlesschwab.domain.model.Account
import com.example.charlesschwab.ui.components.DisclaimerFooter
import com.example.charlesschwab.ui.theme.BullishGreen
import com.example.charlesschwab.ui.theme.SchwabNavy
import java.text.NumberFormat
import java.util.*

@Composable
fun SummaryScreen(
    viewModel: SummaryViewModel = hiltViewModel()
) {
    val accounts by viewModel.accounts.collectAsState()
    val totalBalance = accounts.sumOf { it.balance }
    val totalChange = accounts.sumOf { it.dayChange }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            TotalBalanceCard(totalBalance, totalChange)
        }
        
        items(accounts) { account ->
            AccountRow(account)
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
        }
        
        item {
            DisclaimerFooter()
        }
    }
}

@Composable
fun TotalBalanceCard(balance: Double, change: Double) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = SchwabNavy),
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text("Total Account Value", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.bodyMedium)
            Text(
                text = formatCurrency(balance),
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium,
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${if (change >= 0) "+" else ""}${formatCurrency(change)} (Day's Gain/Loss)",
                color = if (change >= 0) BullishGreen else Color.Red,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun AccountRow(account: Account) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(account.name, style = MaterialTheme.typography.titleMedium)
            Text(account.maskedId, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(formatCurrency(account.balance), style = MaterialTheme.typography.titleMedium)
            Text(
                text = "${if (account.dayChange >= 0) "+" else ""}${formatCurrency(account.dayChange)}",
                color = if (account.dayChange >= 0) BullishGreen else Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

fun formatCurrency(amount: Double): String {
    val format = NumberFormat.getCurrencyInstance(Locale.US)
    return format.format(amount)
}
