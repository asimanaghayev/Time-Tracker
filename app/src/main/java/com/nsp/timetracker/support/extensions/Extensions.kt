package com.nsp.timetracker.support.util

import android.content.Context
import android.widget.Toast
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Extension functions of the app.
 */
fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun List<String>.toQueryString(): String {
    var result = "";
    for (value in this) {
        result = if (result.isEmpty()) {
            result.plus(value)
        } else {
            result.plus(",$value")
        }
    }
    return result
}

fun Float.round(place: Int = 2): Float {
    return this.toBigDecimal().round(place).toFloat()
}

fun BigDecimal.round(place: Int = 2): BigDecimal {
    return this.setScale(place, RoundingMode.HALF_UP)
}

fun Long.timeFormat(): String {
    val hours = this / 3600
    var remainder = this - hours * 3600
    val minutes = remainder / 60
    remainder -= minutes * 60

    return String.format("%02d:%02d:%02d", hours, minutes, remainder)
}

// 2 variables
fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

// 3 variables
fun <T1 : Any, T2 : Any, T3 : Any, R : Any> safeLet(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    block: (T1, T2, T3) -> R?,
): R? {
    return if (p1 != null && p2 != null && p3 != null) block(p1, p2, p3) else null
}

// 4 variables
fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, R : Any> safeLet(
    p1: T1?,
    p2: T2?,
    p3: T3?,
    p4: T4?,
    block: (T1, T2, T3, T4) -> R?,
): R? {
    return if (p1 != null && p2 != null && p3 != null && p4 != null) block(p1, p2, p3, p4) else null
}