package me.thanel.webmark.test

import me.thanel.webmark.WebMarkApp
import me.thanel.webmark.data.testDatabaseModule
import me.thanel.webmark.test.base.TestImageLoader
import me.thanel.webmark.ui.imageloader.ImageLoader
import org.kodein.di.Copy
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class WebMarkTestApp : WebMarkApp() {

    override val kodein by Kodein.lazy {
        extend(super.kodein, copy = Copy.All)
        import(testDatabaseModule(this@WebMarkTestApp), true)
        bind<ImageLoader>(overrides = true) with singleton { TestImageLoader }
    }

    override fun onCreate() {
        super.onCreate()
    }
}
