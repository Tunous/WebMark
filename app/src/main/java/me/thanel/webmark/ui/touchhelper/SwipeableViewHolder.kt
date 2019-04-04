package me.thanel.webmark.ui.touchhelper

interface SwipeableViewHolder {
    fun onSwipe(distance: Float)
    fun onClearSwipe()
    fun getSwipeDirs(): Int
}
