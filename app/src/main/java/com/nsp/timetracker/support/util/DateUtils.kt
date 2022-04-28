package com.nsp.timetracker.support.util

import android.text.format.DateUtils
import com.nsp.timetracker.support.listener.EmptyListener
import timber.log.Timber
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    const val DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss"
    const val DATE_PATTERN = "dd.MM.yyyy"
    const val TIME_PATTERN = "HH:mm"

    fun getFormattedDate(date: Date): String {
        var ago: CharSequence = ""
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        sdf.timeZone = TimeZone.getTimeZone("GMT")
        val time = date.time
        ago = DateUtils.getRelativeTimeSpanString(time)
        return ago.toString()
    }

    fun getDate(milliSeconds: Long, dateFormat: String = "dd.MM.yyyy"): String {
        val formatter = SimpleDateFormat(dateFormat)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    fun setupTimer(listener: EmptyListener): Timer {
        val timer = Timer()
        val timerTask: TimerTask = object : TimerTask() {
            override fun run() {
                listener.onAction()
            }
        }
        timer.schedule(timerTask, 0, 1000)
        return timer
    }

    private fun getDateUnit(date: Date, timeZone: TimeZone, unit: Int): Int {
        val calendar = Calendar.getInstance(timeZone)
        calendar.time = date
        return calendar[unit]
    }

    fun getLocalYear(date: Date): Int {
        return getDateUnit(
            date,
            TimeZone.getDefault(),
            Calendar.YEAR
        )
    }

    fun getLocalMonth(date: Date): Int {
        return getDateUnit(
            date,
            TimeZone.getDefault(),
            Calendar.MONTH
        )
    }

    fun getLocalDayOfMonth(date: Date): Int {
        return getDateUnit(
            date,
            TimeZone.getDefault(),
            Calendar.DAY_OF_MONTH
        )
    }

    fun getLocalHour(date: Date): Int {
        return getDateUnit(date,
            TimeZone.getDefault(),
            Calendar.HOUR_OF_DAY)
    }

    fun getLocalMinutes(date: Date): Int {
        return getDateUnit(date,
            TimeZone.getDefault(),
            Calendar.MINUTE)
    }

    fun parseLocalDate(pattern: String?, date: String?): Date? {
        return parseDate(pattern,
            TimeZone.getDefault(),
            date)
    }

    fun parseDate(pattern: String?, timeZone: TimeZone?, date: String?): Date? {
        return try {
            val format = SimpleDateFormat(pattern, Locale.getDefault())
            format.timeZone = timeZone
            format.parse(date)
        } catch (e: ParseException) {
            Timber.e(e)
            null
        }
    }

    fun getLocalDate(year: Int, month: Int, day: Int, hour: Int, minute: Int): Date? {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        calendar[year, month, day, hour] = minute
        return calendar.time
    }

    fun getLastMonday(calendar: Calendar): Long {
        when (calendar[Calendar.DAY_OF_WEEK]) {
            Calendar.TUESDAY -> calendar.add(Calendar.DAY_OF_MONTH, -1)
            Calendar.WEDNESDAY -> calendar.add(Calendar.DAY_OF_MONTH, -2)
            Calendar.THURSDAY -> calendar.add(Calendar.DAY_OF_MONTH, -3)
            Calendar.FRIDAY -> calendar.add(Calendar.DAY_OF_MONTH, -4)
            Calendar.SATURDAY -> calendar.add(Calendar.DAY_OF_MONTH, -5)
            Calendar.SUNDAY -> calendar.add(Calendar.DAY_OF_MONTH, -6)
        }

        return calendar.time.time
    }

    fun getLocalDateString(date: Date?): String? {
        return getString(
            DATE_PATTERN,
            date,
            TimeZone.getDefault()
        )
    }

    fun getLocalTimeString(date: Date?): String? {
        return getString(
            TIME_PATTERN,
            date,
            TimeZone.getDefault()
        )
    }

    fun getString(pattern: String?, date: Date?, timeZone: TimeZone?): String? {
        if (date == null) {
            return ""
        }
        val format = SimpleDateFormat(pattern, Locale.getDefault())
        format.timeZone = timeZone
        return format.format(date)
    }
}