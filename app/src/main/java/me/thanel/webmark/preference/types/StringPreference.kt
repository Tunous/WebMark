package me.thanel.webmark.preference.types

import android.content.SharedPreferences
import androidx.core.content.edit
import me.thanel.webmark.preference.Preference
import me.thanel.webmark.preference.PreferenceKey

internal class StringPreference(
    private val key: PreferenceKey,
    private val preferences: SharedPreferences
) : Preference<String> {

    override var value: String?
        get() = preferences.getString(key.name, null)
        set(newValue) {
            preferences.edit {
                putString(key.name, newValue)
            }
        }

    override fun remove() = preferences.edit {
        remove(key.name)
    }
}
