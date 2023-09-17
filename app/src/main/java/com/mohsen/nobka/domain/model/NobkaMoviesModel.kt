package com.mohsen.nobka.domain.model

import com.mohsen.nobka.data.local.database.MovieEntity

data class NobkaMoviesModel(
    val id: Int = 1,
    val title: String = "",
    val poster: String = ""
)

fun NobkaMoviesModel.toEntity(): MovieEntity =
    MovieEntity(
        this.id,
        this.title,
        this.poster
    )