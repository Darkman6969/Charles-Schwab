package com.example.charlesschwab.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class StockQuote(
    val symbol: String,
    val companyName: String,
    val lastPrice: Double,
    val change: Double,
    val changePercent: Double,
    val bid: Double,
    val ask: Double,
    val volume: Long,
    val marketStatus: MarketStatus = MarketStatus.OPEN,
    val afterHoursPrice: Double? = null,
    val history: List<ChartPoint> = emptyList()
)

@Serializable
enum class MarketStatus {
    OPEN, CLOSED, AFTER_HOURS
}

@Serializable
data class ChartPoint(
    val timestamp: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Long
)

@Serializable
data class Account(
    val id: String,
    val name: String,
    val maskedId: String, // e.g. *1234
    val balance: Double,
    val dayChange: Double,
    val dayChangePercent: Double,
    val holdings: List<Holding> = emptyList()
)

@Serializable
data class Holding(
    val symbol: String,
    val shares: Double,
    val averagePrice: Double,
    val currentPrice: Double
)

@Serializable
data class OrderTicket(
    val symbol: String,
    val action: TradeAction,
    val quantity: Double,
    val isQuantityInDollars: Boolean,
    val orderType: OrderType,
    val limitPrice: Double? = null,
    val stopPrice: Double? = null,
    val timing: OrderTiming,
    val routing: OrderRouting = OrderRouting.SMART
)

enum class TradeAction {
    BUY, SELL, SELL_SHORT, BUY_TO_COVER
}

enum class OrderType {
    MARKET, LIMIT, STOP, STOP_LIMIT, TRAILING_STOP
}

enum class OrderTiming {
    DAY, GTC, FOK
}

enum class OrderRouting {
    SMART, DIRECT
}
