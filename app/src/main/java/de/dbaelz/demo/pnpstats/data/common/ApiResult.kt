package de.dbaelz.demo.pnpstats.data.common

sealed class ApiResult<out T> {
    data class Success<out T>(val value: T) : ApiResult<T>()
    data class Error(val throwable: Throwable? = null) : ApiResult<Nothing>()
}