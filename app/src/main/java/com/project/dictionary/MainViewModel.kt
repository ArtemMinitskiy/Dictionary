package com.project.dictionary

import android.app.Application
import androidx.lifecycle.ViewModel
import com.project.dictionary.firebase.RealtimeDatabaseRepository
import com.project.dictionary.notifications.ReminderNotificationWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(
    private val realtimeDatabaseRepository: RealtimeDatabaseRepository,
    private val application: Application
) : ViewModel() {
    fun getListOfWords() = realtimeDatabaseRepository.fetchWords()

    private val app = application

    fun scheduleReminderNotification() {
        ReminderNotificationWorker.schedule(app)
    }
}