package me.thanel.webmark.action

import me.thanel.webmark.data.Webmark

interface WebmarkActionHandler {
    fun performAction(action: WebmarkAction, webmark: Webmark)
}
