package me.thanel.webmark.work

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast

class SaveWebmarkActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = getSharedUrlFromIntent()
        if (url != null) {
            SaveWebmarkWorker.enqueue(url)
        } else {
            Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show()
        }
        finish()
    }

    private fun getSharedUrlFromIntent(): Uri? {
        val intent = intent ?: return null
        if (intent.action != Intent.ACTION_SEND) return null

        val url = intent.getStringExtra(Intent.EXTRA_TEXT) ?: return null
        if (!Patterns.WEB_URL.matcher(url).matches()) return null

        return Uri.parse(url)
    }
}
