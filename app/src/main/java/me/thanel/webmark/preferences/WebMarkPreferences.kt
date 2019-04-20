package me.thanel.webmark.preferences

import com.chibatching.kotpref.KotprefModel

object WebMarkPreferences : KotprefModel() {

    var latestSuggestedUrl by nullableStringPref(default = null)
}
