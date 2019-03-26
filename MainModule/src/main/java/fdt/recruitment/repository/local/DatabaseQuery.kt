package fdt.recruitment.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fdt.recruitment.entities.PhotoEntity
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface DatabaseQuery {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhotoList(list: MutableList<PhotoEntity>)

    @Query("SELECT * FROM photo_entity")
    fun getPhotoList(): Flowable<List<PhotoEntity>>

    @Query("SELECT * FROM photo_entity WHERE id =:photoId")
    fun getPhotoById(photoId: String): Single<PhotoEntity>

}