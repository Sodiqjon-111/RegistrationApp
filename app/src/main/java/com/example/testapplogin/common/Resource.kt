package com.example.testapplogin.common


sealed class Resource<T>(data: T?, message: String? = null) {
    data class Success<T>(val data: T?) : Resource<T>(data)
    data class Error<T>(val data: T? = null, val message: String?,val errorCode: Int? = null) :
        Resource<T>(data, message )
}


