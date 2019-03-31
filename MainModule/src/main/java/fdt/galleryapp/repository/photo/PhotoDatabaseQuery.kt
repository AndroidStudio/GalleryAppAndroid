package fdt.galleryapp.repository.photo

import androidx.room.*
import fdt.galleryapp.entities.PhotoEntity
import io.reactivex.Single

@Dao
interface PhotoDatabaseQuery {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhotoList(list: List<PhotoEntity>)

    @Transaction
    @Query("SELECT * FROM photo_entity")
    fun getPhotoList(): Single<List<PhotoEntity>>
}