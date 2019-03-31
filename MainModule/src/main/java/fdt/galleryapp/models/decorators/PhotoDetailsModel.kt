package fdt.galleryapp.models.decorators

import android.content.Context
import fdt.galleryapp.R
import fdt.galleryapp.models.PhotoModel

//PhotoDetailsModel as decorator
class PhotoDetailsModel(private val photoModel: PhotoModel) {
    fun getName(context: Context): String? =
        String.format("%s: %s", context.getString(R.string.from), photoModel.userModel.name ?: "")

    fun getDescription(context: Context): String? =
        String.format("%s: %s", context.getString(R.string.description), photoModel.description ?: "")

    fun getPhotoDetails(context: Context): String? = String.format(
        "%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\nISO: %s",
        context.getString(R.string.make),
        photoModel.exifModel.make ?: "",
        context.getString(R.string.model),
        photoModel.exifModel.model ?: "",
        context.getString(R.string.exposure),
        photoModel.exifModel.exposure_time ?: "",
        context.getString(R.string.aperture),
        photoModel.exifModel.aperture,
        context.getString(R.string.focel_length),
        photoModel.exifModel.focal_length,
        photoModel.exifModel.iso
    )

}