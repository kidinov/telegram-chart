package kidinov.telegram.chart.renderer

import android.graphics.Canvas
import kidinov.telegram.chart.model.Button
import kidinov.telegram.chart.model.Chart
import kidinov.telegram.chart.model.NavigationControl
import kidinov.telegram.chart.util.ChartCalculator
import kidinov.telegram.chart.util.ChatAnimator
import kidinov.telegram.chart.util.PlacementCalculator

const val BUTTON_MARGIN = 12

class GeneralRenderer(
    placementCalculator: PlacementCalculator,
    chartAnimator: ChatAnimator
) {
    private val chartCalculator = ChartCalculator(placementCalculator)
    private val buttonsRenderer = ButtonsRenderer()
    private val navigationRenderer =
        NavigationRenderer(placementCalculator, chartCalculator, chartAnimator)
    private val chartRenderer = ChartRenderer(placementCalculator, chartCalculator, chartAnimator)

    fun draw(c: Canvas) {
        buttonsRenderer.drawButtons(c)
        navigationRenderer.drawNavigation(c)
        chartRenderer.drawChart(c)
    }

    fun dataChanged(data: Chart, buttons: List<Button>) {
        navigationRenderer.dataChanged(data)
        buttonsRenderer.dataChanged(buttons)
        chartRenderer.dataChanged(data)
    }

    fun navigationChanged(navControl: NavigationControl) {
        navigationRenderer.windowChanged(navControl)
        chartRenderer.windowChanged(navControl)
    }
}