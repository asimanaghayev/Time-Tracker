package com.nsp.timetracker.data.repository

import com.nsp.timetracker.data.db.AppDatabase
import com.nsp.timetracker.data.db.model.Category
import com.nsp.timetracker.data.db.model.CategoryProject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    database: AppDatabase,
    private val projectRepo: ProjectRepository,
) {
    private val categoryDao = database.categoryDao
    private val projectDao = database.projectDao
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