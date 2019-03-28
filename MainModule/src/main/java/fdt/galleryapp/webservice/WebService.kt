package fdt.galleryapp.webservice

import android.content.Context
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import fdt.galleryapp.BuildConfig
import fdt.galleryapp.constants.API_KEY
import fdt.galleryapp.constants.BASE_URL
import fdt.galleryapp.utils.connectivityManager
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.internal.platform.Platform
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

open class WebService @Inject constructor(private val context: Context) {

    private val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .client(createOkHttpClient())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build()

    fun <T> request(api: Class<T>, repo: (T) -> Single<Response<ResponseBody>>): Single<String> {
        val request = repo(retrofit.create(api))
        return Single.zip(checkInternetConnection(), request.flatMap { mapResponse(it) },
            BiFunction { _, response -> response })
    }

    private fun createOkHttpClient(): OkHttpClient {
        try {
            val builder = OkHttpClientBuilder.getOkHttpClientBuilder()
            builder.connectTimeout(60, TimeUnit.SECONDS)
            builder.readTimeout(60, TimeUnit.SECONDS)
            builder.addInterceptor(createHeaders())
            if (BuildConfig.DEBUG) {
                builder.addInterceptor(createLoggingInterceptor())
            }
            return builder.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun createHeaders(): Interceptor {
        return Interceptor { chain ->
            chain.proceed(
                chain.request().newBuilder()
                    .addHeader("Authorization", "Client-ID $API_KEY")
                    .addHeader("Accept-Version", "v1")
                    .build()
            )
        }
    }

    private fun createLoggingInterceptor(): LoggingInterceptor {
        return LoggingInterceptor.Builder()
            .loggable(true)
            .setLevel(Level.BASIC)
            .log(Platform.WARN)
            .request("REQUEST")
            .response("RESPONSE")
            .build()
    }

    private fun checkInternetConnection(): Single<Boolean> {
        return Single.create {
            val activeNetwork = context.connectivityManager?.activeNetworkInfo
            if (activeNetwork != null && activeNetwork.isConnected) {
                it.onSuccess(true)
            } else {
                it.onError(Exception("No internet connection"))
            }
        }
    }

    private fun mapResponse(responseBody: Response<ResponseBody>): Single<String> {
        return Single.create {
            val body = responseBody.body()
            if (responseBody.isSuccessful && body != null) {
                val response = body.string()
                it.onSuccess(response)
            } else {
                it.onError(Exception("Connection error... code:${responseBody.code()}"))
            }
        }
    }
}