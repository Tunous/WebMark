package me.thanel.webmark.ui.list

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.thanel.webmark.data.Database
import me.thanel.webmark.data.Webmark
import me.thanel.webmark.data.ext.asLiveData
import me.thanel.webmark.data.ext.mapToList
import me.thanel.webmark.ext.combineWith
import me.thanel.webmark.ext.mutableLiveDataOf
import me.thanel.webmark.ext.nullIfBlank
import me.thanel.webmark.ext.switchMap
import me.thanel.webmark.ui.base.BaseViewModel
import me.thanel.webmark.work.CleanupDatabaseWorker
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

    val webmarks: LiveData<List<Webmark>> = showArchiveLiveData.combineWith(filterLiveData)
        .switchMap {
            getWebmarks(it.first, it.second?.nullIfBlank())
        }

    private fun getWebmarks(loadArchive: Boolean, filterText: String?): LiveData<List<Webmark>> {
        val query = if (loadArchive) {
            database.webmarkQueries.selectArchived(filterText)
        } else {
            database.webmarkQueries.selectUnarchived(filterText)
        }
        return query.asLiveData()
            .mapToList(viewModelScope)
    }

    fun archiveWebmark(id: Long) = runInBackground {
        database.webmarkQueries.archiveById(id)
    }

    fun unarchiveWebmark(id: Long) = runInBackground {
        database.webmarkQueries.unarchiveById(id)
    }

    fun deleteWebmark(context: Context, id: Long) = runInBackground {
        database.webmarkQueries.setMarkedForDeletionById(id = id, markedForDeletion = true)
        CleanupDatabaseWorker.enqueueDelayed(context)
    }

    fun undoDeleteWebmark(context: Context, id: Long) = runInBackground {
        database.webmarkQueries.setMarkedForDeletionById(id = id, markedForDeletion = false)
        CleanupDatabaseWorker.cancelNonPeriodic(context)
    }

    suspend fun isSaved(uri: Uri): Boolean = withContext(Dispatchers.IO) {
        return@withContext database.webmarkQueries.selectIdForUrl(uri).executeAsOneOrNull() != null
    }
}
