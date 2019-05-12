package me.thanel.webmark.ui.popup

import android.content.Context
import android.view.Gravity
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.core.net.toUri
import com.github.zawadz88.materialpopupmenu.popupMenu
import me.thanel.webmark.share.openInBrowser

object LicensesPopupMenu {
    @VisibleForTesting
    internal val licenses = mapOf(
        "Crux" to "https://github.com/chimbori/crux/blob/master/LICENSE.txt",
        "Glide" to "https://github.com/bumptech/glide/blob/master/LICENSE",
        "jsoup" to "https://jsoup.org/license",
        "Kodein" to "https://github.com/Kodein-Framework/Kodein-DI/blob/master/LICENSE.txt",
        "Kotlin" to "https://github.com/JetBrains/kotlin-web-site/blob/master/LICENSE",
        "kotlinx.coroutines" to "https://github.com/Kotlin/kotlinx.coroutines/blob/master/LICENSE.txt",
        "Kotpref" to "https://github.com/chibatching/Kotpref/blob/master/LICENSE",
        "Material Popup Menu" to "https://github.com/zawadz88/MaterialPopupMenu/blob/master/LICENSE",
        "OkHttp" to "https://github.com/square/okhttp/blob/master/LICENSE.txt",
        "SQLDelight" to "https://github.com/square/sqldelight/blob/master/LICENSE.txt",
        "Timber" to "https://github.com/JakeWharton/timber/blob/master/LICENSE.txt"
    )

    fun show(context: Context, anchor: View) {
        popupMenu {
            dropdownGravity = Gravity.END
            section {
                for ((name, link) in licenses) {
                    item {
                        label = name
                        callback = {
                            context.openInBrowser(link.toUri())
                        }
                    }
                }
            }
        }.show(context, anchor)
    }
}
