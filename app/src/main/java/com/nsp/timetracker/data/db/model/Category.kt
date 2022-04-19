package com.nsp.timetracker.data.db.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    var name: String,

    var color: Int,
) : Parcelable