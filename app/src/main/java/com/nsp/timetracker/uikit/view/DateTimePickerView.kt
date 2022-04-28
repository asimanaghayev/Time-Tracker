package com.nsp.timetracker.uikit.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.nsp.timetracker.databinding.ViewDateTimePickerBinding
import com.nsp.timetracker.support.extensions.activityContext
import com.nsp.timetracker.support.util.DateUtils
import com.nsp.timetracker.support.util.DateUtils.getLocalDate
import com.nsp.timetracker.support.util.DateUtils.getLocalDateString
import com.nsp.timetracker.support.util.DateUtils.getLocalDayOfMonth
import com.nsp.timetracker.support.util.DateUtils.getLocalHour
import com.nsp.timetracker.support.util.DateUtils.getLocalMinutes
import com.nsp.timetracker.support.util.DateUtils.getLocalMonth
import com.nsp.timetracker.support.util.DateUtils.getLocalTimeString
import com.nsp.timetracker.support.util.DateUtils.getLocalYear
import com.nsp.timetracker.support.util.DateUtils.parseLocalDate
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog
import java.util.*

class DateTimePickerView : LinearLayout {
    var binding = ViewDateTimePickerBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
        init()
    }

    private fun init() {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT)
        orientation = HORIZONTAL
        date = Date()
        setupClicks()
    }

    private fun setupClicks() {
        binding.labelDate.setOnClickListener { onPickDate() }
        binding.labelTime.setOnClickListener { onPickTime() }
    }

    private fun onPickDate() {
        val selectedDate = date
        val datePickerDialog = DatePickerDialog.newInstance(
            { _: DatePickerDialog?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                updateDate(year,
                    monthOfYear,
                    dayOfMonth)
            },
            getLocalYear(selectedDate!!),
            getLocalMonth(selectedDate),
            getLocalDayOfMonth(selectedDate)
        )
        datePickerDialog.show((context.activityContext() as AppCompatActivity).supportFragmentManager,
            "DatePickerDialog")
    }

    private fun onPickTime() {
        val selectedDate = date
        val timePickerDialog = TimePickerDialog.newInstance(
            { _: TimePickerDialog?, hourOfDay: Int, minute: Int, _: Int ->
                updateTime(hourOfDay,
                    minute)
            },
            getLocalHour(selectedDate!!),
            getLocalMinutes(selectedDate),
            true
        )
        timePickerDialog.show((context.activityContext() as AppCompatActivity).supportFragmentManager,
            "TimePickerDialog")
    }

    private fun updateTime(hour: Int, minute: Int) {
        var date = date
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = hour
        calendar[Calendar.MINUTE] = minute
        this.date = calendar.time
    }

    private fun updateDate(year: Int, month: Int, dayOfMonth: Int) {
        var date = date
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[year, month] = dayOfMonth
        this.date = calendar.time
    }

    var date: Date?
        get() {
            val date = parseLocalDate(DateUtils.DATE_PATTERN, binding.labelDate.text.toString())
            val year = getLocalYear(date!!)
            val month = getLocalMonth(date)
            val day = getLocalDayOfMonth(date)
            val time = parseLocalDate(DateUtils.TIME_PATTERN, binding.labelTime.text.toString())
            val hour = getLocalHour(time!!)
            val minute = getLocalMinutes(time)
            return getLocalDate(year, month, day, hour, minute)
        }
        set(date) {
            requireNotNull(date) { "Date can't be null" }
            binding.labelDate.text = getLocalDateString(date)
            binding.labelTime.text = getLocalTimeString(date)
        }
}