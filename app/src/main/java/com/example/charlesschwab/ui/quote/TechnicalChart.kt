package com.example.charlesschwab.ui.quote

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.charlesschwab.domain.model.ChartPoint
import com.example.charlesschwab.ui.theme.BearishRed
import com.example.charlesschwab.ui.theme.BullishGreen
import kotlin.math.abs

@Composable
fun TechnicalChart(
    points: List<ChartPoint>,
    modifier: Modifier = Modifier
) {
    if (points.isEmpty()) return

    var selectedPointIndex by remember { mutableStateOf<Int?>(null) }
    
    val minPrice = points.minOf { it.low }
    val maxPrice = points.maxOf { it.high }
    val priceRange = maxPrice - minPrice
    
    val maxVolume = points.maxOf { it.volume }.toFloat()

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            selectedPointIndex = calculateIndex(offset.x, size.width, points.size)
                        },
                        onDrag = { change, _ ->
                            selectedPointIndex = calculateIndex(change.position.x, size.width, points.size)
                        },
                        onDragEnd = { selectedPointIndex = null },
                        onDragCancel = { selectedPointIndex = null }
                    )
                }
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        selectedPointIndex = calculateIndex(offset.x, size.width, points.size)
                    }
                }
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val candleWidth = canvasWidth / points.size
            
            // Draw Chart Background/Grid (Optional)
            
            points.forEachIndexed { index, point ->
                val x = index * candleWidth + (candleWidth * 0.1f)
                val currentCandleWidth = candleWidth * 0.8f
                
                // Scale Price to Canvas Height (leaving room for volume)
                val chartHeight = canvasHeight * 0.8f
                val volumeHeight = canvasHeight * 0.2f
                
                fun priceToY(price: Double): Float {
                    return (chartHeight - ((price - minPrice) / priceRange * chartHeight)).toFloat()
                }
                
                val openY = priceToY(point.open)
                val closeY = priceToY(point.close)
                val highY = priceToY(point.high)
                val lowY = priceToY(point.low)
                
                val isBullish = point.close >= point.open
                val color = if (isBullish) BullishGreen else BearishRed
                
                // Draw Wick
                drawLine(
                    color = color,
                    start = Offset(x + currentCandleWidth / 2, highY),
                    end = Offset(x + currentCandleWidth / 2, lowY),
                    strokeWidth = 1.dp.toPx()
                )
                
                // Draw Body
                drawRect(
                    color = color,
                    topLeft = Offset(x, if (isBullish) closeY else openY),
                    size = Size(currentCandleWidth, abs(openY - closeY).coerceAtLeast(1f))
                )
                
                // Draw Volume Bar
                val volBarHeight = (point.volume.toFloat() / maxVolume) * volumeHeight
                drawRect(
                    color = color.copy(alpha = 0.3f),
                    topLeft = Offset(x, canvasHeight - volBarHeight),
                    size = Size(currentCandleWidth, volBarHeight)
                )
            }
            
            // Draw Crosshair
            selectedPointIndex?.let { index ->
                if (index in points.indices) {
                    val point = points[index]
                    val x = index * candleWidth + candleWidth / 2
                    val chartHeight = canvasHeight * 0.8f
                    val priceY = (chartHeight - ((point.close - minPrice) / priceRange * chartHeight)).toFloat()
                    
                    drawLine(
                        color = Color.Gray,
                        start = Offset(x, 0f),
                        end = Offset(x, canvasHeight),
                        strokeWidth = 1.dp.toPx()
                    )
                    
                    drawLine(
                        color = Color.Gray,
                        start = Offset(0f, priceY),
                        end = Offset(canvasWidth, priceY),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }
        }
    }
}

private fun calculateIndex(x: Float, width: Float, count: Int): Int {
    return ((x / width) * count).toInt().coerceIn(0, count - 1)
}
