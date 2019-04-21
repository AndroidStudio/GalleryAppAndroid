package fdt.galleryapp

import android.app.Activity
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.stetho.Stetho
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import fdt.galleryapp.component.DaggerAppComponent
import timber.log.Timber
import javax.inject.Inject

class GalleryApplication : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        DaggerAppComponent.builder()
            .setApplication(this)
            .build()
            .inject(this)

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun activityInjector() = dispatchingAndroidInjector

}