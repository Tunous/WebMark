package me.thanel.webmark.ui.list

import androidx.recyclerview.widget.DiffUtil
import me.thanel.webmark.data.Webmark

object WebmarkItemCallback : DiffUtil.ItemCallback<Webmark>() {
    override fun areItemsTheSame(oldItem: Webmark, newItem: Webmark) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Webmark, newItem: Webmark) =
        oldItem == newItem

    override fun getChangePayload(oldItem: Webmark, newItem: Webmark): Any? =
        mutableListOf<WebmarkChange>()
            .addIf(WebmarkChange.Title) { oldItem.title != newItem.title }
            .addIf(WebmarkChange.Favicon) { oldItem.faviconUrl != newItem.faviconUrl }

    private fun <E> MutableList<E>.addIf(item: E, condition: () -> Boolean): MutableList<E> {
        if (condition()) {
            add(item)
        }
        return this
    }
}
