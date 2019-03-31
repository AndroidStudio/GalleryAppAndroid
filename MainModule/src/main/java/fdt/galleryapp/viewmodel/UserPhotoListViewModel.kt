package fdt.galleryapp.viewmodel

import android.app.Application
import fdt.galleryapp.models.PhotoModel
import fdt.galleryapp.repository.user.UserPhotoRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserPhotoListViewModel @Inject constructor(
    private var userPhotoRepository: UserPhotoRepository,
    application: Application
) : BaseViewModel(application) {

    /**
     * Download user photo list from server
     * */
    fun getUserPhotoList(
        username: String, userPhotoList: (MutableList<PhotoModel>) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        addDisposable(userPhotoRepository.getUserPhotoList(username)
            .map { userPhotoRepository.mapUserPhotoList(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(userPhotoList, onError))
    }
}