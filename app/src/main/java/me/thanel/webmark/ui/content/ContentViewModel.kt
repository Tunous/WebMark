package me.thanel.webmark.ui.content

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import me.thanel.webmark.data.Database
import me.thanel.webmark.data.Webmark
import me.thanel.webmark.data.ext.asLiveData
import me.thanel.webmark.data.ext.mapToOne
import me.thanel.webmark.ext.switchMap
import me.thanel.webmark.ui.base.BaseViewModel
import org.kodein.di.generic.instance

class ContentViewModel(app: Application) : BaseViewModel(app) {

    private val database: Database by instance()

    private val webmarkIdLiveData = MediatorLiveData<Long>()

    var webmarkId: Long
        get() = webmarkIdLiveData.value!!
        set(value) {
            webmarkIdLiveData.value = value
        }

    val webmark: LiveData<Webmark> = webmarkIdLiveData.switchMap {
        database.webmarkQueries.selectById(it).asLiveData().mapToOne(viewModelScope)
    }
}
