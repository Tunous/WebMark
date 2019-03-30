package me.thanel.webmark.ext

import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.view.isVisible
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import me.thanel.webmark.R

@Suppress("UNCHECKED_CAST")
fun <T> View.getTagProperty(id: Int, initializer: View.() -> T): T {
    val storedProperty = getTag(id) as? T
    if (storedProperty != null) return storedProperty

    val property = initializer()
    setTag(id, property)
    return property
}

val View.glideVisibilityListener: RequestListener<Drawable?>
    get() = getTagProperty(R.id.glide_load_visibility_listener) {
        object : RequestListener<Drawable?> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable?>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable?>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                isVisible = true
                return false
            }
        }
    }
