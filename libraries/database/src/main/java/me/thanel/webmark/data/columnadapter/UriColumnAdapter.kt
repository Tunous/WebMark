package me.thanel.webmark.data.columnadapter

import android.net.Uri
import com.squareup.sqldelight.ColumnAdapter

internal object UriColumnAdapter : ColumnAdapter<Uri, String> {
    override fun decode(databaseValue: String): Uri = Uri.parse(databaseValue)
    override fun encode(value: Uri): String = value.toString()
}
