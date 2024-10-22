package com.project.dictionary.utils

sealed class LoadingState {
    object Idle : LoadingState()
    object Loading : LoadingState()
    data class Success(val data: String) : LoadingState()
    data class Error(val error: String) : LoadingState()
}