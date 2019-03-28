package fdt.galleryapp.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
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
import fdt.galleryapp.R
import fdt.galleryapp.constants.*
import fdt.galleryapp.entities.PhotoEntity
import fdt.galleryapp.repository.local.DatabaseQuery
import fdt.galleryapp.repository.local.PhotoDatabase
import fdt.galleryapp.ui.adapters.UserPhotoListAdapter
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.Matchers.equalTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ApplicationTests {

    private lateinit var photoDatabase: PhotoDatabase
    private lateinit var query: DatabaseQuery

    @get:Rule
    val userPhotoListActivityRule = ActivityTestRule(
        UserPhotoListActivity::class.java,
        true, false
    )

    @get:Rule
    val photoDetailsActivityRule = ActivityTestRule(
        PhotoDetailsActivity::class.java,
        true, false
    )

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        photoDatabase = Room.inMemoryDatabaseBuilder(context, PhotoDatabase::class.java).build()
        query = photoDatabase.query()
    }

    @After
    fun closeDb() {
        photoDatabase.close()
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
        query.insertPhotoList(photoList)

        val list = query.getPhotoList().blockingFirst()
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

        onView(withText(R.string.incorrect_user_name))
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

        Espresso.onView(ViewMatchers.withText(R.string.unable_to_display_photo_details))
            .inRoot(RootMatchers.isDialog())
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}