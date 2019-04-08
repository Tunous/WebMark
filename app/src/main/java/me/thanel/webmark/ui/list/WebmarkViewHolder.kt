package me.thanel.webmark.ui.list

import android.graphics.Color
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
import me.thanel.webmark.ui.touchhelper.SwipeableViewHolder

class WebmarkViewHolder(containerView: View) :
    ContainerViewHolder(containerView),
    SwipeableViewHolder {

    private val swipeElevation = context.resources.getDimension(R.dimen.swipe_elevation)
    private val boundItem get() = containerView.getTag(R.id.bound_item) as Webmark

    override fun getSwipeDirs() = when {
        boundItem.isRead -> ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        else -> ItemTouchHelper.RIGHT
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

    private fun updateActionIcon(boundItem: Webmark, isSwipingRight: Boolean) {
        val icon = when {
            isSwipingRight -> if (boundItem.isRead) R.drawable.ic_delete else R.drawable.ic_archive
            else -> if (boundItem.isRead) R.drawable.ic_archive else null
        }
        icon?.let(swipeActionIconView::setImageResource)

        swipeActionIconView.updateLayoutParams<FrameLayout.LayoutParams> {
            gravity = if (isSwipingRight) {
                GravityCompat.START or Gravity.CENTER_VERTICAL
            } else {
                GravityCompat.END or Gravity.CENTER_VERTICAL
            }
        }
    }

    private fun updateBackgroundColor(boundItem: Webmark, isSwipingRight: Boolean) {
        val colorAttrId = when {
            boundItem.isRead -> when {
                isSwipingRight -> R.attr.colorActionDelete
                else -> R.attr.colorActionUnarchive
            }
            else -> when {
                isSwipingRight -> R.attr.colorActionArchive
                else -> R.attr.colorActionUnavailable
            }
        }
        containerView.setBackgroundColor(context.getColorFromAttr(colorAttrId))
    }
}
