package fdt.galleryapp.ui.activities

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import fdt.galleryapp.R
import fdt.galleryapp.constants.PHOTO_URL_FULL
import kotlinx.android.synthetic.main.photo_full_activity.*
import kotlinx.android.synthetic.main.toolbar.*

class FullPhotoActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_full_activity)
        postponeEnterTransition()
        val photoUrl = intent?.extras?.getString(PHOTO_URL_FULL)
        photoUrl?.let {
            Glide.with(this).load(photoUrl)
                .listener(loadPhotoListener).into(photoImageView)
        }

        toolbar.setBackgroundColor(Color.parseColor("#50000000"))
    }

    private val loadPhotoListener = object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any,
            target: Target<Drawable>,
            isFirstResource: Boolean
        ): Boolean {
            startPostponedEnterTransition()
            return false
        }

        override fun onResourceReady(
            resource: Drawable,
            model: Any,
            target: Target<Drawable>,
            dataSource: DataSource,
            isFirstResource: Boolean
        ): Boolean {
            startPostponedEnterTransition()
            return false
        }
    }

    override fun getToolbarTitle(): String {
        return getString(R.string.full_screen_photo).toUpperCase()
    }
}