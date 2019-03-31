package me.thanel.webmark.ui.list

import android.view.View
import kotlinx.android.synthetic.main.item_webmark.view.*
import me.thanel.recyclerviewutils.viewholder.ContainerViewHolder
import me.thanel.webmark.ui.touchhelper.SwipeableViewHolder

class WebmarkViewHolder(containerView: View) :
    ContainerViewHolder(containerView),
    SwipeableViewHolder {

    override val swipeableView: View = containerView.contentView
}
