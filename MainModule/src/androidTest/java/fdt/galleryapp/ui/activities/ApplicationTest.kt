package fdt.galleryapp.ui.activities

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import fdt.galleryapp.R
import fdt.galleryapp.constants.PHOTO_DETAILS_PARAMETERS
import fdt.galleryapp.constants.USER_FIRST_NAME
import fdt.galleryapp.constants.USER_LAST_NAME
import fdt.galleryapp.constants.USER_NAME
import fdt.galleryapp.entities.PhotoEntity
import fdt.galleryapp.models.PhotoModel
import fdt.galleryapp.modules.DatabaseModule
import fdt.galleryapp.parametres.PhotoDetailsParameters
import fdt.galleryapp.repository.photo.PhotoDatabaseQuery
import fdt.galleryapp.ui.holders.UserPhotoViewHolder
import fdt.galleryapp.viewmodel.PhotoDetailsViewModel
import fdt.galleryapp.viewmodel.PhotoListViewModel
import fdt.galleryapp.webservice.WebService
import io.reactivex.Single
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matchers.equalTo
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import timber.log.Timber
import javax.inject.Inject

@RunWith(MockitoJUnitRunner::class)
class ApplicationTest {

    private lateinit var appDatabase: DatabaseModule.AppDatabase
    private lateinit var databaseQuery: PhotoDatabaseQuery

    @Inject
    lateinit var photoDetailsViewModel: PhotoDetailsViewModel

    @Inject
    lateinit var photoListViewModel: PhotoListViewModel

    lateinit var webService: WebService

    /*https://medium.com/@elye.project/befriending-kotlin-and-mockito-1c2e7b0ef791*/
    @Suppress("UNCHECKED_CAST")
    private fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }

    @get:Rule
    val userPhotoListActivityRule = ActivityTestRule(
        UserPhotoListActivity::class.java, true, false
    )

    @get:Rule
    val photoDetailsActivityRule = ActivityTestRule(
        PhotoDetailsActivity::class.java, true, false
    )

    @Before
    fun setupDatabase() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        appDatabase = Room.inMemoryDatabaseBuilder(context, DatabaseModule.AppDatabase::class.java).build()
        databaseQuery = appDatabase.photoQuery()
    }

    @Before
    fun setupDagger() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        webService = Mockito.spy(WebService(context))

        DaggerTestAppComponent.builder()
            .setApplication(context as Application)
            .webService(webService)
            .build()
            .inject(this)
    }

    @After
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    fun testGSON() {
        parseJson<List<PhotoModel>>()
            .subscribe({
                assertThat(30, `is`(it.size))
            }, {
                it.printStackTrace()
            })
    }

    private inline fun <reified T> parseJson(): Single<T> {
        val type = object : TypeToken<T>() {}.type
        val stream = InstrumentationRegistry.getInstrumentation().context.resources.assets
            .open("photo_list.txt")
        val response = stream.bufferedReader().use { it.readText() }
        return Single.create<T> {it.onSuccess(Gson().fromJson<T>(response, type)) }
    }

//
//    @Test
//    fun testWebService() {
//        Mockito.`when`<Single<List<PhotoModel>>>(webService.request(any())).thenReturn(Single.create {
//            it.onSuccess(listOf())
//        })
//
//        photoListViewModel.getPhotoList({
//            it.size
//        },{
//            it.printStackTrace()
//        })
//    }

    @Test
    fun testNoInternetConnection() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val message = context.getString(R.string.noInternetConnection)

        Mockito.`when`(webService.verifyInternetConnection()).thenReturn(Single.create {
            it.onError(Exception(message))
        })

        photoDetailsViewModel.getPhotoDetails("", {
            Timber.d("MockitoSpyTest %s", "success")
        }, {
            Assert.assertEquals(message, it.message)
        })
    }

    @Test
    fun testMapResponseException() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val message = context.getString(R.string.connectionError)

        Mockito.`when`(webService.interceptResponseBody(any())).thenReturn(Single.create {
            it.onError(Exception(message))
        })

        photoDetailsViewModel.getPhotoDetails("", {
            Timber.d("MockitoSpyTest %s", "success")
        }, {
            Assert.assertEquals(message, it.message)
        })
    }

    @Test
    fun testSavePhotoAndReadPhotoList() {
        val photoList = mutableListOf(
            PhotoEntity(
                "photoId", "", "", "",
                "", "", "", "", "", "", "",
                0, 0
            )
        )
        databaseQuery.insertPhotoList(photoList)

        val list = databaseQuery.getPhotoList().blockingGet()
        assertThat(list[0].id, equalTo(photoList[0].id))
    }

    @Test
    fun testUserPhotoListToolbarTitle() {
        val intent = Intent()
        intent.putExtra(USER_FIRST_NAME, "Janek")
        intent.putExtra(USER_LAST_NAME, "Kowalski")
        intent.putExtra(USER_NAME, "test")

        userPhotoListActivityRule.launchActivity(intent)
        Thread.sleep(1000)

        onView(isAssignableFrom(Toolbar::class.java))
            .check(matches(hasDescendant(withText("FROM JANEK KOWALSKI"))))
    }

    @Test
    fun testNoUserNameErrorDialogIsDisplayed() {
        userPhotoListActivityRule.launchActivity(null)
        Thread.sleep(1000)

        onView(withText(R.string.incorrectUserName))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))
    }

    @Test
    fun testUserPhotoListItemClickExtras() {
        Intents.init()
        val userName = "starvingartistfoodphotography"

        val intent = Intent()
        intent.putExtra(USER_NAME, userName)
        userPhotoListActivityRule.launchActivity(intent)
        /*time to load photos*/
        Thread.sleep(1000)

        onView(withId(R.id.recyclerView))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<UserPhotoViewHolder>(0, click())
            )

        intended(
            allOf(
                hasExtraWithKey(PHOTO_DETAILS_PARAMETERS)
            )
        )

        Intents.release()
    }

    @Test
    fun testPhotoDetailsErrorIsDisplayed() {
        val intent = Intent()
        intent.putExtra(
            PHOTO_DETAILS_PARAMETERS, PhotoDetailsParameters.create(
                null,
                null,
                null,
                null,
                null
            )
        )

        photoDetailsActivityRule.launchActivity(intent)
        Thread.sleep(1000)

        onView(ViewMatchers.withText(R.string.unableToDisplayPhotoDetails))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}
