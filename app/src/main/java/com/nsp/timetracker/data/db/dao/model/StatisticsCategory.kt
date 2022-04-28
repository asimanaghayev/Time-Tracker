package com.nsp.timetracker.data.db.dao.model

import android.os.Parcelable
import androidx.room.Embedded
import com.nsp.timetracker.data.db.model.Category
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatisticsCategory(

    @Embedded(prefix = "category_")
    val category: Category,
    val sum: Long,
    val date: Long,
) : Parcelable
