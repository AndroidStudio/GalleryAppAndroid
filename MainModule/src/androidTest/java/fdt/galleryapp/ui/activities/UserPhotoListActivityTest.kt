package fdt.galleryapp.ui.activities

import android.content.Intent
import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import fdt.galleryapp.constants.USER_FIRST_NAME
import fdt.galleryapp.constants.USER_LAST_NAME
import fdt.galleryapp.constants.USER_NAME
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserPhotoListActivityTest {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(
        UserPhotoListActivity::class.java,
        true, false
    )

    @Before
    fun before() {

    }

    @After
    fun after() {

    }

    @Test
    fun checkToolbarTitle() {
        val intent = Intent()
        intent.putExtra(USER_FIRST_NAME, "Janek")
        intent.putExtra(USER_LAST_NAME, "Kowalski")
        intent.putExtra(USER_NAME, "test")

        activityTestRule.launchActivity(intent)

        onView(isAssignableFrom(Toolbar::class.java))
            .check(matches(hasDescendant(withText("FROM JANEK KOWALSKI"))))
    }
}