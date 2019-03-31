package fdt.galleryapp.viewmodel

import android.app.Application
import fdt.galleryapp.models.PhotoListItemModel
import fdt.galleryapp.repository.photo.PhotoRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PhotoListViewModel @Inject constructor(
    private val photoRepository: PhotoRepository,
    application: Application
) : BaseViewModel(application) {

    fun getPhotoList(
        onPublishPhotoList: (List<PhotoListItemModel>) -> Unit,
        onErrorLoadingPhotoList: (Throwable) -> Unit
    ) {
        addDisposable(
            Flowable.create(photoRepository.getPhotoList(), BackpressureStrategy.BUFFER)
                .map { photoRepository.mapPhotoListItem(it) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onPublishPhotoList, onErrorLoadingPhotoList)
        )
    }
}
