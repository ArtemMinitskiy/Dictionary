package com.project.dictionary.datastore

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class SettingsDataModule {

    @Provides
    @Singleton
    fun providerFunction(@ApplicationContext appContext: Context): SettingsDataStore {
        return SettingsDataStoreImpl(
            context = appContext,
            config = StoreConfig(
                fileName = ProtoDataStore.SETTINGS_DATA_PROTO,
                SettingsDataSerializer()
            )
        )
    }
}