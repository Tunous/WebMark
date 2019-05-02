package me.thanel.webmark.share

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.thanel.webmark.R
import me.thanel.webmark.ShareIntentChoiceBroadcastReceiver

fun Context.openInBrowser(uri: Uri) = startActivity(Intent(Intent.ACTION_VIEW, uri))

fun Context.share(uri: Uri) {
    val intent = createShareIntent(uri)
    val title = getString(R.string.title_share_with)

    val chooserIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        val receiver = Intent(this, ShareIntentChoiceBroadcastReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, 0, receiver, PendingIntent.FLAG_UPDATE_CURRENT)
        Intent.createChooser(intent, title, pendingIntent.intentSender)
    } else {
        Intent.createChooser(intent, title)
    }
    startActivity(chooserIntent)
}

fun Context.shareDirectly(uri: Uri, component: ComponentName) {
    val intent = createShareIntent(uri)
    intent.component = component
    startActivity(intent)
}

suspend fun Context.getShareActivityIconAsync(component: ComponentName): Drawable? =
    withContext(Dispatchers.IO) {
        val shareIntent = createShareIntent()
        val activities = packageManager.queryIntentActivities(shareIntent, 0)
        for (activity in activities) {
            val activityInfo = activity.activityInfo
            val packageName = activityInfo.applicationInfo.packageName
            val className = activityInfo.name
            if (component == ComponentName(packageName, className)) {
                return@withContext packageManager.getActivityIcon(component)
            }
        }
        return@withContext null
    }

fun createShareIntent(subject: String = "", text: String = "") =
    Intent(Intent.ACTION_SEND).apply {
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }

private fun createShareIntent(uri: Uri) =
    createShareIntent(text = uri.toString())
