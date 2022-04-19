package com.nsp.timetracker.ui.history

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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
    historyRep: HistoryRepository,
) : BaseViewModel(application) {

    private val _allHistory: MutableLiveData<List<History>> = MutableLiveData<List<History>>()
    val allHistory: LiveData<List<History>> = _allHistory

    init {

        viewModelScope.launch(Dispatchers.IO) {
            historyRep.getFinishedProjects().collect {
                _allHistory.postValue(it as MutableList<History>?)
            }
        }

    }
}
