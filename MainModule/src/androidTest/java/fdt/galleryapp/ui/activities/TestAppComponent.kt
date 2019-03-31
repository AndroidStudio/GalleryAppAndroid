package fdt.galleryapp.ui.activities

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import fdt.galleryapp.component.AppComponent
import fdt.galleryapp.modules.ActivitiesModule
import fdt.galleryapp.modules.ContextModude
import fdt.galleryapp.modules.DatabaseModule
import fdt.galleryapp.modules.NetworkModule
import fdt.galleryapp.webservice.WebService
import javax.inject.Singleton

@Singleton
@Component(
    modules = [DatabaseModule::class, NetworkModule::class, ContextModude::class, AndroidInjectionModule::class,
        ActivitiesModule::class]
)
interface TestAppComponent : AppComponent {

    @Component.Builder
    interface Builder {

        fun build(): TestAppComponent

        @BindsInstance
        fun mockWebService(webService: WebService): Builder

        @BindsInstance
        fun setApplication(application: Application): Builder
    }

    fun inject(test: ApplicationTests)

}