package fdt.galleryapp.viewmodel

import android.app.Application
import fdt.galleryapp.FDTApplication
import fdt.galleryapp.models.PhotoModel
import fdt.galleryapp.repository.remote.RemoteRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserPhotoListViewModel(application: Application) : BaseViewModel(application) {

    @Inject
    lateinit var remoteRepository: RemoteRepository

    init {
        FDTApplication.appComponent.inject(this)
    }

    /**
     * Download user photo list from server
     * */
    fun getUserPhotoList(
        username: String, userPhotoList: (MutableList<PhotoModel>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        addDisposable(remoteRepository.getUserPhotoList(username)
            .map { remoteRepository.mapUserPhotoList(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(userPhotoList, onError))
    }
}