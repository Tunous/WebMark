package me.thanel.webmark.ui.popup

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface.BOLD
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.Gravity
import android.view.View
import androidx.core.net.toUri
import androidx.core.text.buildSpannedString
import androidx.core.text.inSpans
import com.github.zawadz88.materialpopupmenu.MaterialPopupMenuBuilder
import com.github.zawadz88.materialpopupmenu.popupMenu
import kotlinx.android.synthetic.main.view_about.view.*
import me.thanel.webmark.BuildConfig
import me.thanel.webmark.R
import me.thanel.webmark.ext.getColorFromAttr
import me.thanel.webmark.share.openInBrowser

object AboutPopupMenu {
    @SuppressLint("SetTextI18n")
    fun show(context: Context, anchor: View) {
        popupMenu {
            dropdownGravity = Gravity.END
            section {
                aboutView(context)
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
                    labelRes = R.string.action_licenses
                    icon = R.drawable.ic_open_source
                    callback = {
                        LicensesPopupMenu.show(context, anchor)
                    }
                }
                item {
                    labelRes = R.string.info_made_by
                    icon = R.drawable.ic_copyright
                }
            }
        }.show(context, anchor)
    }

    private fun MaterialPopupMenuBuilder.SectionHolder.aboutView(context: Context) {
        customItem {
            layoutResId = R.layout.view_about
            dismissOnSelect = false
            viewBoundCallback = {
                it.appNameView.text = buildSpannedString {
                    append(context.getText(R.string.app_name))
                    append(' ')
                    inSpans(
                        ForegroundColorSpan(context.getColorFromAttr(android.R.attr.textColorSecondary)),
                        StyleSpan(BOLD),
                        RelativeSizeSpan(0.6f)
                    ) {
                        append('v')
                        append(BuildConfig.VERSION_NAME)
                    }
                }
            }
        }
    }
}
