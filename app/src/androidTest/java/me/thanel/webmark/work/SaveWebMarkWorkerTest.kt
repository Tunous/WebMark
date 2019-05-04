package me.thanel.webmark.work

import androidx.core.net.toUri
import androidx.work.WorkInfo
import me.thanel.webmark.data.Database
import me.thanel.webmark.test.base.work.BaseWorkerTest
import org.junit.Assert.assertNotNull
import org.junit.Test

class SaveWebMarkWorkerTest : BaseWorkerTest() {
    @Test
    fun will_save_new_link_to_database() {
        val uri = "https://example.com".toUri()

        val (request, operation) = SaveWebmarkWorker.enqueue(appContext, uri)
        operation.result.get()

        awaitWorkState(request, WorkInfo.State.SUCCEEDED)
        val queries = getDependency<Database>().webmarkQueries
        val id = queries.selectIdForUrl(uri).executeAsOneOrNull()
        assertNotNull("Webmark for requested url should be saved", id)
    }
}
