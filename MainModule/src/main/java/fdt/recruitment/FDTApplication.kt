package fdt.recruitment

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.facebook.stetho.Stetho
import fdt.recruitment.component.AppComponent
import fdt.recruitment.component.DaggerAppComponent
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