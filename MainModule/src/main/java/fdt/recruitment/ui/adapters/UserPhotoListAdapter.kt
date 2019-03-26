package fdt.recruitment.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginLeft
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import fdt.recruitment.models.PhotoModel
import fdt.recruitment.utils.BaseHolder
import fdt.recruitment.utils.Navigation
import fdt.recruitment.utils.device.Device
import kotlinx.android.synthetic.main.user_photo_list_item.view.*

class UserPhotoListAdapter(private var context: AppCompatActivity) :
    RecyclerView.Adapter<UserPhotoListAdapter.UserPhotoViewHolder>() {

    var photoList: List<PhotoModel> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val layoutInflater: LayoutInflater = LayoutInflater
        .from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPhotoViewHolder {
        return UserPhotoViewHolder(
            layoutInflater.inflate(fdt.recruitment.R.layout.user_photo_list_item, parent, false)
            , context
        )
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun onBindViewHolder(holder: UserPhotoViewHolder, position: Int) {
        holder.onBind(photoList[position])
    }

    class UserPhotoViewHolder(itemView: View, private var context: AppCompatActivity) :
        BaseHolder<PhotoModel>(itemView) {
        private var width = Device.getScreenWidth(context) / 2 - itemView.cardView.marginLeft * 3

        override fun onBind(model: PhotoModel) {
            val height = width * model.height / model.width
            itemView.photoImageView.layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, height
            )
            itemView.setOnClickListener { startPhotoDetailsActivity(model) }

            Glide.with(context)
                .load(model.urlsModel.small)
                .apply(RequestOptions())
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(itemView.photoImageView)
        }

        private fun startPhotoDetailsActivity(model: PhotoModel) {
            Navigation.startPhotoDetailsActivity(
                itemView.photoImageView,
                context,
                model.id,
                model.urlsModel.full,
                model.urlsModel.small,
                model.width,
                model.height
            )
        }
    }
}

