package com.nsp.timetracker.data.db.dao

import androidx.room.*
import com.nsp.timetracker.data.db.dao.model.StatisticsCategory
import com.nsp.timetracker.data.db.dao.model.StatisticsProject
import com.nsp.timetracker.data.db.model.History
import kotlinx.coroutines.flow.Flow

@Dao
abstract class HistoryDao {

    val DAY_MILLIS = 1000 * 60 * 60 * 24

    @Query("SELECT * FROM History ORDER BY startDate ASC")
    abstract fun getAll(): Flow<List<History>>

    @Query("SELECT * FROM History WHERE endDate == 0 ORDER BY startDate ASC")
    abstract fun getAllOngoing(): Flow<List<History>>

    @Query("SELECT * FROM History WHERE endDate > 0 ORDER BY startDate DESC")
    abstract fun getFinishedProjects(): Flow<List<History>>

    @Query("SELECT project_category_id as category_id, project_category_name as category_name, project_category_color as category_color, startDate as date, SUM(endDate - startDate) as sum " +
            "FROM History WHERE endDate > 0 " +
            "GROUP BY project_category_id, startDate / (1000*60*60*24) " +
            "ORDER BY startDate DESC")
    abstract fun getFinishedCategory(): Flow<List<StatisticsCategory>>

    @Query("SELECT *, startDate as date, SUM(endDate - startDate) as sum " +
            "FROM History WHERE endDate > 0 " +
            "GROUP BY project_id, startDate / (1000*60*60*24) " +
            "ORDER BY startDate DESC")
    abstract fun getDailyProjectReport(): Flow<List<StatisticsProject>>

    @Query("SELECT project_category_id as category_id, project_category_name as category_name, project_category_color as category_color, startDate as date, SUM(endDate - startDate) as sum " +
            "FROM History WHERE endDate > 0 " +
            "GROUP BY project_category_id, startDate / (1000*60*60*24) " +
            "ORDER BY startDate DESC")
    abstract fun getDailyCategoryReport(): Flow<List<StatisticsCategory>>

    @Query("SELECT *, weekDate as date, SUM(endDate - startDate) as sum " +
            "FROM History WHERE endDate > 0 " +
            "GROUP BY weekDate, project_id ORDER BY date DESC")
    abstract fun getWeeklyProjectReport(): Flow<List<StatisticsProject>>

    @Query("SELECT project_category_id as category_id, project_category_name as category_name, project_category_color as category_color, startDate as date, SUM(endDate - startDate) as sum " +
            "FROM History WHERE endDate > 0 " +
            "GROUP BY weekDate, project_category_id ORDER BY date DESC")
    abstract fun getWeeklyCategoryReport(): Flow<List<StatisticsCategory>>

    @Query("SELECT *, monthDate as date, SUM(endDate - startDate) as sum " +
            "FROM History WHERE endDate > 0 " +
            "GROUP BY monthDate, project_id ORDER BY date DESC")
    abstract fun getMonthlyProjectReport(): Flow<List<StatisticsProject>>

    @Query("SELECT project_category_id as category_id, project_category_name as category_name, project_category_color as category_color, startDate as date, SUM(endDate - startDate) as sum " +
            "FROM History WHERE endDate > 0 " +
            "GROUP BY monthDate, project_category_id ORDER BY date DESC")
    abstract fun getMonthlyCategoryReport(): Flow<List<StatisticsCategory>>

    @Query("SELECT * FROM History WHERE id==:id")
    abstract fun get(id: Long): Flow<History>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(history: History)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(histories: List<History>)

    @Update
    abstract fun update(history: History)

    @Query("delete FROM History WHERE id= :id")
    abstract fun delete(id: Long)

    @Query("delete FROM History")
    abstract fun deleteAll()

    @Transaction
    open suspend fun replaceAll(items: List<History>) {
        deleteAll()
        insertAll(items)
    }
}