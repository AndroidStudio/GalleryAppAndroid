package fdt.galleryapp.ui.activities

import android.app.Application
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import fdt.galleryapp.R
import fdt.galleryapp.viewmodel.PhotoDetailsViewModel
import fdt.galleryapp.webservice.WebService
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.spy
import org.mockito.junit.MockitoJUnitRunner
import timber.log.Timber
import javax.inject.Inject

@RunWith(MockitoJUnitRunner::class)
class MockitoSpyTest {

    @Inject
    lateinit var photoDetailsViewModel: PhotoDetailsViewModel

    private lateinit var webService: WebService

    @Before
    fun setupDagger() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        webService = spy(WebService(context))

        DaggerTestAppComponent.builder()
            .setApplication(context as Application)
            .webService(webService)//spy web view service
            .build()
            .inject(this)
    }

    /*https://medium.com/@elye.project/befriending-kotlin-and-mockito-1c2e7b0ef791*/
    @Suppress("UNCHECKED_CAST")
    private fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }

    @Test
    fun testNoInternetConnection() {
        setupRxPlugins()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val message = context.getString(R.string.noInternetConnection)

        `when`(webService.checkInternetConnection()).thenReturn(Single.create {
            it.onError(Exception(message))
        })

        photoDetailsViewModel.getPhotoDetails("", {
            Timber.d("MockitoSpyTest %s", "success")
        }, {
            assertEquals(message, it.message)
        })
        resetRxPlugins()
    }

    @Test
    fun testMapResponseException() {
        setupRxPlugins()
        val context = ApplicationProvider.getApplicationContext<Context>()
        val message = context.getString(R.string.connectionError)

        `when`(webService.mapResponse(any())).thenReturn(Single.create {
            it.onError(Exception(message))
        })

        photoDetailsViewModel.getPhotoDetails("", {
            Timber.d("MockitoSpyTest %s", "success")
        }, {
            assertEquals(message, it.message)
        })
        resetRxPlugins()
    }

    private fun setupRxPlugins() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
    }

    private fun resetRxPlugins() {
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }
}