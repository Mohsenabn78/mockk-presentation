package com.mohsen.nobka.di

import com.mohsen.nobka.data.local.LocalDataSource
import com.mohsen.nobka.data.remote.RemoteDataSource
import com.mohsen.nobka.data.repository.NobkaRepositoryImpl
import com.mohsen.nobka.domain.repository.NobkaRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ): NobkaRepository {
        return NobkaRepositoryImpl(remoteDataSource, localDataSource)
    }

}