package com.mohsen.nobka.di

import android.content.Context
import androidx.room.Room
import com.mohsen.nobka.data.local.database.NobkaDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DataBaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app, NobkaDataBase::class.java, "nobka-db"
    ).build()

    @Singleton
    @Provides
    fun provideSahifehDao(db: NobkaDataBase) = db.getDao()

}