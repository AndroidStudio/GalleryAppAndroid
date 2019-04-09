package fdt.galleryapp.repository.user

import fdt.galleryapp.models.PhotoModel
import fdt.galleryapp.webservice.WebService
import io.reactivex.Single
import retrofit2.Retrofit
import javax.inject.Inject

class UserPhotoRepository @Inject constructor(private var webService: WebService, retrofit: Retrofit) {

    private val api = retrofit.create(UserPhotoApi::class.java)

    fun getUserPhotoListRemote(username: String): Single<List<PhotoModel>> {
        return webService.request(
            api.getUserPhoto(username, 30, "popular")
        )
    }
}