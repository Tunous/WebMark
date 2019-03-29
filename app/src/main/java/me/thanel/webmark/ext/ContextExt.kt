package me.thanel.webmark.ext

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openInBrowser(uri: Uri) = startActivity(Intent(Intent.ACTION_VIEW, uri))
