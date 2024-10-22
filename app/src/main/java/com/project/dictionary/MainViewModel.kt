package com.project.dictionary

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import com.project.dictionary.firebase.RealtimeDatabaseRepository
import com.project.dictionary.model.Word
import com.project.dictionary.notifications.ReminderNotificationWorker
import com.project.dictionary.utils.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(
    private val realtimeDatabaseRepository: RealtimeDatabaseRepository,
    private val application: Application
) : ViewModel() {

    fun getListOfWords(): Flow<Result<List<Word>>> {
        loading()
        return realtimeDatabaseRepository.fetchWords()
    }

    private val app = application

    fun scheduleReminderNotification() {
        ReminderNotificationWorker.schedule(app)
    }

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Idle)
    var loadingState: StateFlow<LoadingState> = _loadingState

    fun reset() {
        _loadingState.value = LoadingState.Idle
    }

    fun loading() {
        Log.i("mLogLoading", "Loading")
        _loadingState.value = LoadingState.Loading
    }

    fun success(str: String) {
        Log.i("mLogLoading", "Success $str")
        _loadingState.value = LoadingState.Success(str)
    }

    fun error(str: String) {
        Log.e("mLogLoading", "Error $str")
        _loadingState.value = LoadingState.Error(str)
    }

}