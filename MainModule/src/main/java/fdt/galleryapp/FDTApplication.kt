package fdt.galleryapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.stetho.Stetho
import fdt.galleryapp.component.AppComponent
import fdt.galleryapp.component.DaggerAppComponent
import timber.log.Timber

class FDTApplication : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO)

        appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
            Timber.plant(Timber.DebugTree())
        }
    }
}