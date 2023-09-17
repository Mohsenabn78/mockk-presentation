package com.mohsen.nobka.core.data.remote

import com.mohsen.nobka.core.data.remote.error.Failure
import com.mohsen.nobka.core.utils.Either
import kotlinx.coroutines.flow.flow

/**
fetchFromLocal —> It fecth data from local database
shouldFetchFromRemote —> It decide whether network request should be made or use local persistent data if available (Optional)
fetchFromRemote —> It perform network request operation
processRemoteResponse —> It process result of network response before saving model class in database, like saving certain header values(Optional)
saveRemoteData —> It saves result of network request to local persistent database
onFetchFailed —> It handle network request failure scenario (Non HTTP 200..300 response, exceptions etc) (Optional)
 **/

inline fun <DB, REMOTE> networkBoundResource(
    crossinline fetchFromLocal: suspend () -> DB,
    crossinline shouldFetchFromRemote: (DB?) -> Boolean = { true },
    crossinline fetchFromRemote: suspend () -> ApiResponse<REMOTE>,
    crossinline processRemoteResponse: (response: ApiResponse<REMOTE>) -> Unit = { },
    crossinline saveRemoteData: suspend (REMOTE) -> Unit = { },
    crossinline onFetchFailed: (errorBody: String?, statusCode: Int) -> Unit = { _: String?, _: Int -> }
) = flow<Either<Failure, DB>> {

    val localData = fetchFromLocal()

    if (shouldFetchFromRemote(localData)) {

        fetchFromRemote().also { apiResponse ->

            when (apiResponse) {
                is ApiSuccessResponse -> {
                    processRemoteResponse(apiResponse)
                    apiResponse.body?.let { saveRemoteData(it) }
                    emit(Either.Right(fetchFromLocal()))
                }

                is ApiErrorResponse -> {
                    onFetchFailed(apiResponse.errorMessage, apiResponse.statusCode!!)
                    emit(Either.Left(Failure.ApiResourceBoundError(apiResponse.errorMessage, fetchFromLocal())))
                }

                is NoConnectionApiError ->{
                    emit(Either.Right(fetchFromLocal()))
                }

                else -> {}
            }
        }

    } else {
        emit(Either.Right(fetchFromLocal()))
    }

}