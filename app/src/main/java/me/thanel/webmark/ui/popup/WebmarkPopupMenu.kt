package me.thanel.webmark.ui.popup

import com.github.zawadz88.materialpopupmenu.MaterialPopupMenuBuilder
import com.github.zawadz88.materialpopupmenu.popupMenu
import me.thanel.webmark.action.WebmarkActionHandler
import me.thanel.webmark.data.BuildConfig
import me.thanel.webmark.data.Webmark
import me.thanel.webmark.data.ext.isArchived
import me.thanel.webmark.ui.ext.coloredHeader
import me.thanel.webmark.ui.ext.item
import me.thanel.webmark.ui.touchhelper.WebmarkAction

fun createPopupMenu(webmark: Webmark, actionHandler: WebmarkActionHandler) =
    popupMenu {
        section {
            coloredHeader(webmark.title ?: webmark.url.toString())

            val archiveAction = if (webmark.isArchived) WebmarkAction.Unarchive else WebmarkAction.Archive
            item(webmark, archiveAction, actionHandler)
            item(webmark, WebmarkAction.Delete, actionHandler)
            item(webmark, WebmarkAction.ShareLink, actionHandler)
        }

        if (BuildConfig.DEBUG) {
            section {
                item(webmark, WebmarkAction.ExtractDetails, actionHandler)
            }
        }
    }

fun MaterialPopupMenuBuilder.SectionHolder.item(
    webmark: Webmark,
    action: WebmarkAction,
    actionHandler: WebmarkActionHandler
) = item(action.labelResId, action.iconResId) { actionHandler.performAction(action, webmark) }
