package me.thanel.webmark.action

import me.thanel.webmark.data.Webmark
import me.thanel.webmark.ui.touchhelper.WebmarkAction

interface WebmarkActionHandler {
    fun performAction(action: WebmarkAction, webmark: Webmark)
}
