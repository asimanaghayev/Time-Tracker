package com.nsp.timetracker.ui.history

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nsp.timetracker.data.db.dao.model.StatisticsCategory
import com.nsp.timetracker.data.db.model.History
import com.nsp.timetracker.data.repository.HistoryRepository
import com.nsp.timetracker.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    application: Application,
    private val historyRep: HistoryRepository,
) : BaseViewModel(application) {

    private val _allHistoryProject: MutableLiveData<List<History>> =
        MutableLiveData<List<History>>()
    val allHistoryProject: LiveData<List<History>> = _allHistoryProject

    private val _allStatisticsCategory: MutableLiveData<List<StatisticsCategory>> =
        MutableLiveData<List<StatisticsCategory>>()
    val allStatisticsCategory: LiveData<List<StatisticsCategory>> = _allStatisticsCategory

    init {
        enumValues<HistoryType>().forEach {
            getHistory(it)
        }
    }

    private fun getHistory(type: HistoryType) {
        viewModelScope.launch(Dispatchers.IO) {
            when (type) {
                HistoryType.PROJECT -> {
                    historyRep.getFinishedProjects().collect {
                        _allHistoryProject.postValue(it as MutableList<History>?)
                    }
                }
                HistoryType.CATEGORY -> {
                    historyRep.getAllFinishedCategory().collect {
                        _allStatisticsCategory.postValue(it as MutableList<StatisticsCategory>?)
                    }
                }
            }
        }
    }
}
