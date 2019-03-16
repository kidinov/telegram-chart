package kidinov.telegram.chart.renderer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import kidinov.telegram.chart.model.Chart
import kidinov.telegram.chart.util.Area
import kidinov.telegram.chart.util.ChartCalculator
import kidinov.telegram.chart.util.ChatAnimator
import kidinov.telegram.chart.util.PlacementCalculator
import kidinov.telegram.chart.util.px

const val LAGEND_LINE_MARGIN = 6

class ChartRenderer(
    private val placementCalculator: PlacementCalculator,
    private val chartCalculator: ChartCalculator,
    private val chartAnimator: ChatAnimator
) {
    private lateinit var minMaxX: Pair<Long, Long>
    private lateinit var minMaxY: Pair<Long, Long>

    private var leftProp: Float = 0f
    private var rightProp: Float = 1f

    private val path = Path()

    private lateinit var data: Chart

    private val legentPaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.FILL
        color = Color.LTGRAY
    }

    private val linePaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.STROKE
        strokeWidth = (1.px * 1.5).toFloat()
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    fun drawChart(c: Canvas) {
        if (!::data.isInitialized) return

        drawLegend(c)

//        linesToRender(data).forEach { line ->
//            line.coordinates.asIterable().forEachIndexed { index, coordinate ->
//                val x = placementCalculator.calculateXPosition(coordinate.key, minMaxX, Area.NAV)
//                val y = placementCalculator.calculateYPosition(
//                    coordinate.value,
//                    minMaxY,
//                    Area.NAV
//                )
//
//                val yAnimated = y + (placementCalculator.navAreaRect.bottom - y) * (1 - chartAnimator.multiplierY)
//                if (index == 0) path.moveTo(x, yAnimated)
//                else path.lineTo(x, yAnimated)
//            }
//            linePaint.apply { color = adjustAlpha(line.color, chartAnimator.alpha) }
//            c.drawPath(path, linePaint)
//            path.reset()
//        }
    }

    fun dataChanged(data: Chart) {
        this.data = data
        minMaxX = chartCalculator.calcXMinMax(linesToRender(data).map { it.coordinates })
        minMaxY = chartCalculator.calcYMinMax(linesToRender(data).map { it.coordinates })
    }

    fun windowChanged(leftProp: Float, rightProp: Float) {
        if (!::data.isInitialized) return

        this.leftProp = leftProp
        this.rightProp = rightProp
    }

    private fun drawLegend(c: Canvas) {
        val diff = minMaxY.second - minMaxY.first

        for (i in 1..6) {
            val value = minMaxY.first + diff / i
            val x = placementCalculator.calculateXPosition(minMaxX.first, minMaxX, Area.CHART)
            val y = placementCalculator.calculateXPosition(value, minMaxY, Area.CHART)
            c.drawText(value.toString(), x, y, legentPaint)
            c.drawLine(
                x,
                y + LAGEND_LINE_MARGIN.px.toLong(),
                x + placementCalculator.chartAreaRect.width(),
                y + LAGEND_LINE_MARGIN.px.toLong(),
                legentPaint
            )
        }
    }

    private fun adjustAlpha(color: Int, factor: Float): Int {
        val alpha = Math.round(Color.alpha(color) * factor)
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

    private fun linesToRender(data: Chart) = data.lines.filter { it.toRender }
}