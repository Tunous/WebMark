package me.thanel.webmark.ext

fun <E> MutableList<E>.addIf(item: E, condition: () -> Boolean): MutableList<E> {
    if (condition()) {
        add(item)
    }
    return this
}
