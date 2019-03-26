package fdt.recruitment.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import fdt.recruitment.R
import fdt.recruitment.entities.PhotoEntity
import fdt.recruitment.utils.BaseHolder
import fdt.recruitment.utils.Navigation
import fdt.recruitment.utils.PhotoDiffUtil
import kotlinx.android.synthetic.main.photo_list_item.view.*

class PhotoListAdapter(private val context: AppCompatActivity) :
    RecyclerView.Adapter<PhotoListAdapter.PhotoViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater
        .from(context)

    var photoList: List<PhotoEntity> = arrayListOf()
        set(value) {
            val diff = DiffUtil.calculateDiff(PhotoDiffUtil(field, value))
            field = value
            diff.dispatchUpdatesTo(this)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            layoutInflater.inflate(
                R.layout.photo_list_item,
                parent, false
            ), context
        )
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        holder.onBind(photoList[position])
    }

    class PhotoViewHolder(itemView: View, private val context: AppCompatActivity) : BaseHolder<PhotoEntity>(itemView) {

        override fun onBind(model: PhotoEntity) {
            itemView.titleTextView.text = String.format("%s %s", model.first_name ?: "", model.last_name ?: "")
            itemView.fromButton.text = String.format("FROM %s", model.last_name ?: "")
            itemView.descriptionTextView.text = model.description
            itemView.locationTextView.text = model.location

            Glide.with(context)
                .load(model.avatar)
                .apply(RequestOptions().circleCrop())
                .into(itemView.avatarImageView)

            Glide.with(context)
                .load(model.photo_small)
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

        private fun startUserPhotoListActivity(model: PhotoEntity) {
            Navigation.startUserPhotoListActivity(context, model.user_name, model.last_name, model.first_name)
        }

        private fun startPhotoDetailsActivity(model: PhotoEntity) {
            Navigation.startPhotoDetailsActivity(
                itemView.photoImageView,
                context,
                model.id,
                model.photo_full,
                model.photo_small,
                model.photo_width,
                model.photo_height
            )
        }
    }
}