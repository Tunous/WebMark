package me.thanel.webmark.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import me.thanel.webmark.preferences.WebMarkPreferences

fun AppCompatActivity.updateTheme(useDarkTheme: Boolean = WebMarkPreferences.useDarkTheme) {
    val nightMode = when {
        useDarkTheme -> AppCompatDelegate.MODE_NIGHT_YES
        else -> AppCompatDelegate.MODE_NIGHT_NO
    }
    delegate.setLocalNightMode(nightMode)
}

fun Fragment.updateTheme(useDarkTheme: Boolean = WebMarkPreferences.useDarkTheme) {
    val activity = requireActivity()
    check(activity is AppCompatActivity) { "Parent activity must extend AppCompatActivity to update theme." }
    activity.updateTheme(useDarkTheme)
}
