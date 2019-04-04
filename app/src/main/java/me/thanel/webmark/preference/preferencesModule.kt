package me.thanel.webmark.preference

import me.thanel.webmark.preference.types.StringPreference
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

fun preferencesModule() = Kodein.Module("preferencesModule") {
    bindStringPreference(PreferenceKey.LatestSuggestedUrl)
}

private fun Kodein.Builder.bindStringPreference(key: PreferenceKey) =
    bind<Preference<String>>(tag = key) with singleton {
        StringPreference(key, instance())
    }
