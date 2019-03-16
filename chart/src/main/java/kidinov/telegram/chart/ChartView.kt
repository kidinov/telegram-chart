package kidinov.telegram.chart

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.ViewGroup
import kidinov.telegram.chart.model.Button
import kidinov.telegram.chart.model.Chart
import kidinov.telegram.chart.model.NavigationControl
import kidinov.telegram.chart.renderer.GeneralRenderer
import kidinov.telegram.chart.util.ButtonsCalculator
import kidinov.telegram.chart.util.ChatAnimator
import kidinov.telegram.chart.util.PlacementCalculator
import kidinov.telegram.chart.util.TouchHandler

class ChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    private lateinit var renderer: GeneralRenderer
    private lateinit var buttonsCalculator: ButtonsCalculator
    private val chartAnimator = ChatAnimator(
        ValueAnimator.AnimatorUpdateListener {
            postInvalidate()
        }
    )
    private val touchConverter = TouchHandler(this)

    var buttons: List<Button> = emptyList()
    lateinit var navControl: NavigationControl
    private lateinit var data: Chart

    init {
        setWillNotDraw(false)
        setOnTouchListener(touchConverter)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val placementCalculator = PlacementCalculator(w, h)
        renderer = GeneralRenderer(placementCalculator, chartAnimator)
        buttonsCalculator = ButtonsCalculator(placementCalculator)

        navControl = NavigationControl(
            (w / 2).toFloat(),
            (w / 3 * 2).toFloat(),
            placementCalculator.navAreaRect.top.toFloat(),
            placementCalculator.navAreaRect.bottom.toFloat(),
            placementCalculator.navAreaRect.width().toFloat()
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        for (i in 0 until childCount) {
            getChildAt(i).layout(left, top, right, bottom)
        }
    }

    override fun onDraw(canvas: Canvas) {
        renderer.draw(canvas)
    }


    fun setChartData(data: Chart) {
        this.buttons = buttonsCalculator.calculateButtons(data)
        this.data = data
        renderer.navigationChanged(navControl)
        doRedraw()
    }

    private fun doRedraw() {
        renderer.dataChanged(data, buttons)
        chartAnimator.animate()
    }

    fun onButtonClicked(button: Button) {
        val changedButton = buttons.first { it == button }
        changedButton.checked = !changedButton.checked
        data.lines.first { it.name == changedButton.lineName }.toRender = changedButton.checked

        doRedraw()
    }

    fun onNavigationChanged(left: Float, right: Float) {
        renderer.navigationChanged(navControl.apply { this.left = left; this.right = right })
        postInvalidate()
    }
}