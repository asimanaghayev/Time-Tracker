package com.nsp.timetracker.data.db.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class History(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @Embedded(prefix = "project_")
    val project: Project,

    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    var startDate: Long,

    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    var endDate: Long = 0,
) : Parcelable
