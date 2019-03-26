package fdt.recruitment.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import fdt.recruitment.repository.local.LocalRepository
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): LocalRepository {
        return Room.databaseBuilder(context, LocalRepository::class.java, "photo_database")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }
}