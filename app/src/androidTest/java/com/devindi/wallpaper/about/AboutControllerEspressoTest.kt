package com.devindi.wallpaper.about

import android.content.Intent
import android.preference.PreferenceManager
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.bluelinelabs.conductor.RouterTransaction
import com.devindi.wallpaper.BuildConfig
import com.devindi.wallpaper.MainActivity
import com.devindi.wallpaper.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@RunWith(AndroidJUnit4::class)
class AboutControllerEspressoTest {

    private val osmCachePath = "osm"

    @get:Rule
    val activityTestRule = ActivityTestRule<MainActivity>(MainActivity::class.java, true, false)

    private val controller = AboutController()

    @Before
    fun setup() {
        PreferenceManager.getDefaultSharedPreferences(getInstrumentation().context)
                .edit().putString(osmCachePath, "testPath").apply()
        activityTestRule.launchActivity(Intent())
        activityTestRule.runOnUiThread {
            activityTestRule.activity.router.setRoot(RouterTransaction.with(controller))
        }
    }

    @Test
    fun testVersionText() {
        val expectedVersion = String.format(Locale.US,
                activityTestRule.activity.getText(R.string.about_version).toString(),
                BuildConfig.VERSION_NAME)
        onView(withId(R.id.about_version)).check(matches(withText(expectedVersion)))
    }
}
