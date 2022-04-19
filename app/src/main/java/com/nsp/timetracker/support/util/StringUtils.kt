package com.nsp.timetracker.support.util

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan

object StringUtils {

    fun updateBoldSpanWithBoldFont(text: CharSequence, color: Int): SpannableString {
        val new = SpannableString(text)
        val spans = new.getSpans(0, new.length, StyleSpan::class.java)
        for (boldSpan in spans) {
            val start: Int = new.getSpanStart(boldSpan)
            val end: Int = new.getSpanEnd(boldSpan)
            new.setSpan(TypefaceSpan("gilroy_bold"), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            new.setSpan(ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return new
    }

}