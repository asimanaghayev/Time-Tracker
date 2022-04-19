package com.nsp.timetracker.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.nsp.timetracker.base.Constants
import com.nsp.timetracker.data.db.converters.Converters
import com.nsp.timetracker.data.db.dao.CategoryDao
import com.nsp.timetracker.data.db.dao.HistoryDao
import com.nsp.timetracker.data.db.dao.ProjectDao
import com.nsp.timetracker.data.db.model.Category
import com.nsp.timetracker.data.db.model.History
import com.nsp.timetracker.data.db.model.Project


@Database(version = 1, entities = [Project::class, Category::class, History::class])
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val categoryDao: CategoryDao
    abstract val projectDao: ProjectDao
    abstract val historyDao: HistoryDao

    companion object {

        @Volatile
        private lateinit var INSTANCE: AppDatabase

        fun getDatabase(
            context: Context,
        ): AppDatabase {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE
        }
    }
}