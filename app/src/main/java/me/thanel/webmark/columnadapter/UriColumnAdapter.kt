package me.thanel.webmark.columnadapter

import android.net.Uri
import com.squareup.sqldelight.ColumnAdapter

object UriColumnAdapter : ColumnAdapter<Uri, String> {
    override fun decode(databaseValue: String): Uri = Uri.parse(databaseValue)
    override fun encode(value: Uri): String = value.toString()
}
