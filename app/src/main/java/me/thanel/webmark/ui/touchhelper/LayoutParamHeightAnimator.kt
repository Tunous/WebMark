package me.thanel.webmark.ui.touchhelper

import android.animation.ValueAnimator
import android.view.View
import androidx.core.view.updateLayoutParams

class LayoutParamHeightAnimator(
    private val target: View,
    vararg values: Int
) : ValueAnimator() {

    init {
        setIntValues(*values)

        addUpdateListener {
            val value = it.animatedValue as Int
            target.updateLayoutParams {
                height = value
            }
        }
    }

    companion object {
        fun collapse(target: View) = LayoutParamHeightAnimator(target, target.height, 0)
    }
}
