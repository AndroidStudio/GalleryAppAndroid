package fdt.galleryapp.repository.user

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UserPhotoApi {

    @GET("/users/{username}/photos")
    fun getUserPhoto(
        @Path("username") username: String,
        @Query("per_page") perPage: Int,
        @Query("order_by") orderBy: String
    ): Single<Response<ResponseBody>>

}