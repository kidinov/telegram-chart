package kidinov.telegram.chart.util

import android.view.MotionEvent
import android.view.View
import kidinov.telegram.chart.ChartView

const val NAB_BORDER_THRESHOLD = 6

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
                if (event.y !in viewTop..viewBottom) return@with

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
                when (navDrag) {
                    NavDrag.LEFT ->
                        if (event.x <= right - NAB_BORDER_THRESHOLD.px && event.x > viewLeft)
                            view.onNavigationChanged(event.x, right)
                    NavDrag.RIGHT ->
                        if (event.x >= left + NAB_BORDER_THRESHOLD.px && event.x < viewRight)
                            view.onNavigationChanged(left, event.x)
                    NavDrag.BOTH -> {
                        if (prevX > 0) {
                            val dif = event.x - prevX
                            if ((left > viewLeft || dif > 0) &&
                                (right < viewRight || dif < 0)
                            ) {
                                view.onNavigationChanged(left + dif, right + dif)
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
