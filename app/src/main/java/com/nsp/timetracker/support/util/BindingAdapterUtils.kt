package com.nsp.timetracker.support.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nsp.timetracker.R
import com.nsp.timetracker.support.extensions.round
import com.nsp.timetracker.support.extensions.timeFormat
import com.nsp.timetracker.uikit.view.DateTimePickerView
import java.util.*


object BindingAdapterUtils {

    @BindingAdapter("percent")
    @JvmStatic
    internal fun TextView.setPercent(data: Float?) {
        var displayText = ""
        data?.let {
            val percent = it.round()
            when {
                percent < 0 -> {
                    displayText = "▼"
                    setBackgroundResource(R.drawable.bg_percent_red)
                }
                percent > 0 -> {
                    displayText = "▲"
                    setBackgroundResource(R.drawable.bg_percent_green)
                }
                else -> {
                    displayText = ""
                    setBackgroundResource(R.drawable.bg_percent_gray)
                }
            }
            displayText = displayText.plus(percent.toString()).plus("%")
        }
        text = displayText
    }

    @BindingAdapter("date")
    @JvmStatic
    internal fun TextView.setDuration(date: Long) {
        text = (date / 1000).timeFormat()
    }

    @BindingAdapter("startDate", "endDate")
    @JvmStatic
    internal fun TextView.setDuration(startDate: Long, endDate: Long) {
        setDuration(endDate - startDate)
    }

    @BindingAdapter("imageUrl")
    @JvmStatic
    internal fun ImageView.setImageUrl(imageUrl: String?) {
        imageUrl?.let {
            Glide.with(this)
                .load(imageUrl)
                .apply(RequestOptions()
                    .fitCenter())
                .into(this)
        }
    }

    @BindingAdapter("date")
    @JvmStatic
    internal fun DateTimePickerView.setDate(date: Long?) {
        date?.let { this.date = Date(it) }
    }
}