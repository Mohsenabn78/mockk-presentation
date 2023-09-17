package com.mohsen.nobka.core.data.remote

import retrofit2.Response

sealed class ApiResponse<T> {
    companion object {

        fun <T> create(error: Throwable): ApiErrorResponse<T> {
            return ApiErrorResponse(
                error.message ?: "Unknown error",
                0
            )
        }


        fun <T> create(response: Response<T>): ApiResponse<T> {
            return if (response.isSuccessful) {
                val body = response.body()
                if (body == null || response.code() == 204) {
                    ApiEmptyResponse()
                } else {
                    ApiSuccessResponse(
                        body
                    )
                }
            } else {
                val msg = response.errorBody()?.string()
                val errorMsg = if (msg.isNullOrEmpty()) {
                    response.message()
                } else {
                    msg
                }
                ApiErrorResponse(
                    errorMsg ?: "Unknown error",
                    response.code()
                )
            }
        }
    }
}

/**
 * separate class for HTTP 204 responses so that we can make ApiSuccessResponse's body non-null.
 */
class ApiEmptyResponse<T> : ApiResponse<T>()

data class ApiSuccessResponse<T>(
    val body: T? = null
) : ApiResponse<T>()

data class ApiErrorResponse<T>(val errorMessage: String? = null, val statusCode: Int? = null) :
    ApiResponse<T>()

data class NoConnectionApiError<T>(val message: String? = null) : ApiResponse<T>()