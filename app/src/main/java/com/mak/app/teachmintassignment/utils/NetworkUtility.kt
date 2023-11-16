package com.mak.app.teachmintassignment.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import com.mak.app.teachmintassignment.BuildConfig
import com.mak.app.teachmintassignment.data.ApiResult
import com.mak.app.teachmintassignment.data.model.ApiError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NetworkUtility @Inject constructor(
    private val applicationContext: Context,
) {
    fun isOnline(): Boolean {
        var result = false
        val connectionManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            connectionManager.run {
                connectionManager.getNetworkCapabilities(connectionManager.activeNetwork)?.run {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
                        else -> false
                    }
                }
            }
        } else {
            connectionManager.run {
                val activeNetwork: NetworkInfo? = connectionManager.activeNetworkInfo
                result = activeNetwork?.isConnected == true
            }
        }
        return result
    }

    fun getOkHttpClient(): OkHttpClient {

        val builder = OkHttpClient.Builder()

//        builder.addInterceptor{ chain ->
//            try {
//                chain.proceed(getNewBuilder(chain))
//            } catch (ex: Exception) {
//                throw ex
//            }
//        }

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        builder.addInterceptor(loggingInterceptor)

        return builder.connectTimeout(60,  TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    private fun getNewBuilder(chain: Interceptor.Chain): Request {
        val newBuilder = chain.request().newBuilder()

        newBuilder.header(
            "Authorization",
            "Bearer " + BuildConfig.TOKEN
        )

//        newBuilder.header("Accept", "application/vnd.github+json")
//        newBuilder.header("X-GitHub-Api-Version", "2022-11-28")

        return newBuilder.build()
    }

    fun <T: Any> safeApiCall(
        apiToBeCalled: suspend () -> T
    ): Flow<ApiResult<T>> = flow {
        try {
            if (!isOnline()){
                emit(
                    ApiResult.Error(
                    ApiError(
                        NetworkError.NO_INTERNET.code,
                        "No Internet, Please check your internet connection"
                    )
                ))
            } else {
                emit(ApiResult.Loading())
                val response = apiToBeCalled.invoke()
                emit(ApiResult.Success(response))
            }
        } catch (e: Exception) {
            emit(ApiResult.Error(handleNetworkException(e)))
        }
    }

    private fun handleNetworkException(e: Exception): ApiError {
        if (e is HttpException) {
            return ApiError(e.code(), e.message())
        } else {
            return ApiError(
                NetworkError.UNKNOWN_ERROR.code,
                "An unexpected error occurred, Try again later"
            )
        }
    }

    enum class NetworkError(val code: Int) {
        NO_INTERNET(1000),
        UNKNOWN_ERROR(3000),
        VALIDATION_ERROR(4000)
    }

    enum class NetworkApiStatus(val code: Int) {
        SUCCESS(200)
    }

}