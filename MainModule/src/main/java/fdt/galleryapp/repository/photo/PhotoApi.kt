package fdt.galleryapp.repository.photo

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PhotoApi {

    @GET("/photos")
    fun getPhotoList(
        @Query("per_page") perPage: Int,
        @Query("order_by") orderBy: String
    ): Single<Response<ResponseBody>>

    @GET("/photos/{photoId}")
    fun getPhotoDetails(@Path("photoId") id: String): Single<Response<ResponseBody>>

}