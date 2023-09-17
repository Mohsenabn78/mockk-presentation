package com.mohsen.nobka.core.data.remote.error

sealed class Failure {

    object ConnectionError : Failure()

    class ApiResourceBoundError<DB>(val message: String?,val dataBase:DB) : Failure()

    class FlowError(val message: String?): Failure()
}