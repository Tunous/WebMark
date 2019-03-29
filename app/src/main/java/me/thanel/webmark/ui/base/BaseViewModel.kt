package me.thanel.webmark.ui.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

abstract class BaseViewModel(app: Application) : AndroidViewModel(app), KodeinAware {

    override val kodein by kodein()

    protected fun runInBackground(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(Dispatchers.IO, block = block)
    }

    protected fun runInMain(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(Dispatchers.Main, block = block)
    }

}
