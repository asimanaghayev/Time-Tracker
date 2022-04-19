package com.nsp.timetracker.support.tools

import androidx.navigation.NavDirections
import androidx.navigation.Navigator


sealed class NavigationCommand {
    data class To(val directions: NavDirections, val extras: Navigator.Extras? = null) :
        NavigationCommand()

    object Back : NavigationCommand()
    data class BackTo(val destinationId: Int) : NavigationCommand()
}