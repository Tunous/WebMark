package me.thanel.webmark.data.ext

import me.thanel.webmark.data.Webmark

val Webmark.isArchived: Boolean
    get() = archivedAt != null
