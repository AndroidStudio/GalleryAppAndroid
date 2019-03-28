package fdt.galleryapp.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import fdt.galleryapp.entities.PhotoEntity

@Database(entities = [PhotoEntity::class], version = 6, exportSchema = false)
abstract class PhotoDatabase : RoomDatabase() {

    abstract fun query(): DatabaseQuery

}