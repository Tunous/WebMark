package me.thanel.webmark.ui.imageloader

import android.net.Uri
import android.widget.ImageView
import androidx.annotation.Px

interface ImageLoader {
    fun loadImage(
        targetView: ImageView,
        uri: Uri?, @Px cornerRadius: Int = 0,
        onLoad: () -> Unit
    )
}

