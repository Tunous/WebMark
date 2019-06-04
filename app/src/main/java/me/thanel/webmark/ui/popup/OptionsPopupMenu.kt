package me.thanel.webmark.ui.popup

import android.content.Context
import android.view.Gravity
import android.view.View
import com.github.zawadz88.materialpopupmenu.popupMenu
import me.thanel.webmark.R

object OptionsPopupMenu {
    fun show(context: Context, anchor: View) {
        popupMenu {
            dropdownGravity = Gravity.END
            section {
                item {
                    labelRes = R.string.action_select_theme
                    icon = R.drawable.ic_theme
                    callback = {
                        ThemePopupMenu.show(context, anchor)
                    }
                }
                item {
                    labelRes = R.string.title_about
                    icon = R.drawable.ic_info
                    hasNestedItems = true
                    callback = {
                        AboutPopupMenu.show(context, anchor)
                    }
                }
            }
        }.show(context, anchor)
    }
}
