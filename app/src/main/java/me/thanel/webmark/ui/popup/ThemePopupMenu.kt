package me.thanel.webmark.ui.popup

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.CompoundButton
import com.github.zawadz88.materialpopupmenu.MaterialPopupMenuBuilder
import com.github.zawadz88.materialpopupmenu.popupMenu
import me.thanel.webmark.R
import me.thanel.webmark.ext.switchDefaultTheme
import me.thanel.webmark.preferences.AppTheme
import me.thanel.webmark.preferences.WebMarkPreferences

object ThemePopupMenu {
    fun show(context: Context, anchor: View) {
        popupMenu {
            dropdownGravity = Gravity.END
            section {
                themeSwitchItem(AppTheme.Light)
                themeSwitchItem(AppTheme.Dark)
                if (Build.VERSION.SDK_INT >= 28) {
                    themeSwitchItem(AppTheme.FollowSystem)
                } else {
                    themeSwitchItem(AppTheme.BatterySaver)
                }
            }
        }.show(context, anchor)
    }

    private fun MaterialPopupMenuBuilder.SectionHolder.themeSwitchItem(theme: AppTheme) {
        customItem {
            layoutResId = R.layout.item_menu_radio
            viewBoundCallback = {
                it.findViewById<CompoundButton>(R.id.itemRadioButton).apply {
                    setText(theme.labelResId)
                    isChecked = WebMarkPreferences.appTheme == theme
                    setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            switchDefaultTheme(theme)
                        }
                    }
                }
            }
        }
    }
}
