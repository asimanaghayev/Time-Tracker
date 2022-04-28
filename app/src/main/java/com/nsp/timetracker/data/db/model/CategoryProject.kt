package com.nsp.timetracker.data.db.model

import android.os.Parcelable
import androidx.room.Embedded
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryProject(
    @Embedded
    val category: Category,

    val projects: List<Project>,
) : Parcelable