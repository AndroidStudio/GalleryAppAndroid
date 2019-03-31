package fdt.galleryapp.repository.user

import com.google.gson.Gson
import fdt.galleryapp.models.PhotoModel
import fdt.galleryapp.webservice.WebService
import io.reactivex.Single
import retrofit2.Retrofit
import javax.inject.Inject

class UserPhotoRepository @Inject constructor(private var webService: WebService, retrofit: Retrofit) {

    private val api = retrofit.create(UserPhotoApi::class.java)


    /**
     * Get user photo list from server
     * */
    fun getUserPhotoList(username: String): Single<String> {
        return webService.request(api.getUserPhoto(username, 30, "popular"))
    }

    fun mapUserPhotoList(response: String): MutableList<PhotoModel> {
        return Gson().fromJson(response, Array<PhotoModel>::class.java).toMutableList()
    }
}