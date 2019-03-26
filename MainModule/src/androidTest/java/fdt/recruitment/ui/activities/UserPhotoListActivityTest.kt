package fdt.recruitment.ui.activities

import android.content.Intent
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import fdt.recruitment.R
import fdt.recruitment.constants.USER_FIRST_NAME
import fdt.recruitment.constants.USER_LAST_NAME
import fdt.recruitment.constants.USER_NAME
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

        Espresso.onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("FROM JANEK KOWALSKI"))))
    }
}