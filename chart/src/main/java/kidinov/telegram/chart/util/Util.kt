package kidinov.telegram.chart.util

import android.graphics.Color
import kidinov.telegram.chart.model.Chart

fun Chart.linesToRender() =
    lines.asSequence().filter { it.toRender }.toList()

fun Int.adjustAlpha(factor: Float): Int {
    val alpha = Math.round(Color.alpha(this) * factor)
    val red = Color.red(this)
    val green = Color.green(this)
    val blue = Color.blue(this)
    return Color.argb(alpha, red, green, blue)
}