package me.thanel.webmark.data

import me.thanel.webmark.data.matcher.isToday
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class WebMarkUpdateTest : BaseDatabaseTest() {

    @Test
    fun `archiveById should archive webmark with today date`() {
        insert(1L, "aaa")

        db.archiveById(1L)

        val webmark = db.selectAll().executeAsOne()
        assertThat(webmark.id, equalTo(1L))
        assertThat(webmark.archivedAt, isToday())
    }

    @Test
    fun `markAsNewById should unarchive webmark`() {
        insert(1L, "aaa")
        db.archiveById(1L)

        db.markAsNewById(1L)

        val webmark = db.selectAll().executeAsOne()
        assertThat(webmark.id, equalTo(1L))
        assertThat(webmark.archivedAt, nullValue())
    }

    @Test
    fun `markAsNewById should reset save time of webmark`() {
        insert(1L, "aaa")
        db.setSavedAt(todayPlusDays(-2), 1L)

        db.markAsNewById(1L)

        val webmark = db.selectAll().executeAsOne()
        assertThat(webmark.savedAt, isToday())
    }

    @Test
    fun `markAsNewById should reset deleted state of webmark`() {
        insert(1L, "aaa")
        db.setMarkedForDeletionById(true, 1L)

        db.markAsNewById(1L)

        val webmark = db.selectAll().executeAsOne()
        assertThat(webmark.markedForDeletion, equalTo(false))
    }

    @Test
    fun `setMarkedForDeletionById should keep webmark in database`() {
        insert(1L, "aaa")

        db.setMarkedForDeletionById(true, 1L)

        val webmark = db.selectAll().executeAsOne()
        assertThat(webmark.id, equalTo(1L))
        assertThat(webmark.markedForDeletion, equalTo(true))
    }

    // TODO:
    //  updateById
    //  unarchiveById
    //  setArchivedAt
    //  setSavedAt
}
