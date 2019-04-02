package me.thanel.webmark.ui.list

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import me.thanel.webmark.Database
import me.thanel.webmark.data.Webmark
import me.thanel.webmark.ext.asLiveData
import me.thanel.webmark.ext.mapToList
import me.thanel.webmark.ext.nullIfBlank
import me.thanel.webmark.ui.base.BaseViewModel
import org.kodein.di.generic.instance

class WebmarkListViewModel(app: Application) : BaseViewModel(app) {
    private val database: Database by instance()

    private val filterLiveData = MutableLiveData<String?>().apply {
        value = null
    }

    fun filter(text: String?) {
        filterLiveData.value = text
    }

    val unreadWebmarks: LiveData<List<Webmark>> = Transformations.switchMap(filterLiveData) {
        database.webmarkQueries.selectUnread(it?.nullIfBlank())
            .asLiveData()
            .mapToList(viewModelScope)
    }

    fun markWebmarkAsRead(id: Long) = runInBackground {
        database.webmarkQueries.marAsReadById(id)
    }

    fun markWebmarkAsUnread(id: Long) = runInBackground {
        database.webmarkQueries.markAsUnreadById(id)
    }
}
