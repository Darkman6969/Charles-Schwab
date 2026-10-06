package com.example.charlesschwab.ui.markets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.charlesschwab.domain.model.StockQuote
import com.example.charlesschwab.domain.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MarketsViewModel @Inject constructor(
    private val repository: StockRepository
) : ViewModel() {

    private val indexSymbols = listOf("SPX", "DJIA", "IXIC")

    val marketIndices: StateFlow<List<StockQuote>> = repository.getWatchlist()
        .map { quotes -> quotes.filter { it.symbol in indexSymbols } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val topGainers: StateFlow<List<StockQuote>> = repository.getWatchlist()
        .map { quotes -> 
            quotes.filter { it.symbol !in indexSymbols }
                .sortedByDescending { it.changePercent }
                .take(5)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val mostActive: StateFlow<List<StockQuote>> = repository.getWatchlist()
        .map { quotes -> 
            quotes.filter { it.symbol !in indexSymbols }
                .sortedByDescending { it.volume }
                .take(5)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
