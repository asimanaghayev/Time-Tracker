package com.nsp.timetracker.data.repository

import com.nsp.timetracker.data.db.AppDatabase
import com.nsp.timetracker.data.db.model.Category
import com.nsp.timetracker.data.db.model.Project
import kotlinx.coroutines.flow.Flow
import java.util.*
import javax.inject.Inject

class ProjectRepository @Inject constructor(
    database: AppDatabase,
) {
    private val projectDao = database.projectDao

    val projects get() = projectDao.getAll()

    fun getProject(id: Long): Flow<Project> {
        return projectDao.get(id)
    }

    fun getProjectsByCategory(categoryId: Long): Flow<List<Project>> {
        return projectDao.getAll(categoryId)
    }

    fun insert(project: Project) {
        projectDao.insert(project)
    }

    fun insert(name: String, category: Category, color: Int) {
        projectDao.insert(Project(
            name = name,
            category = category,
            createdDate = Date().time,
            color = color
        ))
    }
}