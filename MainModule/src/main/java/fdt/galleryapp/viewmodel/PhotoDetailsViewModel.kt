package fdt.galleryapp.viewmodel

import android.app.Application
import fdt.galleryapp.models.PhotoModel
import fdt.galleryapp.repository.photo.PhotoRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PhotoDetailsViewModel @Inject constructor(
    private val photoRepository: PhotoRepository,
    application: Application
) : BaseViewModel(application) {

    fun getPhotoDetails(
        photoId: String,
        onPublishPhotoDetails: (PhotoModel) -> Unit,
        onErrorLoadingPhotoDetails: (Throwable) -> Unit
    ) {
        addDisposable(
            photoRepository.getPhotoDetailsRemote(photoId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onPublishPhotoDetails, onErrorLoadingPhotoDetails)
        )
    }
}