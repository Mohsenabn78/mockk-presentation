package com.mohsen.nobka.data.local

import com.mohsen.nobka.core.data.local.BaseLocalDataSource
import com.mohsen.nobka.data.local.database.MovieEntity
import com.mohsen.nobka.data.local.database.NobkaDao
import com.mohsen.nobka.domain.model.NobkaMoviesModel
import com.mohsen.nobka.domain.model.toEntity

class LocalDataSource(private val nobkaDao: NobkaDao) : BaseLocalDataSource() {

    suspend fun getMoviesList(): List<MovieEntity> {
        return nobkaDao.getMoviesList()
    }

    suspend fun saveMovieList(list: List<NobkaMoviesModel>) {
        nobkaDao.saveMoviesList(list.map { it.toEntity() })
    }
}