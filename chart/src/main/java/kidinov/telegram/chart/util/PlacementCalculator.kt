package kidinov.telegram.chart.util

import android.graphics.Rect

const val LINES_AREA_HEIGHT = 64
const val NAVIGATION_AREA_HEIGHT = 64
const val MARGIN = 8

class PlacementCalculator(
    private val height: Int,
    private val width: Int
) {

    val chartAreaRect: Rect
        get() = Rect(
            MARGIN.px,
            MARGIN.px,
            width - MARGIN.px,
            chartBottom
        )

    val calcNavAreaRect: Rect
        get() = Rect(
            MARGIN.px,
            chartBottom + MARGIN.px,
            width - MARGIN.px,
            navAreaBottom
        )

    val calcLinesAreaRect: Rect
        get() = Rect(
            MARGIN.px,
            navAreaBottom + MARGIN.px,
            width - MARGIN.px,
            navAreaBottom + LINES_AREA_HEIGHT.px
        )

    private val chartBottom: Int
        get() = height - LINES_AREA_HEIGHT.px - NAVIGATION_AREA_HEIGHT.px - MARGIN.px

    private val navAreaBottom: Int
        get() = chartBottom - NAVIGATION_AREA_HEIGHT.px - MARGIN.px
}