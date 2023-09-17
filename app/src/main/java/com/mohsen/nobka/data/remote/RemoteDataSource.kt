package com.mohsen.nobka.data.remote

import com.mohsen.nobka.core.data.remote.ApiResponse
import com.mohsen.nobka.core.data.remote.BaseRemoteDataSource
import com.mohsen.nobka.domain.model.NobkaDataModel

class RemoteDataSource(private val nobkaServices: NobkaServices) : BaseRemoteDataSource() {

    suspend fun fetchMoviesList(): ApiResponse<NobkaDataModel> {
        return makeApiCall { nobkaServices.getMovies() }
    }
}