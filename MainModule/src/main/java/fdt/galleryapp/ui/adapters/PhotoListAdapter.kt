package fdt.galleryapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import fdt.galleryapp.R
import fdt.galleryapp.models.decorators.PhotoListItemModel
import fdt.galleryapp.ui.holders.PhotoViewHolder
import fdt.galleryapp.utils.PhotoDiffUtil

class PhotoListAdapter(private val context: AppCompatActivity) : RecyclerView.Adapter<PhotoViewHolder>() {

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    var photoList: List<PhotoListItemModel> = arrayListOf()
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
}