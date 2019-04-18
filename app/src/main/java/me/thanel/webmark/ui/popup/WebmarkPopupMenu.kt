package me.thanel.webmark.ui.popup

import com.github.zawadz88.materialpopupmenu.popupMenu
import me.thanel.webmark.R
import me.thanel.webmark.action.WebmarkActionHandler
import me.thanel.webmark.data.Webmark
import me.thanel.webmark.data.ext.isRead
import me.thanel.webmark.ui.ext.header
import me.thanel.webmark.ui.ext.item

fun createPopupMenu(webmark: Webmark, actionHandler: WebmarkActionHandler) = when (webmark.isRead) {
    true -> createPopupMenuForArchived(webmark, actionHandler)
    else -> createPopupMenuForUnarchived(webmark, actionHandler)
}

private fun createPopupMenuForUnarchived(webmark: Webmark, actionHandler: WebmarkActionHandler) =
    popupMenu {
        section {
            header(webmark.title ?: webmark.url.toString())
            item(R.string.action_archive, R.drawable.ic_archive) { actionHandler.archive(webmark) }
            item(R.string.action_delete, R.drawable.ic_delete) { actionHandler.delete(webmark) }
            item(R.string.action_share_link, R.drawable.ic_share) { actionHandler.share(webmark) }
        }
    }

private fun createPopupMenuForArchived(webmark: Webmark, actionHandler: WebmarkActionHandler) =
    popupMenu {
        section {
            header(webmark.title ?: webmark.url.toString())
            item(R.string.action_unarchive, R.drawable.ic_unarchive) { actionHandler.unarchive(webmark) }
            item(R.string.action_delete, R.drawable.ic_delete) { actionHandler.delete(webmark) }
            item(R.string.action_share_link, R.drawable.ic_share) { actionHandler.share(webmark) }
        }
    }
