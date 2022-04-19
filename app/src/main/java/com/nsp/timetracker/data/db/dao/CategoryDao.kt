package com.nsp.timetracker.data.db.dao

import androidx.room.*
import com.nsp.timetracker.data.db.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
abstract class CategoryDao {

    @Query("SELECT * FROM Category ORDER BY id ASC")
    abstract fun getAll(): Flow<List<Category>>

    @Query("SELECT * FROM Category WHERE id==:id")
    abstract fun get(id: Long): Flow<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(categories: List<Category>)

    @Update
    abstract fun update(category: Category)

    @Query("delete FROM Category WHERE id= :categoryId")
    abstract fun delete(categoryId: Long)

    @Query("delete FROM Category")
    abstract fun deleteAll()

    @Transaction
    open suspend fun replaceAll(items: List<Category>) {
        deleteAll()
        insertAll(items)
    }
}