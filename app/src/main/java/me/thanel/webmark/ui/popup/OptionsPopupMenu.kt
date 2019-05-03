package me.thanel.webmark.ui.popup

import android.content.Context
import android.view.Gravity
import android.view.View
import com.github.zawadz88.materialpopupmenu.popupMenu
import me.thanel.webmark.R
import me.thanel.webmark.preferences.WebMarkPreferences

object OptionsPopupMenu {
    fun show(context: Context, anchor: View, toggleThemeAction: () -> Unit) {
        popupMenu {
            dropdownGravity = Gravity.END
            section {
                item {
                    labelRes = when {
                        WebMarkPreferences.useDarkTheme -> R.string.action_use_light_theme
                        else -> R.string.action_use_dark_theme
                    }
                    icon = when {
                        WebMarkPreferences.useDarkTheme -> R.drawable.ic_moon_filled
                        else -> R.drawable.ic_moon_outline
                    }
                    callback = toggleThemeAction
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
