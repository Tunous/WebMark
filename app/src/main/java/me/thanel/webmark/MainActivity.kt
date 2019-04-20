package me.thanel.webmark

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import me.thanel.webmark.preferences.WebMarkPreferences
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein

class MainActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(if (WebMarkPreferences.useDarkTheme) R.style.AppTheme_Dark else R.style.AppTheme)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
