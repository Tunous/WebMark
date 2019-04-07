package me.thanel.webmark.data.columnadapter

import com.squareup.sqldelight.ColumnAdapter
import java.util.*

internal object DateColumnAdapter : ColumnAdapter<Date, Long> {
    override fun decode(databaseValue: Long): Date = Date(databaseValue)
    override fun encode(value: Date): Long = value.time
}
