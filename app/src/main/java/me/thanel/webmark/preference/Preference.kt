package me.thanel.webmark.preference

interface Preference<T> {
    var value: T?

    fun remove()
}
