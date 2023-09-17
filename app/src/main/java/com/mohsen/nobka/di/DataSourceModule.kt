package com.mohsen.nobka.di

import com.mohsen.nobka.data.local.LocalDataSource
import com.mohsen.nobka.data.local.database.NobkaDao
import com.mohsen.nobka.data.remote.NobkaServices
import com.mohsen.nobka.data.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataSourceModule {

    @Singleton
    @Provides
    fun provideLocalSource(nobkaDao: NobkaDao): LocalDataSource {
        return LocalDataSource(nobkaDao)
    }

    @Singleton
    @Provides
    fun provideRemoteSource(nobkaServices: NobkaServices): RemoteDataSource {
        return RemoteDataSource(nobkaServices)
    }

}