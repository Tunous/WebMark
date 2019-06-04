package me.thanel.webmark.preferences

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import me.thanel.webmark.R

enum class AppTheme(val modeConstant: Int) {
    Light(AppCompatDelegate.MODE_NIGHT_NO),
    Dark(AppCompatDelegate.MODE_NIGHT_YES),
    BatterySaver(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY),
    FollowSystem(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

    @get:StringRes
    val labelResId: Int
        get() = when (this) {
            Light -> R.string.title_theme_light
            Dark -> R.string.title_theme_dark
            BatterySaver -> R.string.title_theme_battery_saver
            FollowSystem -> R.string.title_theme_follow_system
        }
}
