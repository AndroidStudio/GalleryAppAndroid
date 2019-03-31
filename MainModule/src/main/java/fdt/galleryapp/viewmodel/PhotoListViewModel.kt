package fdt.galleryapp.viewmodel

import android.app.Application
import fdt.galleryapp.models.PhotoListItemModel
import fdt.galleryapp.repository.photo.PhotoRepository
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
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
            Flowable.create(
                FlowableOnSubscribe<List<PhotoListItemModel>>
                { emitter ->
                    //Get photo list from database
                    var photoList: List<PhotoListItemModel>? = null
                    emitter.setDisposable(
                        photoRepository.getPhotoListFromDatabase()
                            .subscribe({
                                if (!it.isNullOrEmpty()) {
                                    emitter.onNext(it)
                                    photoList = it
                                }
                            }, emitter::onError)
                    )

                    //Get photo list from server
                    emitter.setDisposable(photoRepository.getPhotoListRemote()
                        .subscribe(emitter::onNext) {
                            //If error occurred and photo list in database is empty throw exception
                            if (photoList.isNullOrEmpty()) {
                                emitter.onError(it)
                            }
                        }
                    )
                }, BackpressureStrategy.BUFFER
            ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onPublishPhotoList, onErrorLoadingPhotoList)
        )
    }
}
