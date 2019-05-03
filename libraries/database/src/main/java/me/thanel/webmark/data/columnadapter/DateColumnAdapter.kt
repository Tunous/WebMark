package me.thanel.webmark.data.columnadapter

import com.squareup.sqldelight.ColumnAdapter
import java.util.Date

internal object DateColumnAdapter : ColumnAdapter<Date, Long> {
    override fun decode(databaseValue: Long): Date = Date(databaseValue * 1000)
    override fun encode(value: Date): Long = value.time / 1000
}
