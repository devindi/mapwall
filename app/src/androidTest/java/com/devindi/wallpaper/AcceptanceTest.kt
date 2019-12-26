package com.devindi.wallpaper

import android.content.Intent
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AcceptanceTest {

    @get:Rule
    private val rule = ActivityTestRule(MainActivity::class.java, false, false)

    @Test
    fun runIt0() {
        rule.launchActivity(Intent())
    }

    @Test
    fun runIt1() {
        rule.launchActivity(Intent())
    }
}
