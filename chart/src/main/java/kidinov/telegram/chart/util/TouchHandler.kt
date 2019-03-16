package kidinov.telegram.chart.util

import android.view.MotionEvent
import android.view.View
import kidinov.telegram.chart.ChartView

const val NAB_BORDER_THRESHOLD = 4

class TouchHandler(
    private val view: ChartView
) : View.OnTouchListener {

    private var navDrag = NavDrag.NONE

    private var prevX = -1f

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {

            resetState()

            view.buttons.forEach {
                if (event.x in (it.cx - it.rad)..(it.cx + it.rad) &&
                    event.y in (it.cy - it.rad)..(it.cy + it.rad)
                ) {
                    view.onButtonClicked(it)
                    return@forEach
                }
            }
        }

        with(view.navControl) {
            if (event.action == MotionEvent.ACTION_DOWN) {
                if (event.y !in top..bottom) return@with

                when {
                    event.x in (left - NAB_BORDER_THRESHOLD.px)..(left + NAB_BORDER_THRESHOLD.px) ->
                        navDrag = NavDrag.LEFT
                    event.x in (right - NAB_BORDER_THRESHOLD.px)..(right + NAB_BORDER_THRESHOLD.px) ->
                        navDrag = NavDrag.RIGHT
                    event.x in (left + NAB_BORDER_THRESHOLD.px)..(right - NAB_BORDER_THRESHOLD.px) ->
                        navDrag = NavDrag.BOTH
                }
            }

            if (event.action == MotionEvent.ACTION_MOVE && navDrag != NavDrag.NONE) {
                println("navDrag $navDrag left - $left right - $right")
                when (navDrag) {
                    NavDrag.LEFT ->
                        if (event.x <= right - NAB_BORDER_THRESHOLD.px)
                            view.onNavigationChanged(event.x, right)
                        else navDrag = NavDrag.NONE
                    NavDrag.RIGHT ->
                        if (event.x >= left + NAB_BORDER_THRESHOLD.px)
                            view.onNavigationChanged(left, event.x)
                        else navDrag = NavDrag.NONE
                    NavDrag.BOTH -> {
                        if (prevX > 0) {
                            val dif = event.x - prevX
                            println("dif $dif prevX - $prevX")
                            if ((left > NAB_BORDER_THRESHOLD.px || dif > 0) &&
                                (right < width - NAB_BORDER_THRESHOLD.px || dif < 0)
                            ) {
                                view.onNavigationChanged(left + dif, right + dif)
                            } else {
                                navDrag = NavDrag.NONE
                            }
                        }

                        prevX = event.x
                    }
                    else -> throw RuntimeException("wrong state")
                }
            }
        }

        return true
    }

    private fun resetState() {
        prevX = -1f
        navDrag = NavDrag.NONE
    }

    private enum class NavDrag {
        LEFT, RIGHT, BOTH, NONE
    }

}
