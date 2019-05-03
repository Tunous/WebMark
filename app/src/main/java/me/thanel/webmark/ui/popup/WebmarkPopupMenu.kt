package me.thanel.webmark.ui.popup

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import androidx.core.view.isVisible
import com.github.zawadz88.materialpopupmenu.MaterialPopupMenuBuilder
import com.github.zawadz88.materialpopupmenu.ViewBoundCallback
import com.github.zawadz88.materialpopupmenu.popupMenu
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.thanel.webmark.R
import me.thanel.webmark.action.WebmarkAction
import me.thanel.webmark.action.WebmarkActionHandler
import me.thanel.webmark.data.BuildConfig
import me.thanel.webmark.data.Webmark
import me.thanel.webmark.data.ext.isArchived
import me.thanel.webmark.preferences.WebMarkPreferences
import me.thanel.webmark.share.QuickShareDetailsProvider
import me.thanel.webmark.share.share
import me.thanel.webmark.share.shareDirectly
import me.thanel.webmark.ui.ext.coloredHeader
import me.thanel.webmark.ui.ext.item

class WebMarkPopupMenu(
    private val quickShareDetailsProvider: QuickShareDetailsProvider
) {

    fun createPopupMenu(
        context: Context,
        webmark: Webmark,
        actionHandler: WebmarkActionHandler
    ) = popupMenu {
        style = R.style.Theme_WebMark_PopupMenu_DayNight_WithOffset
        section {
            coloredHeader(webmark.title ?: webmark.url.toString())

            val archiveAction =
                if (webmark.isArchived) WebmarkAction.Unarchive else WebmarkAction.Archive
            item(webmark, archiveAction, actionHandler)
            item(webmark, WebmarkAction.Delete, actionHandler)
            shareItem(context, webmark.url)
        }

        if (BuildConfig.DEBUG) {
            section {
                item(webmark, WebmarkAction.ExtractDetails, actionHandler)
            }
        }
    }

    private fun MaterialPopupMenuBuilder.SectionHolder.shareItem(
        context: Context,
        uri: Uri
    ) = customItem {
        layoutResId = R.layout.item_menu_share
        viewBoundCallback = ViewBoundCallback { view ->
            val quickShareIconView = view.findViewById<ImageView>(R.id.quickShareIconImageView)
            quickShareIconView.isVisible = false

            val component = WebMarkPreferences.latestShareComponent
            if (component != null) {
                GlobalScope.launch(Dispatchers.Main) {
                    val drawable = quickShareDetailsProvider.getQuickShareIconAsync(component)
                    if (drawable != null && quickShareIconView != null) {
                        quickShareIconView.setImageDrawable(drawable)
                        quickShareIconView.setOnClickListener {
                            context.shareDirectly(uri, component)
                            dismissPopup()
                        }
                        quickShareIconView.isVisible = true
                    }
                }
            }
        }
        callback = {
            context.share(uri)
        }
    }

    private fun MaterialPopupMenuBuilder.SectionHolder.item(
        webmark: Webmark,
        action: WebmarkAction,
        actionHandler: WebmarkActionHandler
    ) = item(action.labelResId, action.iconResId) { actionHandler.performAction(action, webmark) }
}
