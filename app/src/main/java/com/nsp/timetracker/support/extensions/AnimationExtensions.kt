package com.nsp.timetracker.support.extensions

import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.LinearLayout


fun View.expandCollapse() {
    if (visibility == View.VISIBLE) collapse()
    else expand()
}

fun View.expand(animationListener: Animation.AnimationListener? = null) {
    measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    val targetHeight = measuredHeight
    layoutParams.height = 0
    visibility = View.VISIBLE
    val animation: Animation = object : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation,
        ) {
            layoutParams.height =
                if (interpolatedTime == 1f) LinearLayout.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
            requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }
    animation.duration = (targetHeight / context.resources.displayMetrics.density).toLong()
    if (animationListener != null) {
        animation.setAnimationListener(animationListener)
    }
    Handler(Looper.getMainLooper()).postDelayed({
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        requestLayout()
    }, animation.duration)
    startAnimation(animation)
}

fun View.collapse(animationListener: Animation.AnimationListener? = null) {
    val initialHeight = measuredHeight
    val animation: Animation = object : Animation() {
        override fun applyTransformation(
            interpolatedTime: Float,
            t: Transformation,
        ) {
            if (interpolatedTime == 1f) {
                visibility = View.GONE
            } else {
                layoutParams.height =
                    initialHeight - (initialHeight * interpolatedTime).toInt()
                requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }
    animation.duration = (initialHeight / context.resources.displayMetrics.density).toLong()
    if (animationListener != null) {
        animation.setAnimationListener(animationListener)
    }
    Handler(Looper.getMainLooper()).postDelayed({
        layoutParams.height = 0
        requestLayout()
    }, animation.duration)
    startAnimation(animation)
}
