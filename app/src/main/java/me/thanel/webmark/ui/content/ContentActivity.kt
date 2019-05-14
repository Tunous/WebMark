package me.thanel.webmark.ui.content

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.thanel.webmark.R

class ContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ContentFragment().apply {
                    arguments = Bundle().apply {
                        putLong("webmark-id", intent.getLongExtra("webmark-id", 0L))
                    }
                })
                .commitNow()
        }
    }

    companion object {
        fun start(context: Context, webMarkId: Long) {
            val intent = Intent(context, ContentActivity::class.java).apply {
                putExtra("webmark-id", webMarkId)
            }
            context.startActivity(intent)
        }
    }
}
