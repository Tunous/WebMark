package me.thanel.webmark.ui.touchhelper

import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.ItemTouchHelper
import me.thanel.webmark.R
import me.thanel.webmark.data.Webmark
import me.thanel.webmark.data.ext.isRead

enum class WebmarkAction(
    @AttrRes val colorAttrId: Int,
    @DrawableRes val iconResId: Int
) {
    Archive(R.attr.colorActionArchive, R.drawable.ic_archive),
    Delete(R.attr.colorActionDelete, R.drawable.ic_delete),
    Unarchive(R.attr.colorActionUnarchive, R.drawable.ic_unarchive);

    companion object {
        fun leftSwipeFor(item: Webmark) = when {
            item.isRead -> Unarchive
            else -> null
        }

        fun rightSwipeFor(item: Webmark) = when {
            item.isRead -> Delete
            else -> Archive
        }

        fun swipeInDirectionFor(direction: Int, item: Webmark) = when (direction) {
            ItemTouchHelper.RIGHT -> rightSwipeFor(item)
            else -> leftSwipeFor(item)
        }
    }
}
