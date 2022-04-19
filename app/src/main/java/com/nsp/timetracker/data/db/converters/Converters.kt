package com.nsp.timetracker.data.db.converters

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.sql.Date

object Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromLong(value: Long?): BigDecimal? {
        return value?.let {
            BigDecimal(it).divide(BigDecimal(100))
        }
    }

    @TypeConverter
    fun toLong(value: BigDecimal?): Long? {
        return value?.multiply(BigDecimal(100))?.toLong()
    }
}
