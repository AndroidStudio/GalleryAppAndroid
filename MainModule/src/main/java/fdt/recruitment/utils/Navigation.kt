package fdt.recruitment.utils

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import fdt.recruitment.constants.*
import fdt.recruitment.launchActivity
import fdt.recruitment.ui.activities.FullPhotoActivity
import fdt.recruitment.ui.activities.PhotoDetailsActivity
import fdt.recruitment.ui.activities.PhotoListActivity
import fdt.recruitment.ui.activities.UserPhotoListActivity


class Navigation {

    companion object {
        fun startPhotoDetailsActivity(
            view: View, context: AppCompatActivity,
            photoId: String,
            photoUrlFull: String?,
            photoUrl: String?,
            photoWidth: Int?,
            photoHeight: Int
        ) {
            context.launchActivity(
                PhotoDetailsActivity::class.java,
                {
                    putExtra(PHOTO_ID, photoId)
                    putExtra(PHOTO_URL_FULL, photoUrlFull)
                    putExtra(PHOTO_URL, photoUrl)
                    putExtra(PHOTO_WIDTH, photoWidth)
                    putExtra(PHOTO_HEIGHT, photoHeight)
                },

                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context, view, "photo_image"
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
                }, null
            )
        }

        fun startPhotoFullActivity(view: View, context: AppCompatActivity, photoUrlFull: String?) {
            context.launchActivity(
                FullPhotoActivity::class.java,
                { putExtra(PHOTO_URL_FULL, photoUrlFull) },
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context, view, "photo_image"
                ).toBundle()
            )
        }

        fun startPhotoListActivity(context: AppCompatActivity) {
            context.launchActivity(PhotoListActivity::class.java, {}, null)
        }
    }
}