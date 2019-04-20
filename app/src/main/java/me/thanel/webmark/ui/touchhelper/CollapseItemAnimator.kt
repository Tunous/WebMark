package me.thanel.webmark.ui.touchhelper

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class CollapseItemAnimator : DefaultItemAnimator() {

    private val removeAnimations = mutableListOf<Animator>()

    override fun canReuseUpdatedViewHolder(viewHolder: RecyclerView.ViewHolder): Boolean {
        val swipeableViewHolder = viewHolder as? SwipeableViewHolder
        val shouldAnimateCollapsing = swipeableViewHolder?.shouldAnimateCollapsing ?: false
        return !shouldAnimateCollapsing && super.canReuseUpdatedViewHolder(viewHolder)
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder): Boolean {
        val swipeableViewHolder = holder as? SwipeableViewHolder
        val shouldAnimateCollapsing = swipeableViewHolder?.shouldAnimateCollapsing ?: false
        if (!shouldAnimateCollapsing) {
            return super.animateRemove(holder)
        }

        val itemView = holder.itemView
        val set = AnimatorSet()

        val animateHeight = LayoutParamHeightAnimator.collapse(itemView).apply {
            duration = removeDuration
            interpolator = COLLAPSE_INTERPOLATOR
        }
        set.play(animateHeight)

        val animateAlpha = ObjectAnimator().apply {
            target = itemView
            setProperty(View.ALPHA)
            setFloatValues(1f, 0f)
        }
        set.play(animateAlpha)

        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator?) {
                dispatchRemoveStarting(holder)
            }

            override fun onAnimationCancel(animation: Animator?) {
                resetView()
            }

            override fun onAnimationEnd(animation: Animator?) {
                set.removeListener(this)
                resetView()

                dispatchRemoveFinished(holder)
                removeAnimations.remove(set)
                if (!isRunning) {
                    dispatchAnimationsFinished()
                }
            }

            private fun resetView() {
                itemView.updateLayoutParams {
                    height = ViewGroup.LayoutParams.WRAP_CONTENT
                }
                itemView.alpha = 1f
            }
        })

        set.start()

        removeAnimations.add(set)

        return false
    }

    override fun isRunning(): Boolean {
        return super.isRunning() || removeAnimations.isNotEmpty()
    }

    companion object {
        private val COLLAPSE_INTERPOLATOR = AccelerateInterpolator(3f)
    }
}
