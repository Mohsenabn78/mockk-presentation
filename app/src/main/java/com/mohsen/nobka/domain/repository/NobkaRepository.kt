package com.mohsen.nobka.domain.repository

import com.mohsen.nobka.core.data.remote.error.Failure
import com.mohsen.nobka.core.utils.Either
import com.mohsen.nobka.domain.model.NobkaMoviesModel
import kotlinx.coroutines.flow.Flow


interface NobkaRepository {
    fun getMovies(): Flow<Either<Failure, List<NobkaMoviesModel>>>
}