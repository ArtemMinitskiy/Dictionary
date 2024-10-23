package com.project.dictionary.datastore

import android.content.Context
import android.util.Log
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

@Module
@InstallIn(SingletonComponent::class)
class SettingsDataStoreImpl(
    context: Context,
    config: StoreConfig<SettingsData>
) : SettingsDataStore, ProtoDataStore<SettingsData>(context, config) {
    private val dataStore = this.protoDataStore

    override suspend fun getSettingsData(): StateFlow<SettingsData> {
        return dataStore.data.stateIn(
            CoroutineScope(Dispatchers.IO),
        )
    }

    override suspend fun updateColorSettings(color: String) {
        Log.e("mLogSettings", "updateColorSettings: $color")
        dataStore.updateData { md ->
            md.copy(
                color = color
            )
        }
    }

}