package fdt.recruitment.component

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import fdt.recruitment.modules.DatabaseModule
import fdt.recruitment.viewmodel.PhotoDetailsViewModel
import fdt.recruitment.viewmodel.PhotoListViewModel
import fdt.recruitment.viewmodel.UserPhotoListViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class])
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

