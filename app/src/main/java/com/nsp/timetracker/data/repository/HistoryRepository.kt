package com.nsp.timetracker.data.repository

import com.nsp.timetracker.data.db.AppDatabase
import com.nsp.timetracker.data.db.dao.model.StatisticsCategory
import com.nsp.timetracker.data.db.dao.model.StatisticsProject
import com.nsp.timetracker.data.db.model.History
import com.nsp.timetracker.support.util.DateUtils
import kotlinx.coroutines.flow.Flow
import java.util.*
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
        return historyDao.getFinishedProjects()
    }

    fun getAllFinishedCategory(): Flow<List<StatisticsCategory>> {
        return historyDao.getFinishedCategory()
    }

    fun getDailyProjectReport(): Flow<List<StatisticsProject>> {
        return historyDao.getDailyProjectReport()
    }

    fun getDailyCategoryReport(): Flow<List<StatisticsCategory>> {
        return historyDao.getDailyCategoryReport()
    }

    fun getWeeklyProjectReport(): Flow<List<StatisticsProject>> {
        return historyDao.getWeeklyProjectReport()
    }

    fun getWeeklyCategoryReport(): Flow<List<StatisticsCategory>> {
        return historyDao.getWeeklyCategoryReport()
    }

    fun getMonthlyProjectReport(): Flow<List<StatisticsProject>> {
        return historyDao.getMonthlyProjectReport()
    }

    fun getMonthlyCategoryReport(): Flow<List<StatisticsCategory>> {
        return historyDao.getMonthlyCategoryReport()
    }

    fun insert(history: History) {
        val calendar = Calendar.getInstance()
        calendar.time = Date(history.startDate)

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        history.weekDate = DateUtils.getLastMonday(calendar)

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        history.monthDate = calendar.time.time

        historyDao.insert(history)
    }

    fun update(history: History) {
        historyDao.update(history)
    }
}