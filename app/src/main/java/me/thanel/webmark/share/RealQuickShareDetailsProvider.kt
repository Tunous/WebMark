package me.thanel.webmark.share

import android.content.ComponentName
import android.content.Context
import android.graphics.drawable.Drawable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RealQuickShareDetailsProvider(private val appContext: Context) :
    QuickShareDetailsProvider {

    override suspend fun getQuickShareIconAsync(component: ComponentName): Drawable? =
        withContext(Dispatchers.IO) {
            getQuickShareIcon(component)
        }

    private fun getQuickShareIcon(component: ComponentName): Drawable? {
        val shareIntent = createShareIntent()
        val activities = appContext.packageManager.queryIntentActivities(shareIntent, 0)
        for (activity in activities) {
            val activityInfo = activity.activityInfo
            val packageName = activityInfo.applicationInfo.packageName
            val className = activityInfo.name
            if (component == ComponentName(packageName, className)) {
                return appContext.packageManager.getActivityIcon(component)
            }
        }
        return null
    }
}
