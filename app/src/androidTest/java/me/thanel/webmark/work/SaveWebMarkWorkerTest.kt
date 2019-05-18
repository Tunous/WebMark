package me.thanel.webmark.work

import androidx.core.net.toUri
import androidx.work.WorkInfo
import me.thanel.webmark.data.Database
import me.thanel.webmark.test.base.work.BaseWorkerTest
import org.hamcrest.Matchers.equalTo
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test

class SaveWebMarkWorkerTest : BaseWorkerTest() {

    @Before
    fun setupDatabase() {
        getDependency<Database>().webMarkQueries.deleteEverything()
    }

    @Test
    fun will_save_new_link_to_database() {
        val uri = "https://example.com".toUri()

        val (request, operation) = SaveWebMarkWorker.enqueue(appContext, uri)
        operation.result.get()

        awaitWorkState(request, WorkInfo.State.SUCCEEDED)
        val queries = getDependency<Database>().webMarkQueries
        val id = queries.selectIdForUrl(uri).executeAsOneOrNull()
        assertNotNull("Webmark for requested url should be saved", id)
    }

    @Test
    fun will_succeed_without_saving_if_webmark_already_exists() {
        val queries = getDependency<Database>().webMarkQueries
        val uri = "https://example.com".toUri()
        val originalId = 1L
        queries.insert(originalId, uri)

        val (request, operation) = SaveWebMarkWorker.enqueue(appContext, uri)
        operation.result.get()

        awaitWorkState(request, WorkInfo.State.SUCCEEDED)
        val id = queries.selectIdForUrl(uri).executeAsOne()
        assertThat(id, equalTo(originalId))
    }

    @Test
    fun will_start_details_extraction_task_after_saving() {
        val uri = "https://example.com".toUri()

        val (request, operation) = SaveWebMarkWorker.enqueue(appContext, uri)
        operation.result.get()
        awaitWorkState(request, WorkInfo.State.SUCCEEDED)

        assertWorkWithTagStarted(ExtractWebMarkDetailsWorker.TAG)
    }
}
