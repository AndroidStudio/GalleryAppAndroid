package fdt.galleryapp.component

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import fdt.galleryapp.modules.DatabaseModule
import fdt.galleryapp.modules.NetworkModule
import fdt.galleryapp.viewmodel.PhotoDetailsViewModel
import fdt.galleryapp.viewmodel.PhotoListViewModel
import fdt.galleryapp.viewmodel.UserPhotoListViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class, NetworkModule::class])
interface AppComponent {

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun context(context: Context): Builder
    }

    fun inject(model: PhotoListViewModel)

    fun inject(model: PhotoDetailsViewModel)

    fun inject(model: UserPhotoListViewModel)

    fun getContext(): Context
}

