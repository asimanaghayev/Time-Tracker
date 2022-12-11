package com.nsp.timetracker.support.listener

import android.view.animation.Animation

open class EmptyAnimationListener : Animation.AnimationListener {
    override fun onAnimationStart(animation: Animation) {}
    override fun onAnimationEnd(animation: Animation) {}
    override fun onAnimationRepeat(animation: Animation) {}
}