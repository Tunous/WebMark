package me.thanel.webmark.ui.ext

import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import com.github.zawadz88.materialpopupmenu.MaterialPopupMenuBuilder
import me.thanel.webmark.R

fun MaterialPopupMenuBuilder.SectionHolder.header(title: String) = customItem(R.layout.view_popup_header) { view ->
    val headerView = view.findViewById<TextView>(R.id.popupMenuHeaderTextView)
    headerView.text = title
}

fun MaterialPopupMenuBuilder.SectionHolder.item(labelRes: Int, @DrawableRes iconRes: Int, callback: () -> Unit) = item {
    this.labelRes = labelRes
    this.icon = iconRes
    this.callback = callback
}

fun MaterialPopupMenuBuilder.SectionHolder.customItem(@LayoutRes layoutResId: Int, viewBoundCallback: (View) -> Unit) = customItem {
    this.layoutResId = layoutResId
    dismissOnSelect = false
    this.viewBoundCallback = viewBoundCallback
}
