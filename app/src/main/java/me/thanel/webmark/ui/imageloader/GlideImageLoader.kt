package me.thanel.webmark.ui.imageloader

import android.net.Uri
import android.widget.ImageView
import androidx.annotation.Px
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import me.thanel.webmark.ext.applyIf
import me.thanel.webmark.ui.ext.getGlideLoadListener

object GlideImageLoader : ImageLoader {
    override fun loadImage(
        targetView: ImageView,
        uri: Uri?,
        @Px cornerRadius: Int,
        onLoad: () -> Unit
    ) {
        Glide.with(targetView)
            .load(uri)
            .applyIf(cornerRadius > 0) { transform(CenterCrop(), RoundedCorners(cornerRadius)) }
            .listener(targetView.getGlideLoadListener(onLoad))
            .into(targetView)
    }
}
