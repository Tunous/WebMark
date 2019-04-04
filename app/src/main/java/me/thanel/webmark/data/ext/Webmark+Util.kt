package me.thanel.webmark.data.ext

import me.thanel.webmark.data.Webmark

val Webmark.isRead: Boolean
    get() = readAt != null
