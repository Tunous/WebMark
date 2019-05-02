package me.thanel.webmark.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import me.thanel.webmark.ext.launchIdling
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

abstract class BaseViewModel(app: Application) : AndroidViewModel(app), KodeinAware {

    override val kodein by kodein()

    protected fun runInBackground(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launchIdling(Dispatchers.IO, block = block)
    }

    protected fun runInMain(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launchIdling(Dispatchers.Main, block = block)
    }
}
