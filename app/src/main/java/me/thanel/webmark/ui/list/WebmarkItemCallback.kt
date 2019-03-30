package me.thanel.webmark.ui.list

import androidx.recyclerview.widget.DiffUtil
import me.thanel.webmark.data.Webmark

object WebmarkItemCallback : DiffUtil.ItemCallback<Webmark>() {
    override fun areItemsTheSame(oldItem: Webmark, newItem: Webmark) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Webmark, newItem: Webmark) =
        oldItem == newItem

    override fun getChangePayload(oldItem: Webmark, newItem: Webmark): Any? {
        val changes = mutableListOf<WebmarkChange>()
        if (oldItem.title != newItem.title) {
            changes.add(WebmarkChange.Title)
        }
        return changes
    }
}
