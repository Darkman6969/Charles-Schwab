package com.example.charlesschwab.data.repository

import com.example.charlesschwab.domain.model.*
import com.example.charlesschwab.domain.repository.StockRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class   MockStockRepository @Inject constructor() : StockRepository {

    private val symbols = listOf(
        "AAPL", "MSFT", "GOOGL", "AMZN", "NVDA", "TSLA", "META",
        "VOO", "SCHD", "QQQ", "DIA",
        "SPX", "DJIA", "IXIC",
        "BRK.B", "JPM", "UNH", "V", "JNJ", "WMT", "PG",
        "eAPRK"
    )

    private val _accounts = MutableStateFlow(
        listOf(
            Account(
                id = "1",
                name = "Brokerage Account",
                maskedId = "*8731",
                balance = 14823.80, // 500 shares * $29.647598173
                dayChange = 3673.80, // 14823.80 - 11150.00 cost basis
                dayChangePercent = 32.95,
                holdings = listOf(
                    Holding("eAPRK", 500.0, 22.3, 29.647598173) // 500 shares @ avg cost $22.30, current price $29.647598173
                )
            )
        )
    )
    
    override fun getAccounts(): Flow<List<Account>> = _accounts.asStateFlow()

    override fun getWatchlist(): Flow<List<StockQuote>> = flow {
        while (true) {
            val quotes = symbols.map { createMockQuote(it) }
            emit(quotes)
            delay(3000)
        }
    }

    override fun getStockQuote(symbol: String): Flow<StockQuote> = flow {
        while (true) {
            emit(createMockQuote(symbol))
            delay(2000)
        }
    }

    override suspend fun executeTrade(order: OrderTicket): Result<Unit> {
        _accounts.update { currentAccounts ->
            currentAccounts.map { account ->
                if (account.id == "1") {
                    val price = getBasePrice(order.symbol)
                    val totalValueChange = order.quantity * price
                    
                    val newHoldings = account.holdings.toMutableList()
                    val existingHoldingIndex = newHoldings.indexOfFirst { it.symbol == order.symbol }
                    
                    if (order.action == TradeAction.BUY) {
                        if (existingHoldingIndex != -1) {
                            val h = newHoldings[existingHoldingIndex]
                            newHoldings[existingHoldingIndex] = h.copy(
                                shares = h.shares + order.quantity,
                                currentPrice = price
                            )
                        } else {
                            newHoldings.add(Holding(order.symbol, order.quantity, price, price))
                        }
                    } else if (order.action == TradeAction.SELL) {
                        if (existingHoldingIndex != -1) {
                            val h = newHoldings[existingHoldingIndex]
                            val finalShares = h.shares - order.quantity
                            if (finalShares <= 0) {
                                newHoldings.removeAt(existingHoldingIndex)
                            } else {
                                newHoldings[existingHoldingIndex] = h.copy(shares = finalShares, currentPrice = price)
                            }
                        }
                    }

                    val newBalance = newHoldings.sumOf { it.shares * it.currentPrice }
                    account.copy(
                        balance = newBalance,
                        holdings = newHoldings,
                        dayChange = newBalance - 11150.00, // Relative to updated cost basis (450 * 24.0 + 50 * 7.0)
                        dayChangePercent = ((newBalance - 11150.00) / 11150.00) * 100
                    )
                } else account
            }
        }
        return Result.success(Unit)
    }

    private fun createMockQuote(symbol: String): StockQuote {
        val basePrice = getBasePrice(symbol)
        val randomVar = Random.nextDouble(-1.5, 1.5)
        val lastPrice = basePrice + randomVar
        
        return StockQuote(
            symbol = symbol,
            companyName = getCompanyName(symbol),
            lastPrice = lastPrice,
            change = randomVar,
            changePercent = (randomVar / basePrice) * 100,
            bid = lastPrice - 0.05,
            ask = lastPrice + 0.05,
            volume = 45000000L + Random.nextLong(1000000L),
            marketStatus = MarketStatus.OPEN,
            history = createMockHistory(basePrice)
        )
    }

    private fun getBasePrice(symbol: String) = when(symbol) {
        "AAPL" -> 228.22
        "MSFT" -> 416.32
        "GOOGL" -> 165.45
        "AMZN" -> 188.10
        "NVDA" -> 118.20
        "TSLA" -> 245.50
        "META" -> 530.10
        "VOO" -> 515.20
        "SCHD" -> 84.15
        "QQQ" -> 485.60
        "DIA" -> 412.30
        "SPX" -> 5620.10
        "DJIA" -> 41390.50
        "IXIC" -> 17680.20
        "BRK.B" -> 465.10
        "JPM" -> 210.40
        "UNH" -> 585.20
        "V" -> 280.15
        "JNJ" -> 165.30
        "WMT" -> 78.45
        "PG" -> 170.20
        "eAPRK" -> 29.647598173 // Updated base price to 29.647598173
        else -> 100.00
    }

    private fun getCompanyName(symbol: String) = when(symbol) {
        "AAPL" -> "Apple Inc."
        "MSFT" -> "Microsoft Corp."
        "GOOGL" -> "Alphabet Inc."
        "AMZN" -> "Amazon.com Inc."
        "NVDA" -> "NVIDIA Corp."
        "TSLA" -> "Tesla, Inc."
        "META" -> "Meta Platforms, Inc."
        "VOO" -> "Vanguard S&P 500 ETF"
        "SCHD" -> "Schwab US Dividend Equity ETF"
        "QQQ" -> "Invesco QQQ Trust"
        "DIA" -> "SPDR Dow Jones Industrial Average ETF"
        "SPX" -> "S&P 500 Index"
        "DJIA" -> "Dow Jones Industrial Average"
        "IXIC" -> "NASDAQ Composite"
        "BRK.B" -> "Berkshire Hathaway Inc."
        "JPM" -> "JPMorgan Chase & Co."
        "UNH" -> "UnitedHealth Group Inc."
        "V" -> "Visa Inc."
        "JNJ" -> "Johnson & Johnson"
        "WMT" -> "Walmart Inc."
        "PG" -> "Procter & Gamble Co."
        "eAPRK" -> "eAPRK Corp"
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
            points.add(ChartPoint(now - (50 - i) * 3600000L, open, high, low, close, 1000000L + Random.nextLong(500000L)))
            currentPrice = close
        }
        return points
    }
}
