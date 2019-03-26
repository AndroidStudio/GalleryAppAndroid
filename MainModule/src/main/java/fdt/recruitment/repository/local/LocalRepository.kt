package fdt.recruitment.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import fdt.recruitment.entities.PhotoEntity

@Database(entities = [PhotoEntity::class], version = 6, exportSchema = false)
abstract class LocalRepository : RoomDatabase() {

    abstract fun query(): DatabaseQuery

}