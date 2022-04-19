package com.nsp.timetracker.data.db.dao

import androidx.room.*
import com.nsp.timetracker.data.db.model.Project
import kotlinx.coroutines.flow.Flow

@Dao
abstract class ProjectDao {

    @Query("SELECT * FROM Project ORDER BY id ASC")
    abstract fun getAll(): Flow<List<Project>>

    @Query("SELECT * FROM Project WHERE category_id==:categoryId")
    abstract fun getAll(categoryId: Long): Flow<List<Project>>

    @Query("SELECT * FROM Project WHERE id==:id")
    abstract fun get(id: Long): Flow<Project>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(project: Project)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(projects: List<Project>)

    @Update
    abstract fun update(project: Project)

    @Query("delete FROM Project WHERE category_id= :id")
    abstract fun delete(id: Long)

    @Query("delete FROM Project")
    abstract fun deleteAll()

    @Transaction
    open suspend fun replaceAll(items: List<Project>) {
        deleteAll()
        insertAll(items)
    }
}