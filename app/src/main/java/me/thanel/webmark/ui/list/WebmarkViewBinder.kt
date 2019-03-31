package me.thanel.webmark.ui.list

import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_webmark.*
import me.thanel.recyclerviewutils.viewholder.ContainerViewHolder
import me.thanel.recyclerviewutils.viewholder.SimpleItemViewBinder
import me.thanel.webmark.R
import me.thanel.webmark.data.Webmark
import me.thanel.webmark.ext.glideVisibilityListener
import me.thanel.webmark.ext.openInBrowser
import me.thanel.webmark.ext.roundedCorners

class WebmarkViewBinder : SimpleItemViewBinder<Webmark>(R.layout.item_webmark) {

    private var imageCornerRadius: Int = 0

    init {
        onItemClickListener = { view, item ->
            view.context.openInBrowser(item.url)
        }
    }

    var markAsDone: (Long) -> Unit = {}

    override fun onInflateViewHolder(holder: ContainerViewHolder) {
        super.onInflateViewHolder(holder)
        imageCornerRadius =
            holder.context.resources.getDimensionPixelOffset(R.dimen.webmark_image_corner_radius)
        holder.markAsDoneButton.setOnClickListener {
            val item = it.tag as Webmark
            markAsDone(item.id)
        }
    }

    override fun onBindViewHolder(holder: ContainerViewHolder, item: Webmark) {
        super.onBindViewHolder(holder, item)
        holder.bindTitle(item)
        holder.bindDetails(item)
        holder.bindFavicon(item)
        holder.markAsDoneButton.tag = item
    }

    override fun onBindViewHolder(holder: ContainerViewHolder, item: Webmark, payloads: List<Any>) {
        super.onBindViewHolder(holder, item, payloads)
        handleEnumPayloadChanges<WebmarkChange>(payloads) {
            when (it) {
                WebmarkChange.Title -> holder.bindTitle(item)
                WebmarkChange.Favicon -> holder.bindFavicon(item)
                WebmarkChange.Details -> holder.bindDetails(item)
            }
        }
    }

    private fun ContainerViewHolder.bindTitle(item: Webmark) {
        webmarkTitleTextView.text = item.title ?: item.url.toString()
    }

    private fun ContainerViewHolder.bindDetails(item: Webmark) {
        var details = item.url.host?.removePrefix("www.") ?: ""
        if (item.estimatedReadingTimeMinutes > 0) {
            if (details.isNotEmpty()) {
                details += " ⸱ "
            }
            details += "${item.estimatedReadingTimeMinutes} min"
        }
        webmarkLinkTextView.text = details
    }

    private fun ContainerViewHolder.bindFavicon(item: Webmark) {
        val faviconUrl = item.faviconUrl
        webmarkImageView.isVisible = false
        Glide.with(webmarkImageView)
            .load(faviconUrl)
            .roundedCorners(imageCornerRadius)
            .listener(webmarkImageView.glideVisibilityListener)
            .into(webmarkImageView)
    }
}
