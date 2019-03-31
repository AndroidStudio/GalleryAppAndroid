package fdt.galleryapp.ui.holders

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginLeft
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import fdt.galleryapp.models.decorators.PhotoListItemModel
import fdt.galleryapp.parametres.PhotoDetailsParameters
import fdt.galleryapp.utils.Navigation
import fdt.galleryapp.utils.device.Device
import kotlinx.android.synthetic.main.photo_list_item.view.*

class PhotoViewHolder(
    itemView: View,
    private val context: AppCompatActivity
) :
    BaseHolder<PhotoListItemModel>(itemView) {
    private var width = Device.getScreenWidth(context) - itemView.cardView.marginLeft * 2

    override fun onBind(model: PhotoListItemModel) {
        val height = width * model.photoHeight / model.photoWidth
        itemView.photoImageView.layoutParams.height = height

        itemView.descriptionTextView.text = model.description
        itemView.fromButton.text = model.fromUser(context)
        itemView.locationTextView.text = model.location
        itemView.titleTextView.text = model.title

        if (model.description.isNullOrEmpty()) {
            itemView.descriptionTextView.visibility = View.GONE
        }

        Glide.with(context)
            .load(model.avatar)
            .apply(RequestOptions().circleCrop())
            .into(itemView.avatarImageView)

        Glide.with(context)
            .load(model.photoSmall)
            .apply(RequestOptions().centerCrop())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(itemView.photoImageView)

        itemView.detailsButton.setOnClickListener {
            startPhotoDetailsActivity(model)
        }

        itemView.fromButton.setOnClickListener {
            startUserPhotoListActivity(model)
        }
    }

    private fun startUserPhotoListActivity(model: PhotoListItemModel) {
        Navigation.startUserPhotoListActivity(context, model.userName, model.lastName, model.firstName)
    }

    private fun startPhotoDetailsActivity(model: PhotoListItemModel) {
        Navigation.startPhotoDetailsActivity(
            itemView.photoImageView,
            context,
            PhotoDetailsParameters.create(
                model.photoId,
                model.photoFull,
                model.photoSmall,
                model.photoWidth,
                model.photoHeight
            )
        )
    }
}