package kidinov.telegram.chart.util

import android.view.MotionEvent
import android.view.View
import kidinov.telegram.chart.ChartView

class TouchConverter(
    private val view: ChartView
) : View.OnTouchListener {

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        if (event.action != MotionEvent.ACTION_UP) return true

        view.buttons.forEach {
            if (event.x in (it.cx - it.rad)..(it.cx + it.rad) &&
                event.y in (it.cy - it.rad)..(it.cy + it.rad)
            ) {
                view.onButtonClicked(it)
                return@forEach
            }
        }
        return true
    }

}
