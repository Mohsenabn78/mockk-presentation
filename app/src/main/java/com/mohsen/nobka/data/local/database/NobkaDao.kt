package com.mohsen.nobka.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NobkaDao {
    @Query("SELECT * FROM movieEntity")
    suspend fun getMoviesList(): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMoviesList(movieList: List<MovieEntity>)
}