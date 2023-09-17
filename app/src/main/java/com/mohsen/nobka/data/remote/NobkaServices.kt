package com.mohsen.nobka.data.remote

import com.mohsen.nobka.domain.model.NobkaDataModel
import retrofit2.Response
import retrofit2.http.GET

interface NobkaServices {
    @GET("api/v1/movies?page=1")
    suspend fun getMovies(): Response<NobkaDataModel>
}