package me.thanel.webmark.ext

fun <T : Any> T.applyIf(condition: Boolean, block: T.() -> Unit): T {
    if (condition) {
        block()
    }
    return this
}
