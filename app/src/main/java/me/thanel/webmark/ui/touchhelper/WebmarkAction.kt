package me.thanel.webmark.ui.touchhelper

import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.ItemTouchHelper
import me.thanel.webmark.R
import me.thanel.webmark.data.Webmark
import me.thanel.webmark.data.ext.isArchived

enum class WebmarkAction(
    @StringRes val labelResId: Int,
    @AttrRes val colorAttrId: Int,
    @DrawableRes val iconResId: Int
) {
    Archive(R.string.action_archive, R.attr.colorActionArchive, R.drawable.ic_archive),
    Delete(R.string.action_delete, R.attr.colorActionDelete, R.drawable.ic_delete),
    ExtractDetails(R.string.action_extract_details, R.attr.colorActionUnavailable, R.drawable.ic_extract_details),
    ShareLink(R.string.action_share_link, R.attr.colorActionUnavailable, R.drawable.ic_share),
    Unarchive(R.string.action_unarchive, R.attr.colorActionUnarchive, R.drawable.ic_unarchive);

    companion object {
        fun leftSwipeFor(item: Webmark) = when {
            item.isArchived -> Unarchive
            else -> null
        }

        fun rightSwipeFor(item: Webmark) = when {
            item.isArchived -> Delete
            else -> Archive
        }

        fun swipeInDirectionFor(direction: Int, item: Webmark) = when (direction) {
            ItemTouchHelper.END -> rightSwipeFor(item)
            else -> leftSwipeFor(item)
        }
    }
}
