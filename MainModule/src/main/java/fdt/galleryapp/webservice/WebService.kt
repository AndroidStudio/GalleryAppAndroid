package fdt.galleryapp.webservice

import android.content.Context
import fdt.galleryapp.R
import fdt.galleryapp.utils.connectivityManager
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

open class WebService @Inject constructor(private val context: Context) {

    open fun request(request: Single<Response<ResponseBody>>): Single<String> {
        return Single.zip(checkInternetConnection(), request.flatMap { mapResponse(it) },
            BiFunction { _, response -> response })
    }

    open fun checkInternetConnection(): Single<Boolean> {
        return Single.create {
            val activeNetwork = context.connectivityManager?.activeNetworkInfo
            if (activeNetwork != null && activeNetwork.isConnected) {
                it.onSuccess(true)
            } else {
                it.onError(Exception(context.getString(R.string.noInternetConnection)))
            }
        }
    }

    open fun mapResponse(responseBody: Response<ResponseBody>): Single<String> {
        return Single.create {
            val body = responseBody.body()
            if (responseBody.isSuccessful && body != null) {
                val response = body.string()
                it.onSuccess(response)
            } else {
                it.onError(Exception(context.getString(R.string.connectionError) + responseBody.code()))
            }
        }
    }
}