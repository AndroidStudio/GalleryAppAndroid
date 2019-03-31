package fdt.galleryapp.utils

import androidx.recyclerview.widget.DiffUtil
import fdt.galleryapp.models.PhotoListItemModel

class PhotoDiffUtil(
    private val oldList: List<PhotoListItemModel>,
    private val newList: List<PhotoListItemModel>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}