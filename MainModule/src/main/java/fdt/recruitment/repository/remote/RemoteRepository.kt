package fdt.recruitment.repository.remote

import com.google.gson.Gson
import fdt.recruitment.models.PhotoModel
import fdt.recruitment.webservice.WebService
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

class RemoteRepository @Inject constructor(private var webService: WebService) {

    interface API {

        @GET("/photos")
        fun getPhotoList(
            @Query("per_page") perPage: Int,
            @Query("order_by") orderBy: String
        ): Single<Response<ResponseBody>>

        @GET("/photos/{id}")
        fun getPhotoDetails(@Path("id") id: String): Single<Response<ResponseBody>>

        @GET("/users/{username}/photos")
        fun getUserPhoto(
            @Path("username") username: String,
            @Query("per_page") perPage: Int,
            @Query("order_by") orderBy: String
        ): Single<Response<ResponseBody>>
    }

    /**
     * Photo list
     * */
    fun getPhotoList(): Single<String> {
        return webService.request(API::class.java) { it.getPhotoList(30, "popular") }
    }

    fun mapPhotoList(response: String): MutableList<PhotoModel> {
        return Gson().fromJson(response, Array<PhotoModel>::class.java).toMutableList()
    }

    /**
     * Photo details
     * */
    fun getPhotoDetails(photoId: String): Single<String> {
        return webService.request(API::class.java) { it.getPhotoDetails(photoId) }
    }

    fun mapPhotoDetails(response: String): PhotoModel {
        return Gson().fromJson(response, PhotoModel::class.java)
    }

    /**
     * User photo list
     * */
    fun getUserPhotoList(username: String): Single<String> {
        return webService.request(API::class.java) { it.getUserPhoto(username, 30, "popular") }
    }

    fun mapUserPhotoList(response: String): MutableList<PhotoModel> {
        return Gson().fromJson(response, Array<PhotoModel>::class.java).toMutableList()
    }

}