package com.nsp.timetracker.ui.base

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import androidx.navigation.Navigator
import com.nsp.timetracker.R
import com.nsp.timetracker.data.network.exceptions.NetworkError
import com.nsp.timetracker.data.network.exceptions.ServerError
import com.nsp.timetracker.support.tools.NavigationCommand
import com.nsp.timetracker.support.tools.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel @Inject constructor(application: Application) :
    AndroidViewModel(application) {

    val navigationCommands = SingleLiveEvent<NavigationCommand>()

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _commonEffect = SingleLiveEvent<BaseEffect>()
    val commonEffect: LiveData<BaseEffect>
        get() = _commonEffect

    private val _message = MutableLiveData<String>()
    val message: LiveData<String>
        get() = _message

    fun navigate(directions: NavDirections, extras: Navigator.Extras? = null) {
        navigationCommands.postValue(NavigationCommand.To(directions, extras))
    }

    fun navigate(command: NavigationCommand) {
        navigationCommands.postValue(command)
    }

    protected fun handleLoading(state: Boolean) {
        _isLoading.postValue(state)
    }

    protected fun handleError(t: Throwable) {
        Timber.e(t)
        when (t) {
            is ServerError.ServerIsDown -> _commonEffect.postValue(BackEndError())
            is ServerError.Unexpected -> _commonEffect.postValue(MessageError(R.string.error_unexpected))
            is NetworkError -> _commonEffect.postValue(NoInternet())
            else -> _commonEffect.postValue(UnknownError(cause = t))
        }
    }

    protected fun handleMessage(text: String) {
        _message.postValue(text)
    }

    fun getString(@StringRes resId: Int): String {
        return getApplication<Application>().resources.getString(resId)
    }

}