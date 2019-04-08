package me.thanel.webmark.ext

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import androidx.annotation.AttrRes
import androidx.core.content.res.use

@SuppressLint("Recycle")
fun Context.getColorFromAttr(@AttrRes attrResId: Int): Int {
    return obtainStyledAttributes(intArrayOf(attrResId)).use {
        it.getColor(0, Color.WHITE)
    }
}

