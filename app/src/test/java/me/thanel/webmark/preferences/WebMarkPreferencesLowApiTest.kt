package me.thanel.webmark.preferences

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [21, 27])
class WebMarkPreferencesLowApiTest: BasePreferencesTest() {

    @Test
    fun `on API 27 or lower app theme should default to follow battery saver`() {
        assertThat(WebMarkPreferences.appTheme, equalTo(AppTheme.BatterySaver))
    }
}
