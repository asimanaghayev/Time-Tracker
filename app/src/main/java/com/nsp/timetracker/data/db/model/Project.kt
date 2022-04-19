package com.nsp.timetracker.data.db.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Project(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    var name: String,

    @Embedded(prefix = "category_")
    val category: Category,

    @ColumnInfo(defaultValue = "CURRENT_TIMESTAMP")
    var createdDate: Long,

    var color: Int,
) : Parcelable