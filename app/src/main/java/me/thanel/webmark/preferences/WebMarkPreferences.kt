package me.thanel.webmark.preferences

import android.content.ComponentName
import android.os.Build
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumValuePref

object WebMarkPreferences : KotprefModel() {

    private var lastSharePackageName by nullableStringPref(default = null)
    private var lastShareClassName by nullableStringPref(default = null)

    var latestSuggestedUrl by nullableStringPref(default = null)

    private val defaultAppTheme
        get() = if (Build.VERSION.SDK_INT >= 28) {
            AppTheme.FollowSystem
        } else {
            AppTheme.BatterySaver
        }
    var appTheme by enumValuePref(default = defaultAppTheme)

    init {
        appTheme = AppTheme.FollowSystem
    }

    var latestShareComponent: ComponentName?
        get() {
            val packageName = lastSharePackageName ?: return null
            val className = lastShareClassName ?: return null
            return ComponentName(packageName, className)
        }
        set(value) {
            lastSharePackageName = value?.packageName
            lastShareClassName = value?.className
        }
}
