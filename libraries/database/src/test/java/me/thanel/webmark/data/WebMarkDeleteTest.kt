package me.thanel.webmark.data

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.containsInAnyOrder
import org.hamcrest.Matchers.empty
import org.junit.Test

class WebMarkDeleteTest : BaseDatabaseTest() {

    // TODO:
    //  deleteEverything

    @Test
    fun `deleteById should delete webmark from database`() {
        insert(1L, "aaa")

        db.deleteById(1L)

        assertThat(db.selectAll().executeAsList(), empty())
    }

    @Test
    fun `cleanup should delete webmarks older than month`() {
        insert(1L, "archived today")
        db.archiveById(1L)
        insert(2L, "archived week ago")
        db.setArchivedAt(todayPlusDays(-7), 2L)
        insert(3L, "archived month ago")
        db.setArchivedAt(todayPlusDays(-30), 3L)
        insert(4L, "archived day and month ago")
        db.setArchivedAt(todayPlusDays(-31), 4L)
        insert(5L, "archived 2 months ago")
        db.setArchivedAt(todayPlusDays(-60), 5L)

        db.cleanup()

        val executeAsList = db.selectAll().executeAsList()
        val leftIds = executeAsList.map { it.id }
        assertThat(leftIds, containsInAnyOrder(1L, 2L, 3L))
    }

    @Test
    fun `cleanup should delete webmarks marked for deletion`() {
        insert(1L, "regular")
        insert(2L, "archived")
        db.archiveById(2L)
        insert(3L, "marked for deletion")
        db.setMarkedForDeletionById(true, 3L)
        insert(4L, "archived and marked for deletion")
        db.archiveById(4L)
        db.setMarkedForDeletionById(true, 4L)

        db.cleanup()

        val leftIds = db.selectAll().executeAsList().map { it.id }
        assertThat(leftIds, containsInAnyOrder(1L, 2L))
    }
}
