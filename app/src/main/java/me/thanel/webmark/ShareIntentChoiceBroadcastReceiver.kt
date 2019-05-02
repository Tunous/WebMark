package me.thanel.webmark

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import me.thanel.webmark.preferences.WebMarkPreferences
import timber.log.Timber

class ShareIntentChoiceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) {
            Timber.w("Received intent on unsupported Android version: ${Build.VERSION.SDK_INT}")
            return
        }

        val componentName = intent.extras?.getParcelable<ComponentName>(Intent.EXTRA_CHOSEN_COMPONENT)
        if (componentName != null) {
            WebMarkPreferences.latestShareComponent = componentName
        }
    }
}
