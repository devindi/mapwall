package com.devindi.wallpaper.misc

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar

class ToolbarHelper {

    fun enableBackButton(activity: Activity?, toolbar: Toolbar) {
        (activity as? AppCompatActivity)?.run {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }
}
