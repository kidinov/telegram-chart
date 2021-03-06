package kidinov.telegram.chart.renderer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import kidinov.telegram.chart.model.Chart
import kidinov.telegram.chart.model.NavigationControl
import kidinov.telegram.chart.util.Area
import kidinov.telegram.chart.util.ChartCalculator
import kidinov.telegram.chart.util.ChatAnimator
import kidinov.telegram.chart.util.PlacementCalculator
import kidinov.telegram.chart.util.adjustAlpha
import kidinov.telegram.chart.util.linesToRender
import kidinov.telegram.chart.util.px
import java.text.SimpleDateFormat

const val LEGEND_Y_LINE_MARGIN = 6
const val LEGEND_TEXT_SIZE = 14
const val LEGEND_ITEMS = 6
val LEGEND_COLOR = Color.argb(255, 200, 200, 200)

class ChartRenderer(
    private val placementCalculator: PlacementCalculator,
    private val chartCalculator: ChartCalculator,
    private val chartAnimator: ChatAnimator
) {
    private lateinit var minMaxX: Pair<Long, Long>
    private lateinit var minMaxY: Pair<Long, Long>

    private val dateFormatter = SimpleDateFormat("MMM dd")

    private val path = Path()

    private lateinit var data: Chart

    private val legendPaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.FILL
        color = LEGEND_COLOR
        textSize = LEGEND_TEXT_SIZE.px.toFloat()
    }

    private val linePaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.STROKE
        strokeWidth = 2.px.toFloat()
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    fun drawChart(c: Canvas) {
        if (!::data.isInitialized) return

        drawLegend(c)
        drawLines(c)
    }

    private fun drawLines(c: Canvas) {
        data.linesToRender().forEach { line ->
            line.coordinatesArea.asIterable().forEachIndexed { index, coordinate ->
                val x = placementCalculator.calculateXPosition(coordinate.key, minMaxX, Area.CHART)
                val y = placementCalculator.calculateYPosition(
                    coordinate.value,
                    minMaxY,
                    Area.CHART
                )

                val yAnimated =
                    y + (placementCalculator.chartAreaRect.bottom - y) * (1 - chartAnimator.multiplierY)
                if (index == 0) path.moveTo(x, yAnimated)
                else path.lineTo(x, yAnimated)
            }
            linePaint.apply { color = line.color.adjustAlpha(chartAnimator.alpha) }
            c.drawPath(path, linePaint)
            path.reset()
        }
    }

    fun dataChanged(data: Chart) {
        this.data = data
        minMaxX = chartCalculator.calcXMinMax(data.linesToRender().map { it.coordinatesArea })
        minMaxY = chartCalculator.calcYMinMax(data.linesToRender().map { it.coordinatesArea })
    }

    fun windowChanged(navControl: NavigationControl) {
        if (!::data.isInitialized) return

        data.linesToRender().forEach { chartCalculator.setRangeToShow(it, navControl) }
        minMaxX = chartCalculator.calcXMinMax(data.linesToRender().map { it.coordinatesArea })
        minMaxY = chartCalculator.calcYMinMax(data.linesToRender().map { it.coordinatesArea })
    }

    private fun drawLegend(c: Canvas) {
        drawXLegend(c)
        drawYLegend(c)
    }

    private fun drawXLegend(c: Canvas) {
        val diff = (getMaxX() - getMinX()) / LEGEND_ITEMS

        var value = getMaxX()
        for (i in 1..LEGEND_ITEMS) {
            value -= diff
            val x = placementCalculator.calculateXPosition(
                value,
                getMinX() to getMaxX(),
                Area.CHART_X_LEGEND
            )
            val y = placementCalculator.calculateYPosition(
                getMinX(),
                getMinX() to getMaxX(),
                Area.CHART_X_LEGEND
            )
            c.drawText(
                dateFormatter.format(value),
                x,
                y,
                legendPaint.apply { color = LEGEND_COLOR.adjustAlpha(chartAnimator.alpha) }
            )
        }
    }

    private fun drawYLegend(c: Canvas) {
        val difVal = (getMaxY() - getMinY()) / (LEGEND_ITEMS - 1)
        val difPx = Math.abs(
            placementCalculator.calculateYPosition(
                getMaxY(),
                getMinY() to getMaxY(),
                Area.CHART_Y_LEGEND
            ) - placementCalculator.calculateYPosition(
                getMinY(),
                getMinY() to getMaxY(),
                Area.CHART_Y_LEGEND
            )
        ) / (LEGEND_ITEMS - 1)

        var valuePx = placementCalculator.chartYLegendRect.bottom.toFloat()
        val x = placementCalculator.calculateXPosition(
            getMinX(),
            getMinX() to getMaxX(),
            Area.CHART_Y_LEGEND
        )

        var value = getMinY()
        for (i in 1..LEGEND_ITEMS) {
            val yAnimated =
                valuePx + (placementCalculator.chartYLegendRect.bottom - valuePx) * (1 - chartAnimator.multiplierY)
            c.drawText(value.toString(), x, yAnimated, legendPaint)
            c.drawLine(
                x,
                yAnimated + LEGEND_Y_LINE_MARGIN.px.toLong(),
                x + placementCalculator.chartYLegendRect.width(),
                yAnimated + LEGEND_Y_LINE_MARGIN.px.toLong(),
                legendPaint.apply { color = LEGEND_COLOR.adjustAlpha(chartAnimator.alpha) }
            )
            valuePx -= difPx
            value += difVal
        }
    }

    private fun getMinX() = minMaxX.first
    private fun getMaxX() = minMaxX.second

    private fun getMinY() = minMaxY.first
    private fun getMaxY() = minMaxY.second
}