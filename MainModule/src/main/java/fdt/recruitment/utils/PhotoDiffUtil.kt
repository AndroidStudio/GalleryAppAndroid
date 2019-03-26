package fdt.recruitment.utils

import androidx.recyclerview.widget.DiffUtil
import fdt.recruitment.entities.PhotoEntity

class PhotoDiffUtil(
    private val oldList: List<PhotoEntity>,
    private val newList: List<PhotoEntity>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return oldItem.avatar == newItem.avatar &&
                oldItem.description == newItem.description &&
                oldItem.first_name == newItem.first_name &&
                oldItem.last_name == newItem.last_name &&
                oldItem.photo_raw == newItem.photo_raw &&
                oldItem.location == newItem.location
    }
}