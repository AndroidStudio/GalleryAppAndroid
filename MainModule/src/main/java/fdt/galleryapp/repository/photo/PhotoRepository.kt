package fdt.galleryapp.repository.photo

import com.google.gson.Gson
import fdt.galleryapp.entities.PhotoEntity
import fdt.galleryapp.models.PhotoListItemModel
import fdt.galleryapp.models.PhotoModel
import fdt.galleryapp.modules.DatabaseModule
import fdt.galleryapp.webservice.WebService
import io.reactivex.FlowableOnSubscribe
import io.reactivex.Single
import retrofit2.Retrofit
import javax.inject.Inject

open class PhotoRepository @Inject constructor(
    private var appDatabase: DatabaseModule.AppDatabase,
    private var webService: WebService,
    retrofit: Retrofit
) {

    private val api = retrofit.create(PhotoApi::class.java)

    fun getPhotoList(): FlowableOnSubscribe<List<PhotoEntity>> {
        return FlowableOnSubscribe { emitter ->
            //Get photo list from database
            var photoList: List<PhotoEntity>? = null
            emitter.setDisposable(
                getPhotoListFromDatabase()
                    .subscribe({
                        if (!it.isNullOrEmpty()) {
                            emitter.onNext(it)
                            photoList = it
                        }
                    }, emitter::onError)
            )
            //Get photo list from server
            emitter.setDisposable(getPhotoListRemote()
                .subscribe(emitter::onNext) {
                    //If error occurred and photo list in database is empty throw exception
                    if (photoList.isNullOrEmpty()) {
                        emitter.onError(it)
                    }
                }
            )
        }
    }

    /**
     * Get photo list from server
     * */
    private fun getPhotoListRemote(): Single<List<PhotoEntity>> {
        return webService.request(api.getPhotoList(30, "popular"))
            .map { Gson().fromJson(it, Array<PhotoModel>::class.java).toList() }
            .map { list ->
                list.map { PhotoEntity(it) }.also { appDatabase.photoQuery().insertPhotoList(it) }
            }
        //TODO simplify to many mapping...
    }

    /**
     * Get photo details from server
     * */
    fun getPhotoDetailsRemote(photoId: String): Single<PhotoModel> {
        return webService.request(api.getPhotoDetails(photoId))
            .map { Gson().fromJson(it, PhotoModel::class.java) }
    }

    /**
     * Get photo list from database
     */
    private fun getPhotoListFromDatabase(): Single<List<PhotoEntity>> {
        return appDatabase.photoQuery().getPhotoList()
    }

    fun mapPhotoListItem(list: List<PhotoEntity>): List<PhotoListItemModel> {
        return list.map { photoEntity -> PhotoListItemModel(photoEntity) }
    }
}