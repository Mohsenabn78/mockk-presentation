package com.mohsen.nobka.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MovieEntity::class], version = 1)
abstract class NobkaDataBase : RoomDatabase() {
    abstract fun getDao(): NobkaDao
}