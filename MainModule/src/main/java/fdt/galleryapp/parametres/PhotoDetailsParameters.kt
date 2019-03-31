package fdt.galleryapp.parametres

import android.os.Parcelable
import com.google.auto.value.AutoValue

@AutoValue
abstract class PhotoDetailsParameters : Parcelable {

    abstract fun photoId(): String?
    abstract fun photoUrlFull(): String?
    abstract fun photoUrl(): String?
    abstract fun photoWidth(): Int?
    abstract fun photoHeight(): Int?

    companion object {
        fun create(
            photoId: String?,
            photoUrlFull: String?,
            photoUrl: String?,
            photoWidth: Int?,
            photoHeight: Int?
        ): PhotoDetailsParameters {
            return AutoValue_PhotoDetailsParameters(
                photoId,
                photoUrlFull,
                photoUrl,
                photoWidth,
                photoHeight
            )
        }
    }
}