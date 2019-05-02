package me.thanel.webmark.preferences

import android.content.ComponentName
import com.chibatching.kotpref.KotprefModel

object WebMarkPreferences : KotprefModel() {

    private var lastSharePackageName by nullableStringPref(default = null)
    private var lastShareClassName by nullableStringPref(default = null)

    var latestSuggestedUrl by nullableStringPref(default = null)

    var useDarkTheme by booleanPref(default = false)

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
