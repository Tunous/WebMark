package me.thanel.webmark.ui.list

import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.GravityCompat
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.ItemTouchHelper
import kotlinx.android.synthetic.main.item_webmark.*
import me.thanel.recyclerviewutils.viewholder.ContainerViewHolder
import me.thanel.webmark.R
import me.thanel.webmark.data.Webmark
import me.thanel.webmark.data.ext.isRead
import me.thanel.webmark.ext.getColorFromAttr
import me.thanel.webmark.ui.touchhelper.SwipeableViewHolder
import me.thanel.webmark.ui.touchhelper.WebmarkAction

class WebmarkViewHolder(containerView: View) :
    ContainerViewHolder(containerView),
    SwipeableViewHolder {

    private val swipeElevation = context.resources.getDimension(R.dimen.swipe_elevation)
    private val boundItem get() = containerView.getTag(R.id.bound_item) as Webmark

    override var shouldAnimateCollapsing: Boolean = false

    override fun getSwipeDirs() = when {
        boundItem.isRead -> ItemTouchHelper.START or ItemTouchHelper.END
        else -> ItemTouchHelper.END
    }

    override fun onSwipe(distance: Float) {
        contentView.translationX = distance
        contentView.translationZ = if (distance != 0f) swipeElevation else 0f

        if (distance == 0f) return

        val item = boundItem
        val isSwipingRight = distance > 0f
        updateBackgroundColor(item, isSwipingRight)
        updateActionIcon(item, isSwipingRight)
    }

    override fun onClearSwipe() {
        onSwipe(0f)
    }

    private fun updateActionIcon(item: Webmark, isSwipingRight: Boolean) {
        val action = when {
            isSwipingRight -> WebmarkAction.rightSwipeFor(item)
            else -> WebmarkAction.leftSwipeFor(item)
        }
        val iconResId = action?.iconResId
        if (iconResId != null) {
            swipeActionIconView.setImageResource(iconResId)
        } else {
            swipeActionIconView.setImageDrawable(null)
        }

        swipeActionIconView.updateLayoutParams<FrameLayout.LayoutParams> {
            gravity = if (isSwipingRight) {
                GravityCompat.START or Gravity.CENTER_VERTICAL
            } else {
                GravityCompat.END or Gravity.CENTER_VERTICAL
            }
        }
    }

    private fun updateBackgroundColor(item: Webmark, isSwipingRight: Boolean) {
        val action = when {
            isSwipingRight -> WebmarkAction.rightSwipeFor(item)
            else -> WebmarkAction.leftSwipeFor(item)
        }
        val colorAttrId = action?.colorAttrId ?: R.attr.colorActionUnarchive
        containerView.setBackgroundColor(context.getColorFromAttr(colorAttrId))
    }
}
