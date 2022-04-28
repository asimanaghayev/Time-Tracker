package com.nsp.timetracker.ui.statistics

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nsp.timetracker.data.db.dao.model.StatisticsCategory
import com.nsp.timetracker.data.db.dao.model.StatisticsProject
import com.nsp.timetracker.data.repository.HistoryRepository
import com.nsp.timetracker.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticsItemViewModel @Inject constructor(
    application: Application,
    private val historyRep: HistoryRepository,
) : BaseViewModel(application) {

    private val _projectStats: MutableLiveData<List<StatisticsProject>> =
        MutableLiveData<List<StatisticsProject>>()
    val projectStats: LiveData<List<StatisticsProject>> = _projectStats

    private val _categoryStats: MutableLiveData<List<StatisticsCategory>> =
        MutableLiveData<List<StatisticsCategory>>()
    val categoryStats: LiveData<List<StatisticsCategory>> = _categoryStats

    fun fetchStatistics(type: StatisticsItemFragment.Timing) {
        viewModelScope.launch(Dispatchers.IO) {
            when (type) {
                StatisticsItemFragment.Timing.DAILY -> fetchDailyReport()
                StatisticsItemFragment.Timing.WEEKLY -> fetchWeeklyReport()
                StatisticsItemFragment.Timing.MONTHLY -> fetchMonthlyReport()
            }
        }
    }

    private fun fetchDailyReport() {
        viewModelScope.launch(Dispatchers.IO) {
            historyRep.getDailyProjectReport().collect {
                _projectStats.postValue(it)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            historyRep.getDailyCategoryReport().collect {
                _categoryStats.postValue(it)
            }
        }
    }

    private fun fetchWeeklyReport() {
        viewModelScope.launch(Dispatchers.IO) {
            historyRep.getWeeklyProjectReport().collect {
                _projectStats.postValue(it)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            historyRep.getWeeklyCategoryReport().collect {
                _categoryStats.postValue(it)
            }
        }
    }

    private fun fetchMonthlyReport() {
        viewModelScope.launch(Dispatchers.IO) {
            historyRep.getMonthlyProjectReport().collect {
                _projectStats.postValue(it)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            historyRep.getMonthlyCategoryReport().collect {
                _categoryStats.postValue(it)
            }
        }
    }
}
