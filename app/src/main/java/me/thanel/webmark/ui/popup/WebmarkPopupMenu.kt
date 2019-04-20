package me.thanel.webmark.ui.popup

import com.github.zawadz88.materialpopupmenu.MaterialPopupMenuBuilder
import com.github.zawadz88.materialpopupmenu.popupMenu
import me.thanel.webmark.action.WebmarkActionHandler
import me.thanel.webmark.data.Webmark
import me.thanel.webmark.data.ext.isRead
import me.thanel.webmark.ui.ext.header
import me.thanel.webmark.ui.ext.item
import me.thanel.webmark.ui.touchhelper.WebmarkAction

fun createPopupMenu(webmark: Webmark, actionHandler: WebmarkActionHandler) = when (webmark.isRead) {
    true -> createPopupMenuForArchived(webmark, actionHandler)
    else -> createPopupMenuForUnarchived(webmark, actionHandler)
}

private fun createPopupMenuForUnarchived(webmark: Webmark, actionHandler: WebmarkActionHandler) =
    popupMenu {
        section {
            header(webmark.title ?: webmark.url.toString())
            item(webmark, WebmarkAction.Archive, actionHandler)
            item(webmark, WebmarkAction.Delete, actionHandler)
            item(webmark, WebmarkAction.ShareLink, actionHandler)
        }
    }

private fun createPopupMenuForArchived(webmark: Webmark, actionHandler: WebmarkActionHandler) =
    popupMenu {
        section {
            header(webmark.title ?: webmark.url.toString())
            item(webmark, WebmarkAction.Unarchive, actionHandler)
            item(webmark, WebmarkAction.Delete, actionHandler)
            item(webmark, WebmarkAction.ShareLink, actionHandler)
        }
    }

fun MaterialPopupMenuBuilder.SectionHolder.item(
    webmark: Webmark,
    action: WebmarkAction,
    actionHandler: WebmarkActionHandler
) = item(action.labelResId, action.iconResId) { actionHandler.performAction(action, webmark) }
