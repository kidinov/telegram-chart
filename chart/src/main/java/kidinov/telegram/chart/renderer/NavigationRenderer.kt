package kidinov.telegram.chart.renderer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import kidinov.telegram.chart.model.Chart
import kidinov.telegram.chart.util.Area
import kidinov.telegram.chart.util.ChartCalculator
import kidinov.telegram.chart.util.PlacementCalculator
import kidinov.telegram.chart.util.px

class NavigationRenderer(
    private val placementCalculator: PlacementCalculator,
    private val chartCalculator: ChartCalculator
) : Renderer {
    private var minMaxX: Pair<Long, Long>? = null
    private var minMaxY: Pair<Long, Long>? = null

    private val path = Path()

    private val bcgPaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.FILL
        color = Color.LTGRAY
    }

    private val linePaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.STROKE
        strokeWidth = 1.px.toFloat()
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    fun drawNavigation(c: Canvas, data: Chart) {
        if (minMaxX == null || minMaxY == null) {
            minMaxX = chartCalculator.calcXMinMax(data.lines.map { it.coordinates })
            minMaxY = chartCalculator.calcYMinMax(data.lines.map { it.coordinates })
        }

        c.drawRect(placementCalculator.calcNavAreaRect, bcgPaint)

        data.lines.forEach { line ->
            linePaint.apply { color = line.color }
            line.coordinates.asIterable().forEachIndexed { index, coordinate ->
                val x = placementCalculator.calculateXPosition(coordinate.key, minMaxX!!, Area.NAV)
                val y = placementCalculator.calculateYPosition(coordinate.value, minMaxY!!, Area.NAV)
                println("x = $x y = $y")
                if (index == 0) path.moveTo(x, y)
                else path.lineTo(x, y)
            }
            c.drawPath(path, linePaint)
            path.reset()
        }
    }

    override fun dataChanged() {
        minMaxX = null
        minMaxY = null
    }
}