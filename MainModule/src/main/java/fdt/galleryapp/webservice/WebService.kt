package fdt.galleryapp.webservice

import android.content.Context
import fdt.galleryapp.R
import fdt.galleryapp.utils.connectivityManager
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

open class WebService @Inject constructor(private val context: Context) {

    open fun request(request: Single<Response<ResponseBody>>): Single<String> {
        return checkInternetConnection().flatMap { request.flatMap { mapResponse(it) } }
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

    open fun mapResponse(responseBody: Response<ResponseBody>?): Single<String> {
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