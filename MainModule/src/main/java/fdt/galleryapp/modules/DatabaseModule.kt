package fdt.galleryapp.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import fdt.galleryapp.repository.local.PhotoDatabase
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): PhotoDatabase {
        return Room.databaseBuilder(context, PhotoDatabase::class.java, "photo_database")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
}