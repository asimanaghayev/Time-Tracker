package com.nsp.timetracker.support.util

import android.text.format.DateUtils
import com.nsp.timetracker.support.listener.EmptyListener
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

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

}