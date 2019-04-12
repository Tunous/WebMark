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

        insert(2L, "marked as read")
        db.markAsReadById(2L)

        insert(3L, "marked for deletion")
        db.setMarkedForDeletionById(true, 3L)

        insert(4L, "marked as read and for deletion")
        db.markAsReadById(4L)
        db.setMarkedForDeletionById(true, 4L)

        val actualIds = db.selectAll().executeAsList().map { it.id }
        assertThat(actualIds, Matchers.containsInAnyOrder(1L, 2L, 3L, 4L))
    }

    @Test
    fun selectUnread_shouldReturnOnlyUnreadWebmarks() {
        insert(1L, "default")

        insert(2L, "marked as read")
        db.markAsReadById(2L)

        insert(3L, "marked for deletion")
        db.setMarkedForDeletionById(true, 3L)

        insert(4L, "marked as read and for deletion")
        db.markAsReadById(4L)
        db.setMarkedForDeletionById(true, 4L)

        val actualIds = db.selectUnread(null).executeAsList().map { it.id }
        assertThat(actualIds, Matchers.containsInAnyOrder(1L))
    }

    @Test
    fun selectRead_shouldReturnOnlyReadWebmarks() {
        insert(1L, "default")

        insert(2L, "marked as read")
        db.markAsReadById(2L)

        insert(3L, "marked for deletion")
        db.setMarkedForDeletionById(true, 3L)

        insert(4L, "marked as read and for deletion")
        db.markAsReadById(4L)
        db.setMarkedForDeletionById(true, 4L)

        val actualIds = db.selectRead(null).executeAsList().map { it.id }
        assertThat(actualIds, Matchers.containsInAnyOrder(2L))
    }

    @Test
    fun selectWithFilter_shouldReturnWebmarksWithMatchingTitleOrUrl() {
        insert(1L, "example.com/kotlin-1", title = "Article about Kotlin")
        insert(2L, "example.com/kotlin-2", title = "Article about Kotlin number 2")
        db.markAsReadById(2L)
        insert(3L, "example.com/android-1", title = "Article about Android")
        insert(4L, "example.com/android-2", title = "Article about Android number 2")
        db.markAsReadById(4L)

        val unreadIdsByUrl = db.selectUnread("kotlin-").executeAsList().map { it.id }
        assertThat(unreadIdsByUrl, Matchers.containsInAnyOrder(1L))

        val unreadIdsByTitle = db.selectUnread("about Android").executeAsList().map { it.id }
        assertThat(unreadIdsByTitle, Matchers.containsInAnyOrder(3L))

        val readIdsByUrl = db.selectRead("kotlin-").executeAsList().map { it.id }
        assertThat(readIdsByUrl, Matchers.containsInAnyOrder(2L))

        val readIdsByTitle = db.selectRead("about Android").executeAsList().map { it.id }
        assertThat(readIdsByTitle, Matchers.containsInAnyOrder(4L))
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

        insert(7L,"bbb")
        insert(4L, "ccc")
        assertThat(db.lastInsertId().executeAsOne(), equalTo(4L))
    }

    @Test
    fun markingAsRead() {
        insert(1L, "aaa")
        insert(2L, "bbb")
        insert(3L, "ccc")

        db.markAsReadById(2L)
        db.markAsNewById(3L)
        db.markAsUnreadById(3L)

        val all = db.selectAll().executeAsList()
        assertThat(all.size, equalTo(3))

        val unread = db.selectUnread(null).executeAsList()
        assertThat(unread.size, equalTo(2))

        val read = db.selectRead(null).executeAsList()
        assertThat(read.size, equalTo(1))
        assertThat(read.first().url, equalTo(Uri.parse("bbb")))
    }

    @Test
    fun selection() {
        insert(1L, "aaa", "First article")
        insert(2L, "bbb", "Second article")
        insert(3L, "ccc", "Something else")
        insert(4L, "ddd", "Read article")
        insert(5L, "eee", "Third article - deleted")
        insert(6L, "fff", "Read article - deleted")

        db.markAsReadById(4L)
        db.setMarkedForDeletionById(true, 5L)
        db.markAsReadById(6L)
        db.setMarkedForDeletionById(true, 6L)

        // selectUnread should return only matching unread (not-marked) articles
        val unread = db.selectUnread("article").executeAsList()
        assertThat(unread.size, equalTo(2))
        assertThat(unread.map { it.title }, hasItems("First article", "Second article"))

        // Select read should return only matching read (not-marked) articles
        val read = db.selectRead("article").executeAsOne()
        assertThat(read.title, equalTo("Read article"))
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
