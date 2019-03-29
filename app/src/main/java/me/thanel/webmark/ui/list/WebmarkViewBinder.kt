package me.thanel.webmark.ui.list

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_webmark.view.*
import me.drakeet.multitype.ItemViewBinder
import me.thanel.webmark.R
import me.thanel.webmark.data.Webmark
import me.thanel.webmark.ext.openInBrowser

class WebmarkViewBinder : ItemViewBinder<Webmark, WebmarkViewBinder.WebmarkViewHolder>() {

    var markAsDone: (Long) -> Unit = {}

    override fun onBindViewHolder(holder: WebmarkViewHolder, item: Webmark) {
        val uri = Uri.parse(item.link)
        val displayUrl = uri.host?.removePrefix("www.")

        holder.itemView.webmarkTitleTextView.text = item.title ?: item.link
        holder.itemView.webmarkLinkTextView.text = displayUrl
        holder.itemView.setOnClickListener {
            it.context.openInBrowser(uri)
        }
        holder.itemView.markAsDoneButton.setOnClickListener {
            markAsDone(item.id)
        }
    }

    override fun onCreateViewHolder(
        inflater: LayoutInflater,
        parent: ViewGroup
    ) = WebmarkViewHolder(inflater.inflate(R.layout.item_webmark, parent, false))

    class WebmarkViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
