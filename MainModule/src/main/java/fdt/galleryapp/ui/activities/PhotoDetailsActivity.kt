package fdt.galleryapp.ui.activities

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import dagger.android.AndroidInjection
import fdt.galleryapp.R
import fdt.galleryapp.constants.PHOTO_DETAILS_PARAMETERS
import fdt.galleryapp.models.decorators.PhotoDetailsModel
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
        val photoParameters = getExtra<PhotoDetailsParameters>(PHOTO_DETAILS_PARAMETERS)

        val photoId = photoParameters?.photoId()
        val photoUrlFull = photoParameters?.photoUrlFull()
        val photoUrl = photoParameters?.photoUrl()
        val photoHeight = photoParameters?.photoHeight()
        val photoWidth = photoParameters?.photoWidth()

        if (photoId == null
                || photoUrl == null
                || photoUrlFull == null
                || photoHeight == null
                || photoWidth == null
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
        photoDetailsViewModel.getPhotoDetails(photoId, ::onPublishPhotoDetails, ::onErrorLoadingPhotoDetails)
    }

    private fun onPublishPhotoDetails(photoDetailsModel: PhotoDetailsModel) {
        progressBar.visibility = View.GONE
        descriptionTextView.text = photoDetailsModel.getDescription(this)
        nameTextView.text = photoDetailsModel.getName(this)
        photoDetailsTextView.text = photoDetailsModel.getPhotoDetails(this)
    }

    private fun onErrorLoadingPhotoDetails(throwable: Throwable) {
        progressBar.visibility = View.GONE
        showErrorMessage(throwable.message)
        throwable.printStackTrace()
    }

    override fun getToolbarTitle(): String {
        return getString(R.string.photoDetails).toUpperCase()
    }
}