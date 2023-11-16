package com.mak.app.teachmintassignment.data

import com.mak.app.teachmintassignment.data.model.ApiError

sealed class ApiResult<T: Any>() {
    class Loading<T: Any>: ApiResult<T>()
    data class Success<T: Any>(val data: T): ApiResult<T>()
    data class Error<T: Any>(val error: ApiError): ApiResult<T>()
}