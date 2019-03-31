package fdt.galleryapp.repository.photo

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fdt.galleryapp.entities.PhotoEntity
import io.reactivex.Single

@Dao
interface PhotoDatabaseQuery {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhotoList(list: List<PhotoEntity>)

    @Query("SELECT * FROM photo_entity")
    fun getPhotoList(): Single<List<PhotoEntity>>
}