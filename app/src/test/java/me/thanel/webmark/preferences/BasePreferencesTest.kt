package me.thanel.webmark.preferences

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.chibatching.kotpref.Kotpref
import org.junit.Before

abstract class BasePreferencesTest {

    @Before
    fun setupPreferences() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        Kotpref.init(context)
    }
}
