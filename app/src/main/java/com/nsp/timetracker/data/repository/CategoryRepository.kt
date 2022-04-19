package com.nsp.timetracker.data.repository

import com.nsp.timetracker.data.db.AppDatabase
import com.nsp.timetracker.data.db.model.Category
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    database: AppDatabase,
) {
    private val categoryDao = database.categoryDao
    val categories = categoryDao.getAll()

    fun getCategory(categoryId: Long): Flow<Category> {
        return categoryDao.get(categoryId)
    }

    fun insert(category: Category) {
        categoryDao.insert(category)
    }

    fun insert(name: String, color: Int) {
        categoryDao.insert(Category(name = name, color = color))
    }
}