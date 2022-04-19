package com.nsp.timetracker.support.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nsp.timetracker.R


object BindingAdapterUtils {

    @BindingAdapter("data")
    @JvmStatic
    internal fun TextView.setData(data: Float?) {
        text = data?.round().toString()
    }

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

    @BindingAdapter("startDate", "endDate")
    @JvmStatic
    internal fun TextView.setDuration(startDate: Long, endDate: Long) {
        text = ((endDate - startDate) / 1000).timeFormat()
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
}