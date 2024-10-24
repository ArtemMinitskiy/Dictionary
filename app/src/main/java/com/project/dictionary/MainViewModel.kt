package com.project.dictionary

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.dictionary.datastore.SettingsData
import com.project.dictionary.datastore.SettingsDataStore
import com.project.dictionary.firebase.RealtimeDatabaseRepository
import com.project.dictionary.model.Word
import com.project.dictionary.notifications.ReminderNotificationWorker
import com.project.dictionary.utils.LoadingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(
    private val realtimeDatabaseRepository: RealtimeDatabaseRepository,
    private val application: Application,
    private val settingsDataStore: SettingsDataStore,
) : ViewModel() {

    private val _listOfWords = MutableStateFlow<List<Word>>(listOf())
    var listOfWords: StateFlow<List<Word>> = _listOfWords

    init {
        addObserver()
        viewModelScope.launch(Dispatchers.IO) {
            realtimeDatabaseRepository.fetchWords().collectLatest {
                when {
                    it.isSuccess -> {
                        _listOfWords.value = it.getOrNull() as ArrayList<Word>
                        success("Success")
                    }

                    it.isFailure -> {
                        error("Failure ${it.exceptionOrNull()?.printStackTrace()}")
                    }
                }
            }
        }
    }

    fun getListOfWords(): Flow<Result<List<Word>>> {
        loading()
        return realtimeDatabaseRepository.fetchWords()
    }

    private val app = application

    fun scheduleReminderNotification() {
        ReminderNotificationWorker.schedule(app)
    }

    private val _loadingState = MutableStateFlow<LoadingState>(LoadingState.Loading)
    var loadingState: StateFlow<LoadingState> = _loadingState

    private fun loading() {
        Log.i("mLogLoading", "Loading")
        _loadingState.value = LoadingState.Loading
    }

    private fun success(str: String) {
        Log.i("mLogLoading", "Success $str")
        _loadingState.value = LoadingState.Success(str)
    }

    private fun error(str: String) {
        Log.e("mLogLoading", "Error $str")
        _loadingState.value = LoadingState.Error(str)
    }

    var colorSettingsFlow = MutableStateFlow("")
    val collector = FlowCollector<SettingsData> {
        colorSettingsFlow.emit(it.color)
    }

    private fun addObserver() {
        viewModelScope.launch {
            settingsDataStore.getSettingsData()
                .collectLatest {
                    collector.emit(it)
                }
        }
    }

    fun updateColorSettings(color: String) {
        viewModelScope.launch {
            settingsDataStore.updateColorSettings(color)
        }
    }
}