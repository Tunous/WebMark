package me.thanel.webmark.model

import androidx.recyclerview.widget.DiffUtil
import me.thanel.webmark.data.Webmark
import me.thanel.webmark.ext.addIf

object WebmarkItemCallback : DiffUtil.ItemCallback<Webmark>() {
    override fun areItemsTheSame(oldItem: Webmark, newItem: Webmark) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Webmark, newItem: Webmark) =
        oldItem.id == newItem.id &&
                oldItem.url == newItem.url &&
                oldItem.title == newItem.title &&
                oldItem.faviconUrl == newItem.faviconUrl &&
                oldItem.estimatedReadingTimeMinutes == newItem.estimatedReadingTimeMinutes &&
                oldItem.archivedAt == newItem.archivedAt &&
                oldItem.savedAt == newItem.savedAt &&
                oldItem.markedForDeletion == newItem.markedForDeletion &&
                oldItem.imageUrl == newItem.imageUrl

    override fun getChangePayload(oldItem: Webmark, newItem: Webmark): Any? =
        mutableListOf<WebmarkChange>()
            .addIf(WebmarkChange.Title) { oldItem.title != newItem.title }
            .addIf(WebmarkChange.Favicon) { oldItem.faviconUrl != newItem.faviconUrl }
            .addIf(WebmarkChange.Details) { oldItem.url != newItem.url || oldItem.estimatedReadingTimeMinutes != newItem.estimatedReadingTimeMinutes }
            .addIf(WebmarkChange.Image) { oldItem.imageUrl != newItem.imageUrl }
}
