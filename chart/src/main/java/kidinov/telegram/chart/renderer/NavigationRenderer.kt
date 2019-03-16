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
import kidinov.telegram.chart.util.adjustAlpha
import kidinov.telegram.chart.util.linesToRender
import kidinov.telegram.chart.util.px

class NavigationRenderer(
    private val placementCalculator: PlacementCalculator,
    private val chartCalculator: ChartCalculator,
    private val chartAnimator: ChatAnimator
) {
    private lateinit var minMaxX: Pair<Long, Long>
    private lateinit var minMaxY: Pair<Long, Long>

    private var left: Float = 300f
    private var right: Float = 700f

    private val navAreaRect by lazy { placementCalculator.navAreaRect }
    private val path = Path()

    private lateinit var data: Chart

    private val bcgPaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.FILL
        color = Color.argb(10, 220, 230, 240)
    }

    private val navFillPaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.FILL
        color = Color.argb(120, 220, 230, 240)
    }

    private val navLinesPaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.STROKE
        color = Color.argb(230, 220, 230, 240)
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

        c.drawRect(navAreaRect, bcgPaint)

        data.linesToRender().forEach { line ->
            line.coordinates.asIterable().forEachIndexed { index, coordinate ->
                val x = placementCalculator.calculateXPosition(coordinate.key, minMaxX, Area.NAV)
                val y = placementCalculator.calculateYPosition(
                    coordinate.value,
                    minMaxY,
                    Area.NAV
                )

                val yAnimated =
                    y + (navAreaRect.bottom - y) * (1 - chartAnimator.multiplierY)
                if (index == 0) path.moveTo(x, yAnimated)
                else path.lineTo(x, yAnimated)
            }
            linePaint.apply { color = line.color.adjustAlpha(chartAnimator.alpha) }
            c.drawPath(path, linePaint)
            path.reset()
        }

        drawNavigationControl(c)
    }

    private fun drawNavigationControl(c: Canvas) {
        if (left > 0) {
            c.drawRect(
                navAreaRect.left.toFloat(),
                navAreaRect.top.toFloat(),
                left,
                navAreaRect.bottom.toFloat(),
                navFillPaint
            )
        }
        if (right < navAreaRect.right) {
            c.drawRect(
                right,
                navAreaRect.top.toFloat(),
                navAreaRect.right.toFloat(),
                navAreaRect.bottom.toFloat(),
                navFillPaint
            )
        }

        navLinesPaint.apply { strokeWidth = 4.px.toFloat() }
        c.drawLine(left, navAreaRect.bottom.toFloat(), left, navAreaRect.top.toFloat(), navLinesPaint)
        c.drawLine(right, navAreaRect.bottom.toFloat(), right, navAreaRect.top.toFloat(), navLinesPaint)
        navLinesPaint.apply { strokeWidth = 2.px.toFloat() }
        c.drawLine(left, navAreaRect.top.toFloat(), right, navAreaRect.top.toFloat(), navLinesPaint)
        c.drawLine(left, navAreaRect.bottom.toFloat(), right, navAreaRect.bottom.toFloat(), navLinesPaint)
    }

    fun windowChanged(left: Float, right: Float) {
        this.left = left
        this.right = right
    }

    fun dataChanged(data: Chart) {
        this.data = data
        minMaxY = chartCalculator.calcYMinMax(data.linesToRender().map { it.coordinates })
        minMaxX = chartCalculator.calcXMinMax(data.linesToRender().map { it.coordinates })
    }
}