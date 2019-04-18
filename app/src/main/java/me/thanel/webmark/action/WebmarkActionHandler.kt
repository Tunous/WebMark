package me.thanel.webmark.action

import me.thanel.webmark.data.Webmark

interface WebmarkActionHandler {
    fun archive(webmark: Webmark)
    fun unarchive(webmark: Webmark)
    fun delete(webmark: Webmark)
    fun share(webmark: Webmark)
}
