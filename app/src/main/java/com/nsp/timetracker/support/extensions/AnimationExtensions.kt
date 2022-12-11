package com.nsp.timetracker.support.extensions

import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.core.view.isVisible
import com.nsp.timetracker.support.listener.EmptyAnimationListener

private val EXPAND_ANIMATION_DURATION: Int = 400
private const val SCALE_EXPANDED = 1f
private const val SCALE_COLLAPSED = 0f

fun View.expandCollapse() {
    if (isVisible) collapse(this)
    else expand(this)
}

fun expand(view: View) {
    view.isVisible = true

    val animation = ScaleAnimation(
        SCALE_EXPANDED,
        SCALE_EXPANDED,
        SCALE_COLLAPSED,
        SCALE_EXPANDED
    )
    animation.duration = EXPAND_ANIMATION_DURATION.toLong()
    animation.fillAfter = true
    view.startAnimation(animation)
}

fun collapse(view: View) {
    val animation = ScaleAnimation(
        SCALE_EXPANDED,
        SCALE_EXPANDED,
        SCALE_EXPANDED,
        SCALE_COLLAPSED
    )
    animation.duration = EXPAND_ANIMATION_DURATION.toLong()
    animation.fillAfter = true

    animation.setAnimationListener(object : EmptyAnimationListener() {
        override fun onAnimationEnd(animation: Animation) {
            view.isVisible = false
        }
    })

    view.startAnimation(animation)
}
