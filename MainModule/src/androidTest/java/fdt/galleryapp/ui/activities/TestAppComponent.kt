package fdt.galleryapp.ui.activities

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import fdt.galleryapp.component.AppComponent
import fdt.galleryapp.modules.DatabaseModule
import fdt.galleryapp.modules.NetworkModule
import fdt.galleryapp.webservice.WebService
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class, NetworkModule::class])
interface TestAppComponent : AppComponent {

    @Component.Builder
    interface Builder {

        fun build(): TestAppComponent

        @BindsInstance
        fun context(context: Context): Builder

        @BindsInstance
        fun webService(webService: WebService): Builder
    }

    fun inject(test: ApplicationTests)

}