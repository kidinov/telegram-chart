package kidinov.telegram.chart.util

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.animation.AccelerateInterpolator

const val ANIMATION_DURATION = 150L

class ChatAnimator(updateListener: ValueAnimator.AnimatorUpdateListener) {
    var multiplierY = 1f
    var alpha = 1f

    private val animator by lazy {
        val yAnim = ObjectAnimator.ofFloat(this, "multiplierY", 0f, 1f).apply {
            interpolator = AccelerateInterpolator()
            duration = ANIMATION_DURATION
            addUpdateListener(updateListener)
        }
        val alphaAnim = ObjectAnimator.ofFloat(this, "alpha", 0f, 1f).apply {
            duration = ANIMATION_DURATION
        }
        AnimatorSet().apply {
            playTogether(yAnim, alphaAnim)
        }
    }

    fun animate() {
        animator.start()
    }
}