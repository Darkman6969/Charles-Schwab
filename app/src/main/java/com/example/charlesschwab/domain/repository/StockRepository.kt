package com.example.charlesschwab.domain.repository

import com.example.charlesschwab.domain.model.Account
import com.example.charlesschwab.domain.model.StockQuote
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    fun getAccounts(): Flow<List<Account>>
    fun getWatchlist(): Flow<List<StockQuote>>
    fun getStockQuote(symbol: String): Flow<StockQuote>
    suspend fun executeTrade(order: com.example.charlesschwab.domain.model.OrderTicket): Result<Unit>
}
