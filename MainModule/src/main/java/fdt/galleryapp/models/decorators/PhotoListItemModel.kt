package fdt.galleryapp.models.decorators

import android.content.Context
import fdt.galleryapp.R
import fdt.galleryapp.entities.PhotoEntity

//PhotoListItemModel as decorator
data class PhotoListItemModel(private val photoEntity: PhotoEntity) {

    val title: String = String.format("%s %s", photoEntity.first_name ?: "", photoEntity.last_name ?: "")

    fun fromUser(context: Context): String {
        return String.format("%s %s", context.getString(R.string.from), photoEntity.last_name ?: "")
    }

    val description: String? = photoEntity.description ?: ""
    val photoSmall: String? = photoEntity.photo_small ?: ""
    val firstName: String? = photoEntity.first_name ?: ""
    val photoFull: String? = photoEntity.photo_full ?: ""
    val lastName: String? = photoEntity.last_name ?: ""
    val location: String? = photoEntity.location ?: ""
    val avatar: String? = photoEntity.avatar ?: ""

    val photoHeight: Int = photoEntity.photo_height
    val photoWidth: Int = photoEntity.photo_width
    val userName: String = photoEntity.user_name
    val photoId: String = photoEntity.id
}