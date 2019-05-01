package me.thanel.webmark.data

import android.database.sqlite.SQLiteConstraintException
import android.net.Uri
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.hasItems
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers
import org.junit.Test

class WebmarkTest : DatabaseTest<WebmarkQueries>({ it.webmarkQueries }) {

    @Test
    fun selectAll_shouldReturnEverything() {
        insert(1L, "default")

        insert(2L, "archived")
        db.archiveById(2L)

        insert(3L, "marked for deletion")
        db.setMarkedForDeletionById(true, 3L)

        insert(4L, "archived and marked for deletion")
        db.archiveById(4L)
        db.setMarkedForDeletionById(true, 4L)

        val actualIds = db.selectAll().executeAsList().map { it.id }
        assertThat(actualIds, Matchers.containsInAnyOrder(1L, 2L, 3L, 4L))
    }

    @Test
    fun selectUnarchived_shouldReturnOnlyUnarchivedWebmarks() {
        insert(1L, "default")

        insert(2L, "archived")
        db.archiveById(2L)

        insert(3L, "marked for deletion")
        db.setMarkedForDeletionById(true, 3L)

        insert(4L, "archived and marked for deletion")
        db.archiveById(4L)
        db.setMarkedForDeletionById(true, 4L)

        val actualIds = db.selectUnarchived(null).executeAsList().map { it.id }
        assertThat(actualIds, Matchers.containsInAnyOrder(1L))
    }

    @Test
    fun selectArchived_shouldReturnOnlyArchivedWebmarks() {
        insert(1L, "default")

        insert(2L, "archived")
        db.archiveById(2L)

        insert(3L, "marked for deletion")
        db.setMarkedForDeletionById(true, 3L)

        insert(4L, "archived and marked for deletion")
        db.archiveById(4L)
        db.setMarkedForDeletionById(true, 4L)

        val actualIds = db.selectArchived(null).executeAsList().map { it.id }
        assertThat(actualIds, Matchers.containsInAnyOrder(2L))
    }

    @Test
    fun selectWithFilter_shouldReturnWebmarksWithMatchingTitleOrUrl() {
        insert(1L, "example.com/kotlin-1", title = "Article about Kotlin")
        insert(2L, "example.com/kotlin-2", title = "Article about Kotlin number 2")
        db.archiveById(2L)
        insert(3L, "example.com/android-1", title = "Article about Android")
        insert(4L, "example.com/android-2", title = "Article about Android number 2")
        db.archiveById(4L)

        val unarchivedIdsByUrl = db.selectUnarchived("kotlin-").executeAsList().map { it.id }
        assertThat(unarchivedIdsByUrl, Matchers.containsInAnyOrder(1L))

        val unarchivedIdsByTitle = db.selectUnarchived("about Android").executeAsList().map { it.id }
        assertThat(unarchivedIdsByTitle, Matchers.containsInAnyOrder(3L))

        val archivedIdsByUrl = db.selectArchived("kotlin-").executeAsList().map { it.id }
        assertThat(archivedIdsByUrl, Matchers.containsInAnyOrder(2L))

        val archivedIdsByTitle = db.selectArchived("about Android").executeAsList().map { it.id }
        assertThat(archivedIdsByTitle, Matchers.containsInAnyOrder(4L))
    }

    @Test
    fun insert_shouldWork() {
        db.insert(1L, Uri.parse("example.com"))

        val webmark = db.selectAll().executeAsOne()
        assertThat(webmark.id, equalTo(1L))
        assertThat(webmark.url, equalTo(Uri.parse("example.com")))
    }

    @Test(expected = SQLiteConstraintException::class)
    fun insert_shouldDisallowDuplicateLinks() {
        db.insert(1L, Uri.parse("example.com"))
        db.insert(2L, Uri.parse("example.com"))
    }

    @Test(expected = SQLiteConstraintException::class)
    fun insert_shouldDisallowDuplicateIds() {
        db.insert(1L, Uri.parse("example.com"))
        db.insert(1L, Uri.parse("example.com/2"))
    }

    @Test
    fun lastInsertId_shouldReturnIdOfLatestInsertion() {
        insert(1L, "aaa")
        assertThat(db.lastInsertId().executeAsOne(), equalTo(1L))

        insert(7L, "bbb")
        insert(4L, "ccc")
        assertThat(db.lastInsertId().executeAsOne(), equalTo(4L))
    }

    @Test
    fun archiving() {
        insert(1L, "aaa")
        insert(2L, "bbb")
        insert(3L, "ccc")

        db.archiveById(2L)
        db.markAsNewById(3L)
        db.unarchiveById(3L)

        val all = db.selectAll().executeAsList()
        assertThat(all.size, equalTo(3))

        val unarchived = db.selectUnarchived(null).executeAsList()
        assertThat(unarchived.size, equalTo(2))

        val archived = db.selectArchived(null).executeAsList()
        assertThat(archived.size, equalTo(1))
        assertThat(archived.first().url, equalTo(Uri.parse("bbb")))
    }

    @Test
    fun selection() {
        insert(1L, "aaa", "First article")
        insert(2L, "bbb", "Second article")
        insert(3L, "ccc", "Something else")
        insert(4L, "ddd", "Archived article")
        insert(5L, "eee", "Third article - deleted")
        insert(6L, "fff", "Archived article - deleted")

        db.archiveById(4L)
        db.setMarkedForDeletionById(true, 5L)
        db.archiveById(6L)
        db.setMarkedForDeletionById(true, 6L)

        // selectUnarchived should return only matching unarchived (not-marked for deletion) articles
        val unarchived = db.selectUnarchived("article").executeAsList()
        assertThat(unarchived.size, equalTo(2))
        assertThat(unarchived.map { it.title }, hasItems("First article", "Second article"))

        // Select archived should return only matching archived (not-marked for deletion) articles
        val archived = db.selectArchived("article").executeAsOne()
        assertThat(archived.title, equalTo("Archived article"))
    }

    @Test
    fun deletion() {
        insert(1L, "aaa")
        insert(2L, "bbb")
        insert(3L, "ccc")

        // Marking for deletion should keep the item in database
        db.setMarkedForDeletionById(true, 2L)
        assertThat(db.selectAll().executeAsList().size, equalTo(3))

        // Deletion should remove immediately
        db.deleteById(1L)
        assertThat(db.selectAll().executeAsList().size, equalTo(2))

        // Cleanup should delete marked items
        db.cleanup()
        assertThat(db.selectAll().executeAsOne().id, equalTo(3L))
    }

    private fun insert(id: Long? = null, url: String, title: String? = null) {
        db.insert(id, Uri.parse(url))
        if (title != null) {
            val insertedId = db.lastInsertId().executeAsOne()
            db.updateById(
                id = insertedId,
                title = title,
                faviconUrl = null,
                estimatedReadingTimeMinutes = 0,
                imageUrl = null
            )
        }
    }
}
