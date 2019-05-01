package me.thanel.webmark.ui.list.item.webmark

import android.view.View
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.item_webmark.*
import me.thanel.recyclerviewutils.viewholder.BaseItemViewBinder
import me.thanel.webmark.R
import me.thanel.webmark.action.WebmarkActionHandler
import me.thanel.webmark.data.Webmark
import me.thanel.webmark.ext.openInBrowser
import me.thanel.webmark.model.WebmarkChange
import me.thanel.webmark.ui.imageloader.ImageLoader
import me.thanel.webmark.ui.popup.createPopupMenu

class WebmarkViewBinder(
    private val actionHandler: WebmarkActionHandler,
    private val imageLoader: ImageLoader
) : BaseItemViewBinder<Webmark, WebmarkViewHolder>(R.layout.item_webmark) {

    private var imageCornerRadius = 0
    private var faviconCornerRadius = 0

    var onLongClickListener: (View, Webmark) -> Unit = { view, item ->
        createPopupMenu(item, actionHandler).show(view.context, view)
    }

    init {
        onItemClickListener = { view, item ->
            view.context.openInBrowser(item.url)
        }
    }

    override fun onCreateViewHolder(itemView: View) =
        WebmarkViewHolder(itemView)

    override fun onInflateViewHolder(holder: WebmarkViewHolder) {
        super.onInflateViewHolder(holder)
        imageCornerRadius =
            holder.context.resources.getDimensionPixelOffset(R.dimen.webmark_image_corner_radius)
        faviconCornerRadius =
            holder.context.resources.getDimensionPixelOffset(R.dimen.webmark_favicon_corner_radius)
        holder.itemView.setOnLongClickListener {
            onLongClickListener(it, it.getTag(R.id.bound_item) as Webmark)
            return@setOnLongClickListener true
        }
    }

    override fun onBindViewHolder(holder: WebmarkViewHolder, item: Webmark) {
        super.onBindViewHolder(holder, item)
        holder.shouldAnimateCollapsing = false
        holder.bindTitle(item)
        holder.bindDetails(item)
        holder.bindFavicon(item)
        holder.bindImage(item)
    }

    override fun onBindViewHolder(holder: WebmarkViewHolder, item: Webmark, payloads: List<Any>) {
        super.onBindViewHolder(holder, item, payloads)
        holder.shouldAnimateCollapsing = false
        handleEnumPayloadChanges<WebmarkChange>(payloads) {
            when (it) {
                WebmarkChange.Title -> holder.bindTitle(item)
                WebmarkChange.Favicon -> holder.bindFavicon(item)
                WebmarkChange.Details -> holder.bindDetails(item)
                WebmarkChange.Image -> holder.bindImage(item)
            }
        }
    }

    private fun WebmarkViewHolder.bindTitle(item: Webmark) {
        webmarkTitleTextView.text = item.title ?: item.url.toString()
    }

    private fun WebmarkViewHolder.bindDetails(item: Webmark) {
        var details = item.url.host?.removePrefix("www.") ?: ""
        if (item.estimatedReadingTimeMinutes > 0) {
            if (details.isNotEmpty()) {
                details += " ⸱ "
            }
            details += "${item.estimatedReadingTimeMinutes} min"
        }
        webmarkLinkTextView.text = details
    }

    private fun WebmarkViewHolder.bindFavicon(item: Webmark) {
        webmarkFaviconView.isVisible = false
        if (item.faviconUrl != null) {
            imageLoader.loadImage(webmarkFaviconView, item.faviconUrl, faviconCornerRadius) {
                webmarkFaviconView.isVisible = true
            }
        } else {
            imageLoader.clearImage(webmarkFaviconView)
        }
    }

    private fun WebmarkViewHolder.bindImage(item: Webmark) {
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
