package kidinov.telegram.chart

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.ViewGroup
import kidinov.telegram.chart.model.Chart
import kidinov.telegram.chart.util.Drawer
import kidinov.telegram.chart.util.PlacementCalculator

class ChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    private lateinit var drawer: Drawer

    private lateinit var data: Chart

    init {
        setWillNotDraw(false)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        drawer = Drawer(PlacementCalculator(context))
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        for (i in 0 until childCount) {
            getChildAt(i).layout(left, top, right, bottom)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (!::data.isInitialized) return

    }

    fun setChartData(data: Chart) {
        this.data = data
    }
}