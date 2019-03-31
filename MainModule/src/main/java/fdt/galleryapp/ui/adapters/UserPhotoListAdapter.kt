package fdt.galleryapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import fdt.galleryapp.models.decorators.UserPhotoListItemModel
import fdt.galleryapp.ui.holders.UserPhotoViewHolder

class UserPhotoListAdapter(private var context: AppCompatActivity) : RecyclerView.Adapter<UserPhotoViewHolder>() {

    var photoList: List<UserPhotoListItemModel> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPhotoViewHolder {
        return UserPhotoViewHolder(
            layoutInflater.inflate(fdt.galleryapp.R.layout.user_photo_list_item, parent, false)
            , context
        )
    }

    override fun getItemCount(): Int {
        return photoList.size
    }

    override fun onBindViewHolder(holder: UserPhotoViewHolder, position: Int) {
        holder.onBind(photoList[position])
    }
}
