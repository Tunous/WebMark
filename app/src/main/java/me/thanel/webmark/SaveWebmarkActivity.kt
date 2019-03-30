package me.thanel.webmark

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.URLUtil
import android.widget.Toast

class SaveWebmarkActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = getSharedUrlFromIntent()
        if (url != null) {
            startService(SaveWebmarkService.getIntent(this, url))
        } else {
            Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    private fun getSharedUrlFromIntent(): Uri? {
        val intent = intent ?: return null
        if (intent.action != Intent.ACTION_SEND) return null

        val url = intent.getStringExtra(Intent.EXTRA_TEXT) ?: return null
        if (!URLUtil.isHttpUrl(url) && !URLUtil.isHttpsUrl(url)) return null

        return Uri.parse(url)
    }
}
