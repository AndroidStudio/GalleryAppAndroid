package fdt.galleryapp.viewmodel

import android.app.Application
import fdt.galleryapp.FDTApplication
import fdt.galleryapp.entities.PhotoEntity
import fdt.galleryapp.models.PhotoModel
import fdt.galleryapp.repository.local.PhotoDatabase
import fdt.galleryapp.repository.remote.RemoteRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PhotoListViewModel(application: Application) : BaseViewModel(application) {

    @Inject
    lateinit var remoteRepository: RemoteRepository

    @Inject
    lateinit var photoDatabase: PhotoDatabase

    init {
        FDTApplication.appComponent.inject(this)
    }

    /**
     * Load photos from database
     * */
    fun getPhotoListLocal(onSuccess: (List<PhotoEntity>) -> Unit) {
        addDisposable(
            photoDatabase.query().getPhotoList()
                .observeOn(AndroidSchedulers.mainThread()).subscribe(onSuccess)
        )
    }

    /**
     * Download photo list from server
     * */
    fun getPhotoListRemote(onError: (Throwable) -> Unit) {
        addDisposable(remoteRepository.getPhotoList()
            .map { remoteRepository.mapPhotoList(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::savePhotosToDatabase, onError))
    }

    private fun savePhotosToDatabase(photoList: MutableList<PhotoModel>) {
        val list: MutableList<PhotoEntity> = arrayListOf()
        photoList.forEach {
            list.add(
                PhotoEntity(
                    it.id,
                    it.userModel.username,
                    it.userModel.first_name,
                    it.userModel.last_name,
                    it.description,
                    it.userModel.location,
                    it.userModel.profile_image.large,
                    it.urlsModel.regular,//small
                    it.urlsModel.thumb,
                    it.urlsModel.raw,
                    it.urlsModel.regular,
                    it.width,
                    it.height
                )
            )
        }

        photoDatabase.query().insertPhotoList(list)
    }
}
