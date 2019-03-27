package fdt.galleryapp.viewmodel

import android.app.Application
import fdt.galleryapp.FDTApplication
import fdt.galleryapp.models.PhotoModel
import fdt.galleryapp.repository.remote.RemoteRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PhotoDetailsViewModel(application: Application) : BaseViewModel(application) {

    @Inject
    lateinit var remoteRepository: RemoteRepository

    init {
        FDTApplication.appComponent.inject(this)
    }

    fun getPhotoDetails(
        photoId: String,
        photoDetails: (PhotoModel) -> Unit,
        error: (Throwable) -> Unit
    ) {
        addDisposable(
            remoteRepository.getPhotoDetails(photoId).subscribeOn(Schedulers.io())
                .map { remoteRepository.mapPhotoDetails(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(photoDetails, error)
        )
    }
}