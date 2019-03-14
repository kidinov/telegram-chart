package kidinov.telegram.chart.renderer

import android.graphics.Canvas
import kidinov.telegram.chart.model.Chart
import kidinov.telegram.chart.util.ChartCalculator
import kidinov.telegram.chart.util.PlacementCalculator

const val BUTTON_SIZE = 32
const val BUTTON_MARGIN = 12

class GeneralRenderer(placementCalculator: PlacementCalculator) : Renderer {
    private val chartCalculator = ChartCalculator()
    private val buttonsRenderer = ButtonsRenderer(placementCalculator)
    private val navigationRenderer = NavigationRenderer(placementCalculator, chartCalculator)

    fun draw(c: Canvas, data: Chart) {
        buttonsRenderer.drawButtons(c, data)
        navigationRenderer.drawNavigation(c, data)
    }

    override fun dataChanged() {
        buttonsRenderer.dataChanged()
        navigationRenderer.dataChanged()
    }
}