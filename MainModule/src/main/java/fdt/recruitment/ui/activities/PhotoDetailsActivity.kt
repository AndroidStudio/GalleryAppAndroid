package fdt.recruitment.ui.activities

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import fdt.recruitment.R
import fdt.recruitment.constants.*
import fdt.recruitment.models.PhotoModel
import fdt.recruitment.utils.Navigation
import fdt.recruitment.utils.device.Device
import fdt.recruitment.viewmodel.PhotoDetailsViewModel
import kotlinx.android.synthetic.main.photo_details_activity.*
import kotlinx.android.synthetic.main.toolbar.*

class PhotoDetailsActivity : BaseActivity() {

    private val photoDetailsViewModel by lazy {
        ViewModelProviders.of(this).get(PhotoDetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_details_activity)
        getPhotoDetails()
        updatePhotoImage()
        toolbar.setBackgroundColor(Color.parseColor("#50000000"))
    }

    private fun getPhotoDetails() {
        val photoId = intent?.extras?.getString(PHOTO_ID)
        if (photoId == null) {
            showMessage(getString(R.string.error_loading_photo_details))
            return
        }
        photoDetailsViewModel.getPhotoDetails(
            photoId, ::updatePhotoDetails, ::onError
        )
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

    private fun updatePhotoImage() {
        val photoUrlFull = intent?.extras?.getString(PHOTO_URL_FULL) ?: ""
        val photoUrl = intent?.extras?.getString(PHOTO_URL) ?: ""

        val srcWidth = intent?.extras?.getInt(PHOTO_WIDTH) ?: 0
        val srcHeight = intent?.extras?.getInt(PHOTO_HEIGHT) ?: 0

        val destHeight = Device.getScreenWidth(this) * srcHeight / srcWidth

        photoImageView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, destHeight)
        Glide.with(this).load(photoUrl).listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable, model: Any, target: Target<Drawable>,
                dataSource: DataSource, isFirstResource: Boolean
            ): Boolean {
                return false
            }
        }).into(photoImageView)
        Glide.with(this).load(photoUrlFull).into(photoImageView)

        fullScreenButton.setOnClickListener {
            Navigation.startPhotoFullActivity(photoImageView, this, photoUrlFull)
        }
    }

    /**
     * Error loading photo details
     * */
    private fun onError(throwable: Throwable) {
        progressBar.visibility = View.GONE
        showMessage(throwable.message)
        throwable.printStackTrace()
    }

    override fun getToolbarTitle(): String {
        return getString(R.string.photoDetails).toUpperCase()
    }

    override fun showBackButton(show: Boolean) {
        super.showBackButton(true)
    }
}