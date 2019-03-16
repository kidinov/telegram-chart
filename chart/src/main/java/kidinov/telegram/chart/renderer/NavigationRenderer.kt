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

class NavigationRenderer(
    private val placementCalculator: PlacementCalculator,
    private val chartCalculator: ChartCalculator,
    private val chartAnimator: ChatAnimator
) {
    private var minMaxX: Pair<Long, Long>? = null
    private var minMaxY: Pair<Long, Long>? = null

    private val path = Path()

    private lateinit var data: Chart

    private val bcgPaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.FILL
        color = Color.rgb(246, 249, 255)
    }

    private val linePaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.STROKE
        strokeWidth = (1.px * 1.5).toFloat()
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    fun drawNavigation(c: Canvas) {
        if (!::data.isInitialized) return

        c.drawRect(placementCalculator.navAreaRect, bcgPaint)

        linesToRender(data).forEach { line ->
            line.coordinates.asIterable().forEachIndexed { index, coordinate ->
                val x = placementCalculator.calculateXPosition(coordinate.key, minMaxX!!, Area.NAV)
                val y = placementCalculator.calculateYPosition(
                    coordinate.value,
                    minMaxY!!,
                    Area.NAV
                )

                val yAnimated = y + (placementCalculator.navAreaRect.bottom - y) * (1 - chartAnimator.multiplierY)
                if (index == 0) path.moveTo(x, yAnimated)
                else path.lineTo(x, yAnimated)
            }
            linePaint.apply { color = adjustAlpha(line.color, chartAnimator.alpha) }
            c.drawPath(path, linePaint)
            path.reset()
        }
    }

    fun dataChanged(data: Chart) {
        this.data = data
        minMaxX = chartCalculator.calcXMinMax(linesToRender(data).map { it.coordinates })
        minMaxY = chartCalculator.calcYMinMax(linesToRender(data).map { it.coordinates })
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