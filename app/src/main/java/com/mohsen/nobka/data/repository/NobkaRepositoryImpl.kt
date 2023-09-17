package com.mohsen.nobka.data.repository

import com.mohsen.nobka.core.data.remote.error.Failure
import com.mohsen.nobka.core.data.remote.networkBoundResource
import com.mohsen.nobka.core.utils.Either
import com.mohsen.nobka.data.local.LocalDataSource
import com.mohsen.nobka.data.local.database.mapToDomain
import com.mohsen.nobka.data.remote.RemoteDataSource
import com.mohsen.nobka.domain.model.NobkaMoviesModel
import com.mohsen.nobka.domain.repository.NobkaRepository
import kotlinx.coroutines.flow.Flow

class NobkaRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : NobkaRepository {

    override fun getMovies(): Flow<Either<Failure, List<NobkaMoviesModel>>> =
        networkBoundResource(
            fetchFromLocal = {
                localDataSource.getMoviesList().map { it.mapToDomain() }
            },
            shouldFetchFromRemote = { true },
            fetchFromRemote = { remoteDataSource.fetchMoviesList() },
            saveRemoteData = { localDataSource.saveMovieList(it.data) }
        )
}