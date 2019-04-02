package me.thanel.webmark.ext

fun String.nullIfBlank() = if (isBlank()) null else this
