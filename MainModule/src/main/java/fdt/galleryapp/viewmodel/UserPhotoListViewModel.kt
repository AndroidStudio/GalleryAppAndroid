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

    fun getUserPhotoListFromServer(
        userName: String,
        onPublishUserPhotoList: (List<PhotoModel>) -> Unit,
        onErrorLoadingUserPhotoList: (Throwable) -> Unit
    ) {
        addDisposable(
            userPhotoRepository.getUserPhotoList(userName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onPublishUserPhotoList, onErrorLoadingUserPhotoList)
        )
    }
}