package fdt.galleryapp.modules

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import fdt.galleryapp.entities.PhotoEntity
import fdt.galleryapp.repository.photo.PhotoDatabaseQuery
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "photo_database")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    @Database(entities = [PhotoEntity::class], version = 7, exportSchema = false)
    abstract class AppDatabase : RoomDatabase() {

        abstract fun photoQuery(): PhotoDatabaseQuery

    }
}