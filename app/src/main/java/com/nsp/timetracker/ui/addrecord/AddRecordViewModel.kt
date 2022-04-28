package com.nsp.timetracker.ui.addrecord

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
import com.nsp.timetracker.support.tools.NavigationCommand
import com.nsp.timetracker.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddRecordViewModel @Inject constructor(
    application: Application,
    projectRep: ProjectRepository,
    categoryRep: CategoryRepository,
    private val historyRepo: HistoryRepository,
) : BaseViewModel(application) {

    private val _categoryProjects: MutableLiveData<List<CategoryProject>> =
        MutableLiveData<List<CategoryProject>>()
    val categoryProjects: LiveData<List<CategoryProject>> = _categoryProjects

    private val categories: MutableLiveData<List<Category>> = MutableLiveData()
    private val projects: MutableLiveData<List<Project>> = MutableLiveData()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRep.categories.collect {
                categories.postValue(it)
                delay(300)
                refreshData()
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            projectRep.projects.collect {
                projects.postValue(it)
                delay(300)
                refreshData()
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

    fun addNewRecord(project: Project, startDate: Date, endDate: Date) {
        viewModelScope.launch(Dispatchers.IO) {
            historyRepo.insert(History(
                project = project,
                startDate = startDate.time,
                endDate = endDate.time
            ))
            navigate(NavigationCommand.Back)
        }
    }

    fun editRecord(history: History, startDate: Date, endDate: Date) {
        viewModelScope.launch(Dispatchers.IO) {
            history.startDate = startDate.time
            history.endDate = endDate.time
            historyRepo.update(history)
            navigate(NavigationCommand.Back)
        }
    }
}
