package me.thanel.webmark.ui.list.item.webmark

import android.view.View
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.item_webmark.*
import me.thanel.recyclerviewutils.viewholder.BaseItemViewBinder
import me.thanel.webmark.R
import me.thanel.webmark.data.Webmark
import me.thanel.webmark.model.WebMarkChange
import me.thanel.webmark.share.openInBrowser
import me.thanel.webmark.ui.imageloader.ImageLoader

class WebMarkViewBinder(
    private val imageLoader: ImageLoader
) : BaseItemViewBinder<Webmark, WebMarkViewHolder>(R.layout.item_webmark) {

    private var imageCornerRadius = 0
    private var faviconCornerRadius = 0

    var onLongClickListener: ((View, Webmark) -> Unit)? = null

    init {
        onItemClickListener = { view, item ->
            view.context.openInBrowser(item.url)
        }
    }

    override fun onCreateViewHolder(itemView: View) =
        WebMarkViewHolder(itemView)

    override fun onInflateViewHolder(holder: WebMarkViewHolder) {
        super.onInflateViewHolder(holder)
        imageCornerRadius =
            holder.context.resources.getDimensionPixelOffset(R.dimen.webmark_image_corner_radius)
        faviconCornerRadius =
            holder.context.resources.getDimensionPixelOffset(R.dimen.webmark_favicon_corner_radius)
        holder.itemView.setOnLongClickListener {
            onLongClickListener?.invoke(it, it.getTag(R.id.bound_item) as Webmark)
            return@setOnLongClickListener true
        }
    }

    override fun onBindViewHolder(holder: WebMarkViewHolder, item: Webmark) {
        super.onBindViewHolder(holder, item)
        holder.shouldAnimateCollapsing = false
        holder.bindTitle(item)
        holder.bindDetails(item)
        holder.bindFavicon(item)
        holder.bindImage(item)
    }

    override fun onBindViewHolder(holder: WebMarkViewHolder, item: Webmark, payloads: List<Any>) {
        super.onBindViewHolder(holder, item, payloads)
        holder.shouldAnimateCollapsing = false
        handleEnumPayloadChanges<WebMarkChange>(payloads) {
            when (it) {
                WebMarkChange.Title -> holder.bindTitle(item)
                WebMarkChange.Favicon -> holder.bindFavicon(item)
                WebMarkChange.Details -> holder.bindDetails(item)
                WebMarkChange.Image -> holder.bindImage(item)
            }
        }
    }

    private fun WebMarkViewHolder.bindTitle(item: Webmark) {
        webmarkTitleTextView.text = item.title ?: item.url.toString()
    }

    private fun WebMarkViewHolder.bindDetails(item: Webmark) {
        var details = item.url.host?.removePrefix("www.") ?: ""
        if (item.estimatedReadingTimeMinutes > 0) {
            if (details.isNotEmpty()) {
                details += " ⸱ "
            }
            details += "${item.estimatedReadingTimeMinutes} min"
        }
        webmarkLinkTextView.text = details
    }

    private fun WebMarkViewHolder.bindFavicon(item: Webmark) {
        webmarkFaviconView.isVisible = false
        if (item.faviconUrl != null) {
            imageLoader.loadImage(webmarkFaviconView, item.faviconUrl, faviconCornerRadius) {
                webmarkFaviconView.isVisible = true
            }
        } else {
            imageLoader.clearImage(webmarkFaviconView)
        }
    }

    private fun WebMarkViewHolder.bindImage(item: Webmark) {
        webmarkImageView.isVisible = false
        if (item.imageUrl != null) {
            imageLoader.loadImage(webmarkImageView, item.imageUrl, imageCornerRadius) {
                webmarkImageView.isVisible = true
            }
        } else {
            imageLoader.clearImage(webmarkImageView)
        }
    }
}
