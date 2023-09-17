package com.mohsen.nobka.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mohsen.nobka.domain.model.NobkaMoviesModel

@Entity
data class MovieEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val title: String,
    val poster: String
)

fun MovieEntity.mapToDomain(): NobkaMoviesModel =
    NobkaMoviesModel(this.id, this.title, this.poster)