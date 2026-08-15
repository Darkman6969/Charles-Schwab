package com.example.charlesschwab.ui.trade

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.charlesschwab.domain.model.*
import com.example.charlesschwab.ui.theme.SchwabNavy

@Composable
fun TradeOrderScreen(
    viewModel: TradeViewModel = hiltViewModel()
) {
    val order by viewModel.order.collectAsState()
    var isReviewMode by remember { mutableStateOf(false) }

    if (isReviewMode) {
        OrderReviewScreen(order, onBack = { isReviewMode = false })
    } else {
        OrderFormScreen(
            order = order,
            onUpdateAction = viewModel::updateAction,
            onUpdateType = viewModel::updateOrderType,
            onReview = { isReviewMode = true }
        )
    }
}

@Composable
fun OrderFormScreen(
    order: OrderTicket,
    onUpdateAction: (TradeAction) -> Unit,
    onUpdateType: (OrderType) -> Unit,
    onReview: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Standard Trade", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        
        Text("Action", style = MaterialTheme.typography.titleMedium)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TradeAction.values().forEach { action ->
                FilterChip(
                    selected = order.action == action,
                    onClick = { onUpdateAction(action) },
                    label = { Text(action.name.replace("_", " ")) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Order Type", style = MaterialTheme.typography.titleMedium)
        Row(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState(), enabled = false), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
             OrderType.values().take(3).forEach { type ->
                FilterChip(
                    selected = order.orderType == type,
                    onClick = { onUpdateType(type) },
                    label = { Text(type.name) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onReview,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SchwabNavy),
            shape = MaterialTheme.shapes.extraSmall
        ) {
            Text("Review Order", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun OrderReviewScreen(order: OrderTicket, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Review Order", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("${order.action} ${order.symbol}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Type: ${order.orderType}", style = MaterialTheme.typography.bodyMedium)
                Text("Timing: ${order.timing}", style = MaterialTheme.typography.bodyMedium)
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Quotes are delayed. Estimated commission is $0.00. Market orders are executed at the next available price. Please review all details before confirming.",
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(60.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SchwabNavy),
            shape = MaterialTheme.shapes.extraSmall
        ) {
            Text("SWIPE TO CONFIRM", fontWeight = FontWeight.ExtraBold)
        }
        
        TextButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Edit Order")
        }
    }
}
