package fdt.galleryapp.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fdt.galleryapp.entities.PhotoEntity
import io.reactivex.Flowable

@Dao
interface DatabaseQuery {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhotoList(list: MutableList<PhotoEntity>)

    @Query("SELECT * FROM photo_entity")
    fun getPhotoList(): Flowable<List<PhotoEntity>>
}