package me.thanel.webmark.ext

import android.content.Context
import android.content.Intent
import android.net.Uri
import me.thanel.webmark.R

fun Context.openInBrowser(uri: Uri) = startActivity(Intent(Intent.ACTION_VIEW, uri))

fun Context.share(uri: Uri) {
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, uri.toString())
        type = "text/plain"
    }
    val chooserIntent = Intent.createChooser(intent, getString(R.string.title_share_with))
    startActivity(chooserIntent)
}
