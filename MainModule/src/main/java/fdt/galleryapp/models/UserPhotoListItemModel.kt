package fdt.galleryapp.models

//UserPhotoListItemModel as decorator
class UserPhotoListItemModel(photoModel: PhotoModel) {
    val photoId: String = photoModel.id
    val height: Int = photoModel.height
    val width: Int = photoModel.width
    val photoFull: String? = photoModel.urlsModel.full
    val photoSmall: String? = photoModel.urlsModel.small
}