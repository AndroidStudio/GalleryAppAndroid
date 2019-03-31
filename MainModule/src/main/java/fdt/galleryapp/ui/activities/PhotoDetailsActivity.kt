package fdt.galleryapp.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import dagger.android.AndroidInjection
import fdt.galleryapp.R
import fdt.galleryapp.constants.PHOTO_DETAILS_PARAMETERS
import fdt.galleryapp.models.PhotoModel
import fdt.galleryapp.parametres.PhotoDetailsParameters
import fdt.galleryapp.utils.Navigation
import fdt.galleryapp.utils.device.Device
import fdt.galleryapp.utils.getExtra
import fdt.galleryapp.viewmodel.PhotoDetailsViewModel
import kotlinx.android.synthetic.main.photo_details_activity.*
import javax.inject.Inject

class PhotoDetailsActivity : BaseActivity() {

    @Inject
    lateinit var photoDetailsViewModel: PhotoDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.photo_details_activity)
        setToolbarColor(R.color.toolbarColor)
        getPhotoDetails()
    }

    private fun getPhotoDetails() {
        val photoDetailsParameters =
            getExtra<PhotoDetailsParameters>(PHOTO_DETAILS_PARAMETERS)

        val photoId = photoDetailsParameters?.photoId()
        val photoUrlFull = photoDetailsParameters?.photoUrlFull()
        val photoUrl = photoDetailsParameters?.photoUrl()
        val photoHeight = photoDetailsParameters?.photoHeight() ?: 0
        val photoWidth = photoDetailsParameters?.photoWidth() ?: 0

        if (photoId == null
            || photoUrl == null
            || photoUrlFull == null
            || photoHeight == 0
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
        descriptionTextView.text =
            String.format("%s: %s", getString(R.string.description), photoModel.description ?: "")
        nameTextView.text = String.format("%s: %s", getString(R.string.from), photoModel.userModel.name ?: "")
        photoDetailsTextView.text = String.format(
            "%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\nISO: %s",
            getString(R.string.make),
            photoModel.exifModel.make ?: "",
            getString(R.string.model),
            photoModel.exifModel.model ?: "",
            getString(R.string.exposure),
            photoModel.exifModel.exposure_time ?: "",
            getString(R.string.aperture),
            photoModel.exifModel.aperture,
            getString(R.string.focel_length),
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