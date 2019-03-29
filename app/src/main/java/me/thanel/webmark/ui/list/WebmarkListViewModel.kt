package me.thanel.webmark.ui.list

import android.app.Application
import androidx.lifecycle.viewModelScope
import me.thanel.webmark.Database
import me.thanel.webmark.ext.asLiveData
import me.thanel.webmark.ext.mapToList
import me.thanel.webmark.ui.base.BaseViewModel
import org.kodein.di.generic.instance

class WebmarkListViewModel(app: Application) : BaseViewModel(app) {
    private val database: Database by instance()

    val webmarks = database.webmarkQueries.selectAll()
        .asLiveData()
        .mapToList(viewModelScope)

    fun deleteWebmark(id: Long) = runInBackground {
        database.webmarkQueries.deleteById(id)
    }
}
