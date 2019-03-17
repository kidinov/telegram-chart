package kidinov.telegram.chart.renderer

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kidinov.telegram.chart.model.Button
import kidinov.telegram.chart.util.px

class ButtonsRenderer {
    private val buttonPaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.STROKE
        strokeWidth = 3.px.toFloat()
    }
    private val checkPaint = Paint().apply {
        flags = Paint.ANTI_ALIAS_FLAG
        style = Paint.Style.FILL
        color = Color.BLACK
    }

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

        if (button.checked) drawCheck(c, button)
    }

    private fun drawCheck(c: Canvas, button: Button) {
        c.drawCircle(button.cx, button.cy, button.rad.toFloat() / 3, checkPaint)
    }

    fun dataChanged(buttons: List<Button>) {
        this.buttons = buttons
    }
}