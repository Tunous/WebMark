package me.thanel.webmark.core.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.Px
import me.thanel.webmark.ui.imageloader.ImageLoader

object TestImageLoader : ImageLoader {
    override fun loadImage(
        targetView: ImageView,
        uri: Uri?,
        @Px cornerRadius: Int,
        onLoad: () -> Unit
    ) {
        if (uri == null) {
            targetView.setImageDrawable(null)
        } else {
            targetView.setImageDrawable(ColorDrawable(Color.BLACK))
            onLoad()
        }
    }
}
