package com.project.dictionary.firebase

import com.project.dictionary.model.Word
import kotlinx.coroutines.flow.Flow

interface RealtimeDatabaseRepository {
    fun fetchNotifications() : Flow<Result<List<Word>>>
}