package com.project.dictionary.firebase

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class FirebaseModule {
    @Binds
    @Singleton
    abstract fun provideFirebaseService(realtimeDatabaseRepositoryImpl: RealtimeDatabaseRepositoryImpl): RealtimeDatabaseRepository
}