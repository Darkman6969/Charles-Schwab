package com.example.charlesschwab.ui.trade

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.charlesschwab.domain.model.OrderTiming
import com.example.charlesschwab.domain.model.OrderType
import com.example.charlesschwab.domain.model.TradeAction

// --- Design Tokens (Overrides standard Material 3) ---
val SchwabNavyToken = Color(0xFF0A1B29)
val SchwabBlueToken = Color(0xFF00A0DF)
val SuccessGreenToken = Color(0xFF00875A)
val ErrorRedToken = Color(0xFFD1345B)
val LightDividerToken = Color(0xFFE2E8F0)
val DarkDividerToken = Color(0xFF2D3748)

// Tabular Numerals TextStyle for financial data (prevents jumping UI)
val TabularTextStyle = TextStyle(
    fontFeatureSettings = "tnum",
    fontSize = 14.sp,
    color = Color.Unspecified
)

enum class OrderAction { BUY, SELL, SELL_SHORT, BUY_TO_COVER }
enum class OrderTypeLocal { MARKET, LIMIT, STOP, STOP_LIMIT, TRAILING_STOP }
enum class TimeInForce { DAY, GTC, DAY_EXT, GTC_EXT }

@Composable
fun TradeOrderTicketScreen(
    ticker: String = "AAPL",
    currentPrice: Double = 178.25,
    bidPrice: Double = 178.20,
    askPrice: Double = 178.30,
    onClose: () -> Unit = {},
    viewModel: TradeViewModel = hiltViewModel()
) {
    var action by remember { mutableStateOf(OrderAction.BUY) }
    var quantity by remember { mutableStateOf("10") }
    var orderType by remember { mutableStateOf(OrderTypeLocal.MARKET) }
    var limitPrice by remember { mutableStateOf(currentPrice.toString()) }
    var stopPrice by remember { mutableStateOf(currentPrice.toString()) }
    var timeInForce by remember { mutableStateOf(TimeInForce.DAY) }
    
    var showReviewDialog by remember { mutableStateOf(false) }

    LaunchedEffect(ticker) {
        viewModel.updateSymbol(ticker)
        viewModel.updateQuantity(10.0, false) // Default quantity
        viewModel.updateAction(TradeAction.BUY) // Default action
    }

    val isDarkTheme = true // Hardcoded for preview, ideally use isSystemInDarkTheme()
    val bgColor = if (isDarkTheme) SchwabNavyToken else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val dividerColor = if (isDarkTheme) DarkDividerToken else LightDividerToken

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        // --- 1. Header (Quote) ---
        TopQuoteHeader(
            ticker = ticker,
            currentPrice = currentPrice,
            bidPrice = bidPrice,
            askPrice = askPrice,
            textColor = textColor,
            onClose = onClose
        )
        HorizontalDivider(thickness = 1.dp, color = dividerColor)

        // --- 2. Order Form ---
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            
            // Action Selection
            DenseLabel(text = "Action", textColor = textColor)
            ActionSelector(
                selectedAction = action,
                onActionSelected = { 
                    action = it
                    viewModel.updateAction(TradeAction.valueOf(it.name))
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Quantity
            DenseLabel(text = "Quantity (Shares)", textColor = textColor)
            DataEntryField(
                value = quantity,
                onValueChange = { 
                    val filtered = it.filter { char -> char.isDigit() }
                    quantity = filtered
                    viewModel.updateQuantity(filtered.toDoubleOrNull() ?: 0.0, false)
                },
                textColor = textColor,
                dividerColor = dividerColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Order Type
            DenseLabel(text = "Order Type", textColor = textColor)
            DropdownSelector(
                options = OrderTypeLocal.entries.map { it.name.replace("_", " ") },
                selectedIndex = orderType.ordinal,
                onOptionSelected = { 
                    orderType = OrderTypeLocal.entries[it]
                    viewModel.updateOrderType(OrderType.valueOf(OrderTypeLocal.entries[it].name))
                },
                textColor = textColor,
                dividerColor = dividerColor
            )

            // Conditional Price Fields based on Order Type
            if (orderType == OrderTypeLocal.LIMIT || orderType == OrderTypeLocal.STOP_LIMIT) {
                Spacer(modifier = Modifier.height(16.dp))
                DenseLabel(text = "Limit Price", textColor = textColor)
                DataEntryField(
                    value = limitPrice,
                    onValueChange = { limitPrice = it },
                    textColor = textColor,
                    dividerColor = dividerColor,
                    isDecimal = true
                )
            }

            if (orderType == OrderTypeLocal.STOP || orderType == OrderTypeLocal.STOP_LIMIT || orderType == OrderTypeLocal.TRAILING_STOP) {
                Spacer(modifier = Modifier.height(16.dp))
                DenseLabel(text = "Stop Price / Trail Amount", textColor = textColor)
                DataEntryField(
                    value = stopPrice,
                    onValueChange = { stopPrice = it },
                    textColor = textColor,
                    dividerColor = dividerColor,
                    isDecimal = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Timing (Time in Force)
            DenseLabel(text = "Timing", textColor = textColor)
            DropdownSelector(
                options = listOf("Day Only", "Good 'Til Canceled (GTC)", "Day + Ext Hours", "GTC + Ext Hours"),
                selectedIndex = timeInForce.ordinal,
                onOptionSelected = { timeInForce = TimeInForce.entries[it] },
                textColor = textColor,
                dividerColor = dividerColor
            )
        }

        // --- 3. Footer (Review & Cost) ---
        HorizontalDivider(thickness = 1.dp, color = dividerColor)
        OrderFooter(
            action = action,
            quantity = quantity.toIntOrNull() ?: 0,
            price = if (orderType == OrderTypeLocal.MARKET) currentPrice else limitPrice.toDoubleOrNull() ?: 0.0,
            onReviewClick = { showReviewDialog = true }
        )
    }

    if (showReviewDialog) {
        OrderReviewDialog(
            action = action,
            quantity = quantity,
            ticker = ticker,
            orderType = orderType,
            limitPrice = limitPrice,
            stopPrice = stopPrice,
            timeInForce = timeInForce,
            onConfirm = { 
                viewModel.placeOrder(onComplete = {
                    showReviewDialog = false
                    onClose()
                })
            },
            onDismiss = { showReviewDialog = false }
        )
    }
}

@Composable
fun TopQuoteHeader(ticker: String, currentPrice: Double, bidPrice: Double, askPrice: Double, textColor: Color, onClose: () -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Trade: $ticker", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textColor)
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close", tint = textColor)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            QuoteDetail("Last", currentPrice, textColor)
            QuoteDetail("Bid", bidPrice, textColor)
            QuoteDetail("Ask", askPrice, textColor)
        }
    }
}

@Composable
fun QuoteDetail(label: String, value: Double, textColor: Color) {
    Column(horizontalAlignment = Alignment.Start) {
        Text(text = label, fontSize = 11.sp, color = textColor.copy(alpha = 0.6f))
        Text(text = "$$value", style = TabularTextStyle.copy(color = textColor, fontWeight = FontWeight.SemiBold))
    }
}

@Composable
fun DenseLabel(text: String, textColor: Color) {
    Text(
        text = text.uppercase(),
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = textColor.copy(alpha = 0.6f),
        modifier = Modifier.padding(bottom = 4.dp)
    )
}

@Composable
fun ActionSelector(selectedAction: OrderAction, onActionSelected: (OrderAction) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        OrderAction.entries.forEach { action ->
            val isSelected = selectedAction == action
            val bgColor = if (isSelected) SchwabBlueToken else Color.Transparent
            val contentColor = if (isSelected) Color.White else SchwabBlueToken
            
            Box(
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, SchwabBlueToken, RoundedCornerShape(0.dp))
                    .background(bgColor)
                    .clickable { onActionSelected(action) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = action.name.replace("_", " "), 
                    fontSize = 13.sp, 
                    fontWeight = FontWeight.Bold, 
                    color = contentColor
                )
            }
        }
    }
}

@Composable
fun DataEntryField(value: String, onValueChange: (String) -> Unit, textColor: Color, dividerColor: Color, isDecimal: Boolean = false) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        textStyle = TabularTextStyle.copy(color = textColor, fontSize = 16.sp),
        keyboardOptions = KeyboardOptions(keyboardType = if (isDecimal) KeyboardType.Decimal else KeyboardType.NumberPassword),
        cursorBrush = SolidColor(SchwabBlueToken),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, dividerColor, RoundedCornerShape(2.dp))
            .padding(12.dp)
    )
}

@Composable
fun DropdownSelector(options: List<String>, selectedIndex: Int, onOptionSelected: (Int) -> Unit, textColor: Color, dividerColor: Color) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, dividerColor, RoundedCornerShape(2.dp))
            .clickable { expanded = true }
            .padding(12.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = options[selectedIndex], color = textColor, fontSize = 16.sp)
            Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = textColor)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(if (textColor == Color.White) SchwabNavyToken else Color.White)
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = { Text(text = option, color = textColor) },
                    onClick = {
                        onOptionSelected(index)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun OrderFooter(action: OrderAction, quantity: Int, price: Double, onReviewClick: () -> Unit) {
    val estimatedCost = quantity * price
    val isBuy = action == OrderAction.BUY || action == OrderAction.BUY_TO_COVER
    val actionColor = if (isBuy) SuccessGreenToken else ErrorRedToken
    
    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = if (isBuy) "Estimated Cost" else "Estimated Proceeds", fontSize = 12.sp, color = Color.Gray)
            Text(
                text = "$%,.2f".format(estimatedCost), 
                style = TabularTextStyle.copy(fontSize = 14.sp, fontWeight = FontWeight.Bold, color = actionColor)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onReviewClick,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(2.dp),
            colors = ButtonDefaults.buttonColors(containerColor = actionColor)
        ) {
            Text("Review Order", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun OrderReviewDialog(
    action: OrderAction, quantity: String, ticker: String, orderType: OrderTypeLocal, 
    limitPrice: String, stopPrice: String, timeInForce: TimeInForce,
    onConfirm: () -> Unit, onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Surface(
            modifier = Modifier.fillMaxWidth(0.9f),
            shape = RoundedCornerShape(4.dp), // Sharp edge dialog
            color = SchwabNavyToken
        ) {
            Column(modifier = Modifier.padding(0.dp)) {
                // Dialog Header
                Box(modifier = Modifier.fillMaxWidth().background(Color(0xFF162938)).padding(16.dp)) {
                    Text("Verify Order Details", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                
                Column(modifier = Modifier.padding(16.dp)) {
                    ReviewRow("Action", action.name.replace("_", " "))
                    ReviewRow("Quantity", "$quantity Shares")
                    ReviewRow("Symbol", ticker)
                    ReviewRow("Order Type", orderType.name.replace("_", " "))
                    
                    if (orderType == OrderTypeLocal.LIMIT || orderType == OrderTypeLocal.STOP_LIMIT) {
                        ReviewRow("Limit Price", "$$limitPrice")
                    }
                    if (orderType == OrderTypeLocal.STOP || orderType == OrderTypeLocal.STOP_LIMIT || orderType == OrderTypeLocal.TRAILING_STOP) {
                        ReviewRow("Stop Price", "$$stopPrice")
                    }
                    
                    ReviewRow("Timing", timeInForce.name.replace("_", " "))
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "By pressing submit, you acknowledge you have read the trading disclosures.",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(2.dp),
                        ) {
                            Text("Do Not Place", color = Color.White)
                        }
                        Button(
                            onClick = onConfirm,
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(2.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SchwabBlueToken)
                        ) {
                            Text("Place Order", color = Color.White)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReviewRow(label: String, value: String) {
    Column {
        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = label, fontSize = 14.sp, color = Color.Gray)
            Text(text = value, style = TabularTextStyle.copy(color = Color.White, fontWeight = FontWeight.SemiBold))
        }
        HorizontalDivider(thickness = 1.dp, color = DarkDividerToken)
    }
}
