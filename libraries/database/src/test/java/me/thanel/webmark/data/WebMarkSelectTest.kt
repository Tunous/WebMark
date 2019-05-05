package me.thanel.webmark.data

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.contains
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.Test

class WebMarkSelectTest : BaseDatabaseTest() {

    // TODO:
    //  selectIdForUrl
    //  selectUrlForId

    @Test
    fun `selectAll should return everything`() {
        insert(1L, "default")
        insert(2L, "archived")
        db.archiveById(2L)
        insert(3L, "marked for deletion")
        db.setMarkedForDeletionById(true, 3L)
        insert(4L, "archived and marked for deletion")
        db.archiveById(4L)
        db.setMarkedForDeletionById(true, 4L)

        val actualIds = db.selectAll().executeAsList().map { it.id }
        assertThat(actualIds, containsInAnyOrder(1L, 2L, 3L, 4L))
    }

    @Test
    fun `selectUnarchived should return only unarchived webmarks`() {
        insert(1L, "default")
        insert(2L, "archived")
        db.archiveById(2L)
        insert(3L, "marked for deletion")
        db.setMarkedForDeletionById(true, 3L)
        insert(4L, "archived and marked for deletion")
        db.archiveById(4L)
        db.setMarkedForDeletionById(true, 4L)

        val actualIds = db.selectUnarchived(null).executeAsList().map { it.id }
        assertThat(actualIds, containsInAnyOrder(1L))
    }

    @Test
    fun `selectArchived should return only archived webmarks`() {
        insert(1L, "default")
        insert(2L, "archived")
        db.archiveById(2L)
        insert(3L, "marked for deletion")
        db.setMarkedForDeletionById(true, 3L)
        insert(4L, "archived and marked for deletion")
        db.archiveById(4L)
        db.setMarkedForDeletionById(true, 4L)

        val actualIds = db.selectArchived(null).executeAsList().map { it.id }
        assertThat(actualIds, containsInAnyOrder(2L))
    }

    @Test
    fun `selectUnarchived should order results by saved date starting from today`() {
        insert(1L, "saved today")
        insert(2L, "saved month ago")
        db.setSavedAt(todayPlusDays(-30), 2L)
        insert(3L, "saved 5 days ago")
        db.setSavedAt(todayPlusDays(-5), 3L)
        insert(4L, "saved week ago")
        db.setSavedAt(todayPlusDays(-7), 4L)

        val ids = db.selectUnarchived(null).executeAsList().map { it.id }
        assertThat(ids, contains(1L, 3L, 4L, 2L))
    }

    @Test
    fun `selectArchived should order results by archived date starting from today`() {
        insert(1L, "archived 3 days ago")
        db.setArchivedAt(todayPlusDays(-3), 1L)
        insert(2L, "archived 2 weeks ago")
        db.setArchivedAt(todayPlusDays(-14), 2L)
        insert(3L, "archived week ago")
        db.setArchivedAt(todayPlusDays(-7), 3L)
        insert(4L, "archived today")
        db.archiveById(4L)

        val ids = db.selectArchived(null).executeAsList().map { it.id }
        assertThat(ids, contains(4L, 1L, 3L, 2L))
    }

    @Test
    fun `select with filter should return webmarks with matching title or url`() {
        insert(1L, "example.com/kotlin-1", title = "Article about Kotlin")
        insert(2L, "example.com/kotlin-2", title = "Article about Kotlin number 2")
        db.archiveById(2L)
        insert(3L, "example.com/android-1", title = "Article about Android")
        insert(4L, "example.com/android-2", title = "Article about Android number 2")
        db.archiveById(4L)

        val unarchivedIdsByUrl = db.selectUnarchived("kotlin-").executeAsList().map { it.id }
        assertThat(unarchivedIdsByUrl, containsInAnyOrder(1L))

        val unarchivedIdsByTitle =
            db.selectUnarchived("about Android").executeAsList().map { it.id }
        assertThat(unarchivedIdsByTitle, containsInAnyOrder(3L))

        val archivedIdsByUrl = db.selectArchived("kotlin-").executeAsList().map { it.id }
        assertThat(archivedIdsByUrl, containsInAnyOrder(2L))

        val archivedIdsByTitle = db.selectArchived("about Android").executeAsList().map { it.id }
        assertThat(archivedIdsByTitle, containsInAnyOrder(4L))
    }

    @Test
    fun `lastInsertId should return last inserted id`() {
        insert(1L, "aaa")
        assertThat(db.lastInsertId().executeAsOne(), equalTo(1L))

        insert(7L, "bbb")
        insert(4L, "ccc")
        assertThat(db.lastInsertId().executeAsOne(), equalTo(4L))
    }
}
