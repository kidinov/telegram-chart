package kidinov.telegram.chart.renderer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import kidinov.telegram.chart.model.Button
import kidinov.telegram.chart.util.px

class ButtonsRenderer {
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

    private var buttons: List<Button> = emptyList()

    fun drawButtons(c: Canvas) {
        buttons.forEach { button -> drawButton(c, button) }
    }

    private fun drawButton(c: Canvas, button: Button) {
        c.drawCircle(
            button.cx,
            button.cy,
            button.rad.toFloat(),
            buttonPaint.apply { this.color = button.color }
        )

        if (button.checked) drawCheck(button.cx, button.cy, c)
    }

    private fun drawCheck(cx: Float, cy: Float, c: Canvas) {
        checkPath.reset()
        checkPath.moveTo(cx, cy)
        checkPath.lineTo(cx, cy)
        checkPath.lineTo(cx, cy)
        c.drawPath(checkPath, checkPaint)
    }

    fun dataChanged(buttons: List<Button>) {
        this.buttons = buttons
    }
}