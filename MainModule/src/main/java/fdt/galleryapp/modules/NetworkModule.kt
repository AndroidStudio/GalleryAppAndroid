package fdt.galleryapp.modules

import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import dagger.Module
import dagger.Provides
import fdt.galleryapp.BuildConfig
import fdt.galleryapp.constants.API_KEY
import fdt.galleryapp.constants.BASE_URL
import fdt.galleryapp.webservice.HttpClientBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL)
            .client(createOkHttpClient())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun createOkHttpClient(): OkHttpClient {
        try {
            val builder = HttpClientBuilder.getOkHttpClientBuilder()
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
}