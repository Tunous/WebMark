package me.thanel.webmark.ui.popup

import android.annotation.SuppressLint
import android.content.Context
import android.view.Gravity
import android.view.View
import androidx.core.net.toUri
import com.github.zawadz88.materialpopupmenu.popupMenu
import kotlinx.android.synthetic.main.view_about.view.*
import me.thanel.webmark.BuildConfig
import me.thanel.webmark.R
import me.thanel.webmark.share.openInBrowser

object AboutPopupMenu {
    @SuppressLint("SetTextI18n")
    fun show(context: Context, anchor: View) {
        popupMenu {
            dropdownGravity = Gravity.END
            section {
                customItem {
                    layoutResId = R.layout.view_about
                    viewBoundCallback = {
                        it.appVersionView.text = "v${BuildConfig.VERSION_NAME}"
                    }
                }
            }
            section {
                item {
                    labelRes = R.string.action_view_github
                    icon = R.drawable.ic_github
                    callback = {
                        context.openInBrowser("https://github.com/Tunous/WebMark".toUri())
                    }
                }
                item {
                    labelRes = R.string.info_made_by
                    icon = R.drawable.ic_copyright
                }
            }
        }.show(context, anchor)
    }
}
