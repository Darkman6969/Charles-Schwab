package com.example.charlesschwab.data.repository

import com.example.charlesschwab.domain.model.*
import com.example.charlesschwab.domain.repository.StockRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class MockStockRepository @Inject constructor() : StockRepository {

    private val symbols = listOf("AAPL", "MSFT", "GOOGL", "AMZN", "SCHD", "VOO")
    
    override fun getAccounts(): Flow<List<Account>> = flow {
        val accounts = listOf(
            Account("1", "Individual Brokerage", "*4291", 125430.55, 1240.10, 0.99),
            Account("2", "Roth IRA", "*8822", 85000.00, -450.20, -0.53),
            Account("3", "Cash Management", "*1102", 5200.45, 0.00, 0.00)
        )
        emit(accounts)
    }

    override fun getWatchlist(): Flow<List<StockQuote>> = flow {
        while (true) {
            val quotes = symbols.map { createMockQuote(it) }
            emit(quotes)
            delay(3000) // Update every 3 seconds
        }
    }

    override fun getStockQuote(symbol: String): Flow<StockQuote> = flow {
        while (true) {
            emit(createMockQuote(symbol))
            delay(2000) // Ticker updates faster for detail view
        }
    }

    override suspend fun executeTrade(order: OrderTicket): Result<Unit> = Result.success(Unit)

    private fun createMockQuote(symbol: String): StockQuote {
        val basePrice = when(symbol) {
            "AAPL" -> 220.50
            "MSFT" -> 410.15
            "GOOGL" -> 175.30
            "AMZN" -> 185.00
            "SCHD" -> 82.40
            "VOO" -> 510.00
            else -> 100.00
        }
        
        val randomVar = Random.nextDouble(-1.5, 1.5)
        val lastPrice = basePrice + randomVar
        val bid = lastPrice - 0.05
        val ask = lastPrice + 0.05
        
        return StockQuote(
            symbol = symbol,
            companyName = getCompanyName(symbol),
            lastPrice = lastPrice,
            change = randomVar,
            changePercent = (randomVar / basePrice) * 100,
            bid = bid,
            ask = ask,
            volume = 45000000L + Random.nextLong(1000000L),
            marketStatus = MarketStatus.OPEN,
            history = createMockHistory(basePrice)
        )
    }

    private fun getCompanyName(symbol: String) = when(symbol) {
        "AAPL" -> "Apple Inc."
        "MSFT" -> "Microsoft Corp."
        "GOOGL" -> "Alphabet Inc."
        "AMZN" -> "Amazon.com Inc."
        "SCHD" -> "Schwab US Dividend Equity ETF"
        "VOO" -> "Vanguard S&P 500 ETF"
        else -> "Unknown Company"
    }

    private fun createMockHistory(basePrice: Double): List<ChartPoint> {
        val points = mutableListOf<ChartPoint>()
        var currentPrice = basePrice - 10.0
        val now = System.currentTimeMillis()
        
        for (i in 0 until 50) {
            val open = currentPrice
            val high = open + Random.nextDouble(2.0)
            val low = open - Random.nextDouble(2.0)
            val close = (high + low) / 2
            points.add(ChartPoint(
                timestamp = now - (50 - i) * 3600000L,
                open = open,
                high = high,
                low = low,
                close = close,
                volume = 1000000L + Random.nextLong(500000L)
            ))
            currentPrice = close
        }
        return points
    }
}
