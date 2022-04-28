package com.nsp.timetracker.ui.adddata

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nsp.timetracker.data.db.model.Category
import com.nsp.timetracker.data.repository.CategoryRepository
import com.nsp.timetracker.data.repository.ProjectRepository
import com.nsp.timetracker.support.extensions.safeLet
import com.nsp.timetracker.support.tools.NavigationCommand
import com.nsp.timetracker.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddViewModel @Inject constructor(
    application: Application,
    private val categoryRepository: CategoryRepository,
    private val projectRepository: ProjectRepository,
) : BaseViewModel(application) {

    val name = MutableLiveData<String>()
    val selectedColor = MutableLiveData<Int>()

    fun addCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            safeLet(name.value, selectedColor.value) { name, selectedColor ->
                categoryRepository.insert(name, selectedColor)
                navigate(NavigationCommand.Back)
            }
        }
    }

    fun addProject(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            safeLet(
                name.value,
                category,
                selectedColor.value
            ) { name, category, selectedColor ->
                projectRepository.insert(name, category, selectedColor)
                navigate(NavigationCommand.Back)
            }
        }
    }
}
