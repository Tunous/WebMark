package me.thanel.webmark.ui.ext

import android.view.View
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

fun <T> View.getGlideLoadListener(onSuccess: () -> Unit): RequestListener<T> {
    return getTagProperty(R.id.glide_load_listener) {
        object : RequestListener<T> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<T>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: T,
                model: Any?,
                target: Target<T>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onSuccess()
                return false
            }
        }
    }
}
