package com.nsp.timetracker.ui.home

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nsp.timetracker.data.db.model.Category
import com.nsp.timetracker.data.db.model.CategoryProject
import com.nsp.timetracker.data.db.model.History
import com.nsp.timetracker.data.db.model.Project
import com.nsp.timetracker.data.repository.CategoryRepository
import com.nsp.timetracker.data.repository.HistoryRepository
import com.nsp.timetracker.data.repository.ProjectRepository
import com.nsp.timetracker.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    projectRep: ProjectRepository,
    categoryRep: CategoryRepository,
    private val historyRep: HistoryRepository,
) : BaseViewModel(application) {

    private val _categoryProjects: MutableLiveData<List<CategoryProject>> = MutableLiveData()
    val categoryProjects: LiveData<List<CategoryProject>> = _categoryProjects

    private val _ongoingProjects: MutableLiveData<List<History>> = MutableLiveData()
    val ongoingProjects: LiveData<List<History>> = _ongoingProjects

    private val categories: MutableLiveData<List<Category>> = MutableLiveData()
    private val projects: MutableLiveData<List<Project>> = MutableLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            projectRep.projects.collectLatest {
                projects.postValue(it)
                refreshData()
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            categoryRep.categories.collectLatest {
                categories.postValue(it)
                refreshData()
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            historyRep.getOngoingProjects().collect {
                _ongoingProjects.postValue(it)
            }
        }
    }

    private fun refreshData() {
        val data = arrayListOf<CategoryProject>()

        categories.value?.forEach { category ->
            data.add(CategoryProject(
                category,
                (projects.value ?: listOf())
                    .filter { project -> project.category.id == category.id }
            ))
        }
        _categoryProjects.postValue(data)
    }

    fun addCategory() {
        navigate(HomeFragmentDirections.actionToAddCategory())
    }

    fun addProject(category: Category) {
        navigate(HomeFragmentDirections.actionToAddProject(category = category))
    }

    fun navigateToHistory() {
        navigate(HomeFragmentDirections.actionToHistory())
    }

    fun startTimer(project: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRep.insert(History(project = project, startDate = Date().time))
        }
    }

    fun stopTimer(project: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            ongoingProjects.value?.filter { it.project == project }?.let { histories ->
                histories.forEach { stopTimer(it) }
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
