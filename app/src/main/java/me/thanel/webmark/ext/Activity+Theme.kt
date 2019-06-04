package me.thanel.webmark.ext

import androidx.appcompat.app.AppCompatDelegate
import me.thanel.webmark.preferences.AppTheme
import me.thanel.webmark.preferences.WebMarkPreferences

fun switchDefaultTheme(appTheme: AppTheme) {
    WebMarkPreferences.appTheme = appTheme
    refreshDefaultTheme()
}

fun refreshDefaultTheme() {
    AppCompatDelegate.setDefaultNightMode(WebMarkPreferences.appTheme.modeConstant)
}
