package me.thanel.webmark.ui.list

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import me.thanel.webmark.Database
import me.thanel.webmark.data.Webmark
import me.thanel.webmark.ext.asLiveData
import me.thanel.webmark.ext.combineWith
import me.thanel.webmark.ext.mapToList
import me.thanel.webmark.ext.mutableLiveDataOf
import me.thanel.webmark.ext.nullIfBlank
import me.thanel.webmark.ext.switchMap
import me.thanel.webmark.ui.base.BaseViewModel
import org.kodein.di.generic.instance

class WebmarkListViewModel(app: Application) : BaseViewModel(app) {
    private val database: Database by instance()

    private val filterLiveData = mutableLiveDataOf("")
    private val showArchiveLiveData = mutableLiveDataOf(false)

    var showArchive: Boolean
        get() = showArchiveLiveData.value!!
        set(value) {
            showArchiveLiveData.value = value
        }

    var filterText: String
        get() = filterLiveData.value!!
        set(value) {
            filterLiveData.value = value
        }

    val unreadWebmarks: LiveData<List<Webmark>> = showArchiveLiveData.combineWith(filterLiveData)
        .switchMap {
            getWebmarks(it.first, it.second?.nullIfBlank())
        }

    private fun getWebmarks(loadArchive: Boolean, filterText: String?): LiveData<List<Webmark>> {
        val query = if (loadArchive) {
            database.webmarkQueries.selectRead(filterText)
        } else {
            database.webmarkQueries.selectUnread(filterText)
        }
        return query.asLiveData()
            .mapToList(viewModelScope)
    }

    fun markWebmarkAsRead(id: Long) = runInBackground {
        database.webmarkQueries.marAsReadById(id)
    }

    fun markWebmarkAsUnread(id: Long) = runInBackground {
        database.webmarkQueries.markAsUnreadById(id)
    }
}
