package com.mohsen.nobka.di

import com.mohsen.nobka.data.remote.NobkaServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ServiceModule {

    @Singleton
    @Provides
    fun providesService(retrofit: Retrofit): NobkaServices =
        retrofit.create(NobkaServices::class.java)

}