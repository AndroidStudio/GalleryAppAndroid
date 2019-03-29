package fdt.galleryapp.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import fdt.galleryapp.R
import fdt.galleryapp.constants.*
import fdt.galleryapp.models.PhotoModel
import fdt.galleryapp.utils.Navigation
import fdt.galleryapp.utils.device.Device
import fdt.galleryapp.utils.getExtra
import fdt.galleryapp.viewmodel.PhotoDetailsViewModel
import kotlinx.android.synthetic.main.photo_details_activity.*

class PhotoDetailsActivity : BaseActivity() {

    private val photoDetailsViewModel by lazy {
        ViewModelProviders.of(this).get(PhotoDetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_details_activity)
        setToolbarColor(R.color.toolbarColor)
        getPhotoDetails()
    }

    private fun getPhotoDetails() {
        val photoId = getExtra<String>(PHOTO_ID)
        val photoUrlFull = getExtra<String>(PHOTO_URL_FULL)
        val photoUrl = getExtra<String>(PHOTO_URL)
        val photoWidth = getExtra<Int>(PHOTO_WIDTH)
        val photoHeight = getExtra<Int>(PHOTO_HEIGHT)

        if (photoId == null
            || photoUrlFull == null
            || photoUrl == null
            || photoHeight == null
            || photoWidth == null
            || photoWidth == 0
            ) {
            showErrorMessage(getString(R.string.unableToDisplayPhotoDetails))
            return
        }

        val destHeight = Device.getScreenWidth(this) * photoHeight / photoWidth

        photoImageView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, destHeight)
        Glide.with(this).load(photoUrl).into(photoImageView)

        fullScreenButton.setOnClickListener {
            Navigation.startPhotoFullActivity(photoImageView, this, photoUrlFull)
        }
        getPhotoDetailsRemote(photoId)
    }

    private fun getPhotoDetailsRemote(photoId: String) {
        photoDetailsViewModel.getPhotoDetails(photoId, ::updatePhotoDetails, ::onError)
    }

    private fun updatePhotoDetails(photoModel: PhotoModel) {
        descriptionTextView.text = String.format("Description: %s", photoModel.description ?: "")
        nameTextView.text = String.format("From: %s", photoModel.userModel.name ?: "")
        photoDetailsTextView.text = String.format(
            "Make: %s\nModel: %s\nExposure time: %s\nAperture: %s\nFocal length: %s\nISO: %s",
            photoModel.exifModel.make ?: "",
            photoModel.exifModel.model ?: "",
            photoModel.exifModel.exposure_time ?: "",
            photoModel.exifModel.aperture,
            photoModel.exifModel.focal_length,
            photoModel.exifModel.iso
        )
        progressBar.visibility = View.GONE
    }

    /**
     * Error loading photo details
     * */
    private fun onError(throwable: Throwable) {
        progressBar.visibility = View.GONE
        showErrorMessage(throwable.message)
        throwable.printStackTrace()
    }

    override fun getToolbarTitle(): String {
        return getString(R.string.photoDetails).toUpperCase()
    }
}