package com.nsp.timetracker.data.db.dao.model

import android.os.Parcelable
import androidx.room.Embedded
import com.nsp.timetracker.data.db.model.Project
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatisticsProject(

    @Embedded(prefix = "project_")
    val project: Project,
    val sum: Long,
    val date: Long,
) : Parcelable
