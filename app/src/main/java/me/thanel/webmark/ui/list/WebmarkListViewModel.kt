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

    val unreadWebmarks = database.webmarkQueries.selectUnread()
        .asLiveData()
        .mapToList(viewModelScope)

    fun markWebmarkAsRead(id: Long) = runInBackground {
        database.webmarkQueries.marAsReadById(id)
    }

    fun markWebmarkAsUnread(id: Long) = runInBackground {
        database.webmarkQueries.markAsUnreadById(id)
    }
}
