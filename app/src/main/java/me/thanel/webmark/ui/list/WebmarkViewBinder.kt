package me.thanel.webmark.ui.list

import android.net.Uri
import android.util.Log
import kotlinx.android.synthetic.main.item_webmark.*
import me.thanel.recyclerviewutils.viewholder.BaseItemViewBinder
import me.thanel.recyclerviewutils.viewholder.ContainerViewHolder
import me.thanel.webmark.R
import me.thanel.webmark.data.Webmark
import me.thanel.webmark.ext.openInBrowser

class WebmarkViewBinder : BaseItemViewBinder<Webmark>(R.layout.item_webmark) {

    init {
        onItemClickListener = { view, item ->
            val uri = Uri.parse(item.link)
            view.context.openInBrowser(uri)
        }
    }

    var markAsDone: (Long) -> Unit = {}

    override fun onInflateViewHolder(holder: ContainerViewHolder) {
        super.onInflateViewHolder(holder)

        holder.markAsDoneButton.setOnClickListener {
            val item = it.tag as Webmark
            markAsDone(item.id)
        }
    }

    private fun <E : Enum<E>> handleEnumPayloadChanges(payloads: List<Any>, block: (E) -> Unit) {
        for (payload in payloads) {
            @Suppress("UNCHECKED_CAST")
            val changes = payload as? List<E>
            if (changes == null) {
                Log.w("RecyclerViewUtils", "Unsupported payload type: $payload")
                continue
            }
            changes.forEach(block)
        }
    }

    override fun onBindViewHolder(holder: ContainerViewHolder, item: Webmark, payloads: List<Any>) {
        super.onBindViewHolder(holder, item, payloads)
        holder.itemView.setTag(R.id.bound_item, item)

        handleEnumPayloadChanges<WebmarkChange>(payloads) {
            when (it) {
                WebmarkChange.Title -> holder.bindTitle(item)
            }
        }
    }

    override fun onBindViewHolder(holder: ContainerViewHolder, item: Webmark) {
        super.onBindViewHolder(holder, item)
        holder.bindTitle(item)
        holder.bindLink(item)
        holder.markAsDoneButton.tag = item
    }

    private fun ContainerViewHolder.bindTitle(item: Webmark) {
        webmarkTitleTextView.text = item.title ?: item.link
    }

    private fun ContainerViewHolder.bindLink(item: Webmark) {
        val uri = Uri.parse(item.link)
        val shortLink = uri.host?.removePrefix("www.")
        webmarkLinkTextView.text = shortLink
    }
}
