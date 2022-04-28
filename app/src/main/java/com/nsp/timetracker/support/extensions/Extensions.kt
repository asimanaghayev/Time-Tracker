package com.nsp.timetracker.support.extensions

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.nsp.timetracker.R
import dagger.hilt.android.internal.managers.ViewComponentManager
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

fun TextView.setEndDrawable(@DrawableRes drawableRes: Int) {
    setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableRes, 0)
}

fun Long.timeFormat(): String {
    val hours = this / 3600
    var remainder = this - hours * 3600
    val minutes = remainder / 60
    remainder -= minutes * 60

    return String.format("%02d:%02d:%02d", hours, minutes, remainder)
}

fun Context.activityContext(): Context {
    val context = this
    return if (context is ViewComponentManager.FragmentContextWrapper) {
        context.baseContext
    } else context
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

fun View.showGroupViews(vararg view: View) {
    view.forEach {
        it.isVisible = true
    }
}

fun View.hideGroupViews(vararg view: View) {
    view.forEach {
        it.isVisible = false
    }
}

fun setupStatusBarColor(
    activity: Activity,
    @ColorRes color: Int = R.color.colorPrimary,
    isDarkActionBar: Boolean,
) {
    val window: Window = activity.window
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = ContextCompat.getColor(activity, color)

    window.decorView.systemUiVisibility = if (!isDarkActionBar) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    else View.SYSTEM_UI_FLAG_VISIBLE
}