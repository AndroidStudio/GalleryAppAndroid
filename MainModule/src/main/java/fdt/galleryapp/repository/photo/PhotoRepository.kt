package fdt.galleryapp.repository.photo

import com.google.gson.Gson
import fdt.galleryapp.entities.PhotoEntity
import fdt.galleryapp.models.PhotoModel
import fdt.galleryapp.modules.DatabaseModule
import fdt.galleryapp.webservice.WebService
import io.reactivex.Single
import retrofit2.Retrofit
import javax.inject.Inject

open class PhotoRepository @Inject constructor(
    private var appDatabase: DatabaseModule.AppDatabase,
    private var webService: WebService,
    retrofit: Retrofit
) {

    private val api = retrofit.create(PhotoApi::class.java)

    /**
     * Get photo list from server
     * */
    open fun getPhotoListRemote(): Single<String> {
        return webService.request(api.getPhotoList(30, "popular"))
    }

    fun mapPhotoList(response: String): MutableList<PhotoModel> {
        return Gson().fromJson(response, Array<PhotoModel>::class.java).toMutableList()
    }

    /**
     * Get photo details from server
     * */
    fun getPhotoDetailsRemote(photoId: String): Single<String> {
        return webService.request(api.getPhotoDetails(photoId))
    }

    fun mapPhotoDetails(response: String): PhotoModel {
        return Gson().fromJson(response, PhotoModel::class.java)
    }

    /**
     * Get photo list from database
     */
    open fun getPhotoListFromDatabase(): List<PhotoEntity> {
        return appDatabase.photoQuery().getPhotoList()
    }

    /**
     * Save photo list to database
     */
    fun savePhotosToDatabase(photoList: List<PhotoModel>): List<PhotoEntity> {
        return photoList.map { PhotoEntity(it) }.also {
            appDatabase.photoQuery().insertPhotoList(it)
        }
    }
}