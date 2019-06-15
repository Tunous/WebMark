package me.thanel.webmark.preferences

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(minSdk = 28)
class WebMarkPreferencesTest: BasePreferencesTest() {

    @Test
    fun `on API 28 or higher app theme should default to follow system`() {
        assertThat(WebMarkPreferences.appTheme, equalTo(AppTheme.FollowSystem))
    }
}
