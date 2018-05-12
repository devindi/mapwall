package com.devindi.wallpaper

import android.content.Intent
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AcceptanceTest {

    @get:Rule
    private val rule = ActivityTestRule(MainActivity::class.java, false, false)

    @Test
    fun runIt() {
        rule.launchActivity(Intent())
    }
}
