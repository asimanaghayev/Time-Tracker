package com.nsp.timetracker.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nsp.timetracker.R
import com.nsp.timetracker.data.db.model.Category
import com.nsp.timetracker.data.db.model.History
import com.nsp.timetracker.data.db.model.Project
import com.nsp.timetracker.data.repository.CategoryRepository
import com.nsp.timetracker.data.repository.HistoryRepository
import com.nsp.timetracker.data.repository.ProjectRepository
import com.nsp.timetracker.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    categoryRep: CategoryRepository,
    private val projectRep: ProjectRepository,
    private val historyRep: HistoryRepository,
) : BaseViewModel(application) {

    private val _allCategories: MutableLiveData<List<Category>> = MutableLiveData<List<Category>>()
    val allCategories: LiveData<List<Category>> = _allCategories

    private val _allProjects: MutableLiveData<List<Project>> = MutableLiveData<List<Project>>()
    val allProjects: LiveData<List<Project>> = _allProjects

    private val _ongoingProjects: MutableLiveData<List<History>> = MutableLiveData<List<History>>()
    val ongoingProjects: LiveData<List<History>> = _ongoingProjects

    val category: MutableLiveData<Category> = MutableLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRep.categories.collect {
                _allCategories.postValue(it as MutableList<Category>?)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            historyRep.getOngoingProjects().collect {
                _ongoingProjects.postValue(it as MutableList<History>?)
            }
        }
    }

    fun addCategory() {
        navigate(HomeFragmentDirections.actionToAddCategory())
    }

    fun addProject() {
        category.value?.let {
            navigate(HomeFragmentDirections.actionToAddProject(category = it))
        } ?: run {
            handleMessage(getString(R.string.error_add_category))
        }
    }

    fun navigateToHistory() {
        navigate(HomeFragmentDirections.actionToHistory())
    }

    fun refreshProjects(category: Category) {
        this.category.postValue(category)
        viewModelScope.launch(Dispatchers.IO) {
            projectRep.getProjectsByCategory(categoryId = category.id).collect {
                _allProjects.postValue(it as MutableList<Project>?)
            }
        }
    }

    fun startTimer(project: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRep.insert(History(project = project, startDate = Date().time))
        }
    }

    fun stopTimer(project: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            ongoingProjects.value?.first { it.project == project }?.let {
                stopTimer(it)
            }
        }
    }

    fun stopTimer(history: History) {
        viewModelScope.launch(Dispatchers.IO) {
            history.endDate = Date().time
            historyRep.update(history)
        }
    }
}
