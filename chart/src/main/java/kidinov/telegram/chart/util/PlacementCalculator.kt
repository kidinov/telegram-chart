package kidinov.telegram.chart.util

import android.graphics.Rect

const val LINES_AREA_HEIGHT = 64
const val NAVIGATION_AREA_HEIGHT = 64
const val MARGIN = 8


enum class Side {
    LEFT, TOP
}

enum class Area {
    CHART, NAV, BUTTONS
}

class PlacementCalculator(
    private val width: Int,
    private val height: Int
) {
    val chartAreaRect: Rect
        get() = Rect(
            MARGIN.px,
            MARGIN.px,
            width - MARGIN.px,
            chartBottom
        )

    val navAreaRect: Rect
        get() = Rect(
            MARGIN.px,
            chartBottom + MARGIN.px,
            width - MARGIN.px,
            navAreaBottom
        )

    private val buttonsAreaRect: Rect
        get() = Rect(
            MARGIN.px,
            navAreaBottom + MARGIN.px,
            width - MARGIN.px,
            navAreaBottom + LINES_AREA_HEIGHT.px
        )

    fun convert(value: Float, area: Area, side: Side): Float {
        fun doConvert(rect: Rect, side: Side) =
            if (side == Side.LEFT) rect.left + value else rect.top + value
        return when (area) {
            Area.CHART -> doConvert(chartAreaRect, side)
            Area.NAV -> doConvert(navAreaRect, side)
            Area.BUTTONS -> doConvert(buttonsAreaRect, side)
        }
    }

    fun calculateXPosition(x: Long, minMaxX: Pair<Long, Long>, area: Area): Float {
        val dif = minMaxX.second - minMaxX.first
        val xDif = x - minMaxX.first
        return when (area) {
            Area.CHART -> (chartAreaRect.left + chartAreaRect.width().toFloat() / dif * xDif)
            Area.NAV -> (navAreaRect.left + navAreaRect.width().toFloat() / dif * xDif)
            Area.BUTTONS -> (buttonsAreaRect.left + navAreaRect.width().toFloat() / dif * xDif)
        }
    }

    fun calculateYPosition(y: Long, minMaxY: Pair<Long, Long>, area: Area): Float {
        val dif = minMaxY.second - minMaxY.first
        val yDif = y - minMaxY.first
        return when (area) {
            Area.CHART -> (chartAreaRect.bottom - chartAreaRect.height().toFloat() / dif * yDif)
            Area.NAV -> (navAreaRect.bottom - navAreaRect.height().toFloat() / dif * yDif)
            Area.BUTTONS -> (buttonsAreaRect.bottom - navAreaRect.height().toFloat() / dif * yDif)
        }
    }

    private val chartBottom: Int
        get() = height - LINES_AREA_HEIGHT.px - NAVIGATION_AREA_HEIGHT.px - MARGIN.px

    private val navAreaBottom: Int
        get() = chartBottom + NAVIGATION_AREA_HEIGHT.px + MARGIN.px
}