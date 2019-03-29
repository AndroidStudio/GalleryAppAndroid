package fdt.galleryapp.ui.activities

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
import androidx.test.rule.ActivityTestRule
import com.google.gson.JsonSyntaxException
import fdt.galleryapp.R
import fdt.galleryapp.constants.*
import fdt.galleryapp.entities.PhotoEntity
import fdt.galleryapp.repository.local.DatabaseQuery
import fdt.galleryapp.repository.local.PhotoDatabase
import fdt.galleryapp.repository.remote.RemoteRepository
import fdt.galleryapp.ui.adapters.UserPhotoListAdapter
import fdt.galleryapp.webservice.WebService
import io.reactivex.Single
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.mockito.Mockito
import javax.inject.Inject

class ApplicationTests {

    private lateinit var photoDatabase: PhotoDatabase
    private lateinit var databaseQuery: DatabaseQuery

    @Inject
    lateinit var remoteRepository: RemoteRepository

    @Inject
    lateinit var webService: WebService

    /*https://medium.com/@elye.project/befriending-kotlin-and-mockito-1c2e7b0ef791*/
    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> uninitialized(): T = null as T

    @get:Rule
    val expectedException: ExpectedException = ExpectedException.none()

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
        photoDatabase = Room.inMemoryDatabaseBuilder(context, PhotoDatabase::class.java).build()
        databaseQuery = photoDatabase.query()
    }

    @Before
    fun setupDagger() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        DaggerTestAppComponent.builder()
            .context(context)
            .webService(Mockito.mock(WebService::class.java))
            .build().inject(this)
    }

    @After
    fun closeDb() {
        photoDatabase.close()
    }

    @Test
    fun testIncorrectJSON() {
        val response = "incorrect json"
        Mockito.`when`(webService.request(any())).thenReturn(Single.just(response))

        expectedException.expect(JsonSyntaxException::class.java)
        remoteRepository.getPhotoList().map { remoteRepository.mapUserPhotoList(it) }.blockingGet()
    }

    @Test
    fun testSavePhotoAndReadPhotoList() {
        val photoList = mutableListOf(
            PhotoEntity(
                "id", "", "", "",
                "", "", "", "", "", "", "",
                0, 0
            ),
            PhotoEntity(
                "id", "", "", "",
                "", "", "", "", "", "", "",
                0, 0
            )
        )
        databaseQuery.insertPhotoList(photoList)

        val list = databaseQuery.getPhotoList().blockingFirst()
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
                RecyclerViewActions.actionOnItemAtPosition<UserPhotoListAdapter.UserPhotoViewHolder>(0, click())
            )

        intended(
            allOf(
                hasExtraWithKey(PHOTO_ID),
                hasExtraWithKey(PHOTO_URL_FULL),
                hasExtraWithKey(PHOTO_URL),
                hasExtraWithKey(PHOTO_WIDTH),
                hasExtraWithKey(PHOTO_HEIGHT)
            )
        )

        Intents.release()
    }

    @Test
    fun testPhotoDetailsErrorIsDisplayed() {
        val intent = Intent()
        intent.putExtra(PHOTO_ID, "test")
        intent.putExtra(PHOTO_URL_FULL, "test")
        val url: String? = null
        intent.putExtra(PHOTO_URL, url)
        intent.putExtra(PHOTO_HEIGHT, 100)
        intent.putExtra(PHOTO_WIDTH, 100)
        photoDetailsActivityRule.launchActivity(intent)
        Thread.sleep(1000)

        onView(ViewMatchers.withText(R.string.unableToDisplayPhotoDetails))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}