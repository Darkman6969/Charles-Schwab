package com.example.charlesschwab.ui.trade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.charlesschwab.domain.model.*
import com.example.charlesschwab.domain.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TradeViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel() {

    private val _order = MutableStateFlow(
        OrderTicket(
            symbol = "",
            action = TradeAction.BUY,
            quantity = 0.0,
            isQuantityInDollars = false,
            orderType = OrderType.MARKET,
            timing = OrderTiming.DAY
        )
    )
    val order: StateFlow<OrderTicket> = _order.asStateFlow()

    fun updateSymbol(symbol: String) {
        _order.value = _order.value.copy(symbol = symbol)
    }

    fun updateAction(action: TradeAction) {
        _order.value = _order.value.copy(action = action)
    }

    fun updateQuantity(quantity: Double, inDollars: Boolean) {
        _order.value = _order.value.copy(quantity = quantity, isQuantityInDollars = inDollars)
    }

    fun updateOrderType(type: OrderType) {
        _order.value = _order.value.copy(orderType = type)
    }

    fun placeOrder(onComplete: () -> Unit) {
        viewModelScope.launch {
            repository.executeTrade(_order.value)
            onComplete()
        }
    }
}
