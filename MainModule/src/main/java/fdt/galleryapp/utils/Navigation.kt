package fdt.galleryapp.utils

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import fdt.galleryapp.constants.*
import fdt.galleryapp.parametres.PhotoDetailsParameters
import fdt.galleryapp.ui.activities.FullPhotoActivity
import fdt.galleryapp.ui.activities.PhotoDetailsActivity
import fdt.galleryapp.ui.activities.PhotoListActivity
import fdt.galleryapp.ui.activities.UserPhotoListActivity

object Navigation {

    fun startPhotoDetailsActivity(
        sharedElement: View, context: AppCompatActivity,
        photoDetailsParameters: PhotoDetailsParameters
    ) {
        context.launchActivity(
            PhotoDetailsActivity::class.java, { putExtra(PHOTO_DETAILS_PARAMETERS, photoDetailsParameters) },
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                context, sharedElement, "photo_image"
            ).toBundle()
        )
    }

    fun startUserPhotoListActivity(
        context: AppCompatActivity,
        username: String,
        userLastName: String?,
        userFirstName: String?
    ) {
        context.launchActivity(
            UserPhotoListActivity::class.java,
            {
                putExtra(USER_NAME, username)
                putExtra(USER_LAST_NAME, userLastName)
                putExtra(USER_FIRST_NAME, userFirstName)
            }
        )
    }

    fun startPhotoFullActivity(sharedElement: View, context: AppCompatActivity, photoUrlFull: String?) {
        context.launchActivity(
            FullPhotoActivity::class.java,
            { putExtra(PHOTO_URL_FULL, photoUrlFull) },
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                context, sharedElement, "photo_image"
            ).toBundle()
        )
    }

    fun startPhotoListActivity(context: AppCompatActivity) {
        context.launchActivity(PhotoListActivity::class.java, {})
    }
}
