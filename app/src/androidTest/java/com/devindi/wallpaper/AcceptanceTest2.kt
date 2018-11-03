package com.devindi.wallpaper

import android.content.Intent
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//Just another acceptance test to demonstrate that it works
//Remove it after new test merge
@RunWith(AndroidJUnit4::class)
class AcceptanceTest2 {

    @get:Rule
    private val rule = ActivityTestRule(MainActivity::class.java, false, false)

    @Test
    fun runIt() {
        rule.launchActivity(Intent())
    }
}
