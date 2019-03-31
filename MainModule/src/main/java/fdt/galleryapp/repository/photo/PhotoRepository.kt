package fdt.galleryapp.repository.photo

import com.google.gson.Gson
import fdt.galleryapp.entities.PhotoEntity
import fdt.galleryapp.models.PhotoListItemModel
import fdt.galleryapp.models.PhotoModel
import fdt.galleryapp.modules.DatabaseModule
import fdt.galleryapp.webservice.WebService
import io.reactivex.Single
import retrofit2.Retrofit
import java.lang.Exception
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
    open fun getPhotoListRemote(): Single<List<PhotoListItemModel>> {
        return webService.request(api.getPhotoList(30, "popular"))
            .map { Gson().fromJson(it, Array<PhotoModel>::class.java).toList() }
            .map { list ->
                list.map { PhotoEntity(it) }.also { appDatabase.photoQuery().insertPhotoList(it) }
            }
            .map(::mapPhotoListItem)
        //TODO simplify to many mapping...
    }

    /**
     * Get photo details from server
     * */
    open fun getPhotoDetailsRemote(photoId: String): Single<PhotoModel> {
        return webService.request(api.getPhotoDetails(photoId))
            .map { Gson().fromJson(it, PhotoModel::class.java) }
    }

    /**
     * Get photo list from database
     */
    open fun getPhotoListFromDatabase(): Single<List<PhotoListItemModel>> {
        return appDatabase.photoQuery().getPhotoList()
            .map(::mapPhotoListItem)
    }

    private fun mapPhotoListItem(list: List<PhotoEntity>): List<PhotoListItemModel> {
        return list.map { photoEntity -> PhotoListItemModel(photoEntity) }
    }
}