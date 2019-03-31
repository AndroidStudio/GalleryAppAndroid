package fdt.galleryapp.component

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import fdt.galleryapp.GalleryApplication
import fdt.galleryapp.modules.ActivitiesModule
import fdt.galleryapp.modules.ContextModude
import fdt.galleryapp.modules.DatabaseModule
import fdt.galleryapp.modules.NetworkModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DatabaseModule::class, NetworkModule::class, ContextModude::class,
        AndroidInjectionModule::class, ActivitiesModule::class]
)
interface AppComponent {

    @Component.Builder
    interface Builder {

        fun build(): AppComponent

        @BindsInstance
        fun setApplication(application: Application): Builder
    }

    fun inject(galleryApplication: GalleryApplication)

}

