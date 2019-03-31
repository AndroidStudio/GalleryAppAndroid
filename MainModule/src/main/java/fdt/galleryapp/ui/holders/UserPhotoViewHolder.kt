package fdt.galleryapp.ui.holders

import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginLeft
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import fdt.galleryapp.models.UserPhotoListItemModel
import fdt.galleryapp.parametres.PhotoDetailsParameters
import fdt.galleryapp.utils.Navigation
import fdt.galleryapp.utils.device.Device
import kotlinx.android.synthetic.main.user_photo_list_item.view.*

class UserPhotoViewHolder(itemView: View, private var context: AppCompatActivity) :
    BaseHolder<UserPhotoListItemModel>(itemView) {
    private var width = Device.getScreenWidth(context) / 2 - itemView.cardView.marginLeft * 3

    override fun onBind(model: UserPhotoListItemModel) {
        val height = width * model.height / model.width
        itemView.photoImageView.layoutParams = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT, height
        )
        itemView.photoImageView.setOnClickListener { startPhotoDetailsActivity(model) }

        Glide.with(context)
            .load(model.photoSmall)
            .apply(RequestOptions())
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(itemView.photoImageView)
    }

    private fun startPhotoDetailsActivity(model: UserPhotoListItemModel) {
        Navigation.startPhotoDetailsActivity(
            itemView.photoImageView,
            context,
            PhotoDetailsParameters.create(
                model.photoId,
                model.photoFull,
                model.photoSmall,
                model.width,
                model.height
            )
        )
    }
}
