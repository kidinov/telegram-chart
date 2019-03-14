package kidinov.telegram.chart.renderer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import kidinov.telegram.chart.model.Chart
import kidinov.telegram.chart.util.Area
import kidinov.telegram.chart.util.MARGIN
import kidinov.telegram.chart.util.PlacementCalculator
import kidinov.telegram.chart.util.Side
import kidinov.telegram.chart.util.px

class ButtonsRenderer(private val placementCalculator: PlacementCalculator) : Renderer {
    private val buttonPaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.FILL
        setShadowLayer(2.px.toFloat(), 0f, 2.px.toFloat(), Color.DKGRAY)
    }
    private val checkPaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.STROKE
        color = Color.GREEN
        strokeWidth = 4.px.toFloat()
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    private var checkPath = Path()

    fun drawButtons(c: Canvas, data: Chart) {
        data.lines.forEachIndexed { index, line ->
            drawButton(c, line.color, index)
        }
    }

    private fun drawButton(c: Canvas, color: Int, index: Int) {
        val cx = placementCalculator.convert(
            (index * (BUTTON_SIZE.px + BUTTON_MARGIN.px) + BUTTON_MARGIN.px).toFloat(),
            Area.BUTTONS,
            Side.LEFT
        )
        val cy = placementCalculator.convert(
            (BUTTON_SIZE.px / 2).toFloat(),
            Area.BUTTONS,
            Side.TOP
        )
        c.drawCircle(
            cx,
            cy,
            BUTTON_SIZE.px.toFloat() / 2,
            buttonPaint.apply { this.color = color }
        )

        drawCheck(cx, cy, c)
    }

    private fun drawCheck(cx: Float, cy: Float, c: Canvas) {
        checkPath.reset()
        checkPath.moveTo(cx - MARGIN.px, cy - MARGIN.px)
        checkPath.lineTo(cx, cy + MARGIN.px)
        checkPath.lineTo(cx + MARGIN.px, cy - MARGIN.px)
        c.drawPath(checkPath, checkPaint)
    }

    override fun dataChanged() {
    }
}