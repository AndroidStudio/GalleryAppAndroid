package fdt.galleryapp.webservice

import android.content.Context
import fdt.galleryapp.repository.remote.RemoteRepository
import fdt.galleryapp.utils.connectivityManager
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

open class WebService @Inject constructor(private val context: Context, private val api: RemoteRepository.API) {

    open fun request(repo: (RemoteRepository.API) -> Single<Response<ResponseBody>>?): Single<String> {
        val request = repo(api)
        return Single.zip(checkInternetConnection(), request?.flatMap { mapResponse(it) },
            BiFunction { _, response -> response })
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