package kidinov.telegram.chart.util

import kidinov.telegram.chart.model.Button
import kidinov.telegram.chart.model.Chart
import kidinov.telegram.chart.renderer.BUTTON_MARGIN

const val BUTTON_RAD = 16

class ButtonsCalculator(private val placementCalculator: PlacementCalculator) {
    fun calculateButtons(data: Chart) =
        data.lines.mapIndexed { index, line ->
            val cx = placementCalculator.convert(
                (index * (BUTTON_RAD.px * 2 + BUTTON_MARGIN.px) + BUTTON_MARGIN.px).toFloat(),
                Area.BUTTONS,
                Side.LEFT
            )
            val cy = placementCalculator.convert(
                (BUTTON_RAD.px).toFloat(),
                Area.BUTTONS,
                Side.TOP
            )
            Button(line.name, cx, cy, BUTTON_RAD.px, line.color, true)
        }
}