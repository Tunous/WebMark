package me.thanel.webmark.ui.list

import android.view.View
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_webmark.*
import me.thanel.recyclerviewutils.viewholder.BaseItemViewBinder
import me.thanel.webmark.R
import me.thanel.webmark.data.Webmark
import me.thanel.webmark.ext.glideVisibilityListener
import me.thanel.webmark.ext.openInBrowser
import me.thanel.webmark.ext.roundedCorners

class WebmarkViewBinder : BaseItemViewBinder<Webmark, WebmarkViewHolder>(R.layout.item_webmark) {

    private var imageCornerRadius: Int = 0

    init {
        onItemClickListener = { view, item ->
            view.context.openInBrowser(item.url)
        }
    }

    override fun onCreateViewHolder(itemView: View) = WebmarkViewHolder(itemView)

    override fun onInflateViewHolder(holder: WebmarkViewHolder) {
        super.onInflateViewHolder(holder)
        imageCornerRadius =
            holder.context.resources.getDimensionPixelOffset(R.dimen.webmark_image_corner_radius)
    }

    override fun onBindViewHolder(holder: WebmarkViewHolder, item: Webmark) {
        super.onBindViewHolder(holder, item)
        holder.bindTitle(item)
        holder.bindDetails(item)
        holder.bindFavicon(item)
    }

    override fun onBindViewHolder(holder: WebmarkViewHolder, item: Webmark, payloads: List<Any>) {
        super.onBindViewHolder(holder, item, payloads)
        handleEnumPayloadChanges<WebmarkChange>(payloads) {
            when (it) {
                WebmarkChange.Title -> holder.bindTitle(item)
                WebmarkChange.Favicon -> holder.bindFavicon(item)
                WebmarkChange.Details -> holder.bindDetails(item)
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
        val faviconUrl = item.faviconUrl
        webmarkImageView.isVisible = false
        Glide.with(webmarkImageView)
            .load(faviconUrl)
            .roundedCorners(imageCornerRadius)
            .listener(webmarkImageView.glideVisibilityListener)
            .into(webmarkImageView)
    }
}
