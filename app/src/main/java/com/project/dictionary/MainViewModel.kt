package com.project.dictionary

import androidx.lifecycle.ViewModel
import com.project.dictionary.firebase.RealtimeDatabaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(
    private val _realtimeDatabaseRepository: RealtimeDatabaseRepository
) : ViewModel() {
    fun getListOfWords() = _realtimeDatabaseRepository.fetchWords()
}