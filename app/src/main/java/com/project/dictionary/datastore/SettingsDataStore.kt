package com.project.dictionary.datastore

import kotlinx.coroutines.flow.Flow

interface SettingsDataStore {
    suspend fun getSettingsData(): Flow<SettingsData>
    suspend fun updateColorSettings(color: String)
}