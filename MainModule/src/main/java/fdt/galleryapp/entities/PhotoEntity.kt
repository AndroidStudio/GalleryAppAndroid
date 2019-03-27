package fdt.galleryapp.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo_entity")
data class PhotoEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "user_name") var user_name: String,
    @ColumnInfo(name = "first_name") var first_name: String?,
    @ColumnInfo(name = "last_name") var last_name: String?,
    @ColumnInfo(name = "description") var description: String?,
    @ColumnInfo(name = "location") var location: String?,
    @ColumnInfo(name = "avatar") var avatar: String?,
    @ColumnInfo(name = "photo_small") var photo_small: String?,
    @ColumnInfo(name = "photo_thumb") var photo_thumb: String?,
    @ColumnInfo(name = "photo_raw") var photo_raw: String?,
    @ColumnInfo(name = "photo_full") var photo_full: String?,
    @ColumnInfo(name = "photo_width") var photo_width: Int,
    @ColumnInfo(name = "photo_height") var photo_height: Int
)