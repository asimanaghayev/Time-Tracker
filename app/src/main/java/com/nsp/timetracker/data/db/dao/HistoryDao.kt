package com.nsp.timetracker.data.db.dao

import androidx.room.*
import com.nsp.timetracker.data.db.model.History
import kotlinx.coroutines.flow.Flow

@Dao
abstract class HistoryDao {

    @Query("SELECT * FROM History ORDER BY id ASC")
    abstract fun getAll(): Flow<List<History>>

    @Query("SELECT * FROM History WHERE endDate == 0 ORDER BY id ASC")
    abstract fun getAllOngoing(): Flow<List<History>>

    @Query("SELECT * FROM History WHERE endDate > 0 ORDER BY id DESC")
    abstract fun getAllFinished(): Flow<List<History>>

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