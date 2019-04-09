package fdt.galleryapp.webservice

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fdt.galleryapp.R
import fdt.galleryapp.utils.connectivityManager
import io.reactivex.Completable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

open class WebService @Inject constructor(private val context: Context) {

    inline fun <reified T> request(request: Single<Response<ResponseBody>>): Single<T> {
        val responseType = object : TypeToken<T>() {}.type
        return verifyInternetConnection()
            .andThen(request.flatMap { interceptResponseBody(it) })
            .flatMap { Single.just(Gson().fromJson<T>(it, responseType)) }
    }

    open fun verifyInternetConnection(): Completable {
        return Completable.create {
            val activeNetwork = context.connectivityManager?.activeNetworkInfo
            if (activeNetwork == null || !activeNetwork.isConnected) {
                it.onError(Exception(context.getString(R.string.noInternetConnection)))
            }
            it.onComplete()
        }
    }

    open fun interceptResponseBody(responseBody: Response<ResponseBody>?): Single<String> {
        return Single.create {
            val body = responseBody?.body()
            if (body != null && responseBody.isSuccessful) {
                val response = body.string()
                it.onSuccess(response)
            } else {
                it.onError(Exception(context.getString(R.string.connectionError) + responseBody?.code()))
            }
        }
    }
}