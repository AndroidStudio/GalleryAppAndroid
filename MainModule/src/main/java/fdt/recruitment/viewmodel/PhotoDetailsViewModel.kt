package fdt.recruitment.viewmodel

import android.app.Application
import fdt.recruitment.FDTApplication
import fdt.recruitment.models.PhotoModel
import fdt.recruitment.repository.remote.RemoteRepository
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