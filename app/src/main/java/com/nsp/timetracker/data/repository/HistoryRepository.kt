package com.nsp.timetracker.data.repository

import com.nsp.timetracker.data.db.AppDatabase
import com.nsp.timetracker.data.db.model.History
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistoryRepository @Inject constructor(
    database: AppDatabase,
) {
    private val historyDao = database.historyDao
    val histories = historyDao.getAll()

    fun getOngoingProjects(): Flow<List<History>> {
        return historyDao.getAllOngoing()
    }

    fun getFinishedProjects(): Flow<List<History>> {
        return historyDao.getAllFinished()
    }

    fun insert(history: History) {
        historyDao.insert(history)
    }

    fun update(history: History) {
        historyDao.update(history)
    }
}