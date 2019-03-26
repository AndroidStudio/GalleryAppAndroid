package fdt.recruitment.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import fdt.recruitment.utils.Navigation
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit


class SplashActivity : AppCompatActivity() {

    private var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(fdt.recruitment.R.layout.splash_activity)
    }

    override fun onResume() {
        super.onResume()
        postponeStartActivity()
    }

    private fun postponeStartActivity() {
        compositeDisposable.add(
            Observable.interval(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .take(3)
                .map { t -> 3 - t }
                .subscribe({ Navigation.startPhotoListActivity(this) }, { it.printStackTrace() })
        )
    }

    override fun onPause() {
        super.onPause()
        compositeDisposable.clear()
    }

}