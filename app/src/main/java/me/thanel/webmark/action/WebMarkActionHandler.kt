package me.thanel.webmark.action

import me.thanel.webmark.data.Webmark

interface WebMarkActionHandler {
    fun performAction(action: WebMarkAction, webmark: Webmark)
}
