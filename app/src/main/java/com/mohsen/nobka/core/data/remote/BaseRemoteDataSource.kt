package com.mohsen.nobka.core.data.remote


import com.mohsen.nobka.core.data.remote.error.NoConnectionError
import retrofit2.Response

abstract class BaseRemoteDataSource {
    protected suspend fun <T> makeApiCall(call: suspend () -> Response<T>): ApiResponse<T> {
        return try {
            val response = call.invoke()

            if (response.code() == 200) {
                if (response.body() == null) {
                    ApiErrorResponse(null, null)
                } else {
                    ApiSuccessResponse(response.body())
                }

            } else {
                ApiErrorResponse("errorMessage", -1)
            }

        } catch (e: Throwable) {
            if (e is NoConnectionError)
                return NoConnectionApiError()
            return NoConnectionApiError()
        }
    }
}
