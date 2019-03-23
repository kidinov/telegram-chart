package kidinov.telegram.chart.util

import android.graphics.Rect

const val LINES_AREA_HEIGHT = 64
const val NAVIGATION_AREA_HEIGHT = 64
const val BORDER_MARGIN = 8
const val BETWEEN_MARGIN = 16
const val LEGEND_MARGIN = 20

enum class Side {
    LEFT, TOP
}

enum class Area {
    CHART, CHART_X_LEGEND, CHART_Y_LEGEND, NAV, BUTTONS
}

class PlacementCalculator(
    private val width: Int,
    private val height: Int
) {
    val chartAreaRect: Rect
        get() = Rect(
            0,
            BORDER_MARGIN.px,
            width,
            chartBottom - LEGEND_MARGIN.px
        )
    val chartYLegendRect: Rect
        get() = Rect(
            chartAreaRect.left + BORDER_MARGIN.px,
            chartAreaRect.top + LEGEND_MARGIN.px,
            chartAreaRect.right,
            chartAreaRect.bottom + LEGEND_MARGIN.px
        )

    private val chartXLegendRect: Rect
        get() = Rect(
            chartYLegendRect.left + LEGEND_MARGIN.px,
            chartAreaRect.top,
            chartAreaRect.right,
            chartAreaRect.bottom + LEGEND_MARGIN.px * 2
        )

    val navAreaRect: Rect
        get() = Rect(
            BORDER_MARGIN.px,
            chartBottom + BETWEEN_MARGIN + LEGEND_MARGIN.px,
            width - BORDER_MARGIN.px,
            navAreaBottom
        )

    private val buttonsAreaRect: Rect
        get() = Rect(
            BORDER_MARGIN.px,
            navAreaBottom + BETWEEN_MARGIN,
            width - BORDER_MARGIN.px,
            navAreaBottom + LINES_AREA_HEIGHT.px
        )

    fun convert(value: Float, area: Area, side: Side): Float {
        fun doConvert(rect: Rect, side: Side) =
            if (side == Side.LEFT) rect.left + value else rect.top + value
        return when (area) {
            Area.CHART -> doConvert(chartAreaRect, side)
            Area.CHART_X_LEGEND -> doConvert(chartXLegendRect, side)
            Area.CHART_Y_LEGEND -> doConvert(chartYLegendRect, side)
            Area.NAV -> doConvert(navAreaRect, side)
            Area.BUTTONS -> doConvert(buttonsAreaRect, side)
        }
    }

    fun calculateXPosition(x: Long, minMaxX: Pair<Long, Long>, area: Area): Float {
        val dif = minMaxX.second - minMaxX.first
        val xDif = x - minMaxX.first
        return when (area) {
            Area.CHART -> (chartAreaRect.left + chartAreaRect.width().toFloat() / dif * xDif)
            Area.CHART_X_LEGEND -> (chartXLegendRect.left + chartXLegendRect.width().toFloat() / dif * xDif)
            Area.CHART_Y_LEGEND -> (chartYLegendRect.left + chartYLegendRect.width().toFloat() / dif * xDif)
            Area.NAV -> (navAreaRect.left + navAreaRect.width().toFloat() / dif * xDif)
            Area.BUTTONS -> (buttonsAreaRect.left + navAreaRect.width().toFloat() / dif * xDif)
        }
    }

    fun calculateYPosition(y: Long, minMaxY: Pair<Long, Long>, area: Area): Float {
        val dif = minMaxY.second - minMaxY.first
        val yDif = y - minMaxY.first
        return when (area) {
            Area.CHART -> (chartAreaRect.bottom - chartAreaRect.height().toFloat() / dif * yDif)
            Area.CHART_X_LEGEND -> (chartXLegendRect.bottom - chartXLegendRect.height().toFloat() / dif * yDif)
            Area.CHART_Y_LEGEND -> (chartYLegendRect.bottom - chartYLegendRect.height().toFloat() / dif * yDif)
            Area.NAV -> (navAreaRect.bottom - navAreaRect.height().toFloat() / dif * yDif)
            Area.BUTTONS -> (buttonsAreaRect.bottom - navAreaRect.height().toFloat() / dif * yDif)
        }
    }

    private val chartBottom: Int
        get() = height - LINES_AREA_HEIGHT.px - NAVIGATION_AREA_HEIGHT.px - BETWEEN_MARGIN.px

    private val navAreaBottom: Int
        get() = chartBottom + NAVIGATION_AREA_HEIGHT.px + BETWEEN_MARGIN.px
}