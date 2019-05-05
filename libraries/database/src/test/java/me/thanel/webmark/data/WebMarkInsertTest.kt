package me.thanel.webmark.data

import android.database.sqlite.SQLiteConstraintException
import android.net.Uri
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test

class WebMarkInsertTest : BaseDatabaseTest() {

    @Test
    fun `insert should insert id and link`() {
        val url = Uri.parse("example.com")
        val id = 5L
        db.insert(id, url)

        val webmark = db.selectAll().executeAsOne()
        MatcherAssert.assertThat(webmark.id, CoreMatchers.equalTo(id))
        MatcherAssert.assertThat(webmark.url, CoreMatchers.equalTo(url))
    }

    @Test(expected = SQLiteConstraintException::class)
    fun `insert should disallow duplicate links`() {
        db.insert(1L, Uri.parse("example.com"))
        db.insert(2L, Uri.parse("example.com"))
    }

    @Test(expected = SQLiteConstraintException::class)
    fun `insert should disallow duplicate ids`() {
        db.insert(1L, Uri.parse("example.com"))
        db.insert(1L, Uri.parse("example.com/2"))
    }
}
