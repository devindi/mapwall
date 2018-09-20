package com.devindi.wallpaper.about

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import com.devindi.wallpaper.BuildConfig
import com.devindi.wallpaper.R
import com.devindi.wallpaper.misc.BaseController
import com.devindi.wallpaper.misc.ReportManager
import com.devindi.wallpaper.misc.inject
import com.devindi.wallpaper.model.analytics.ScreenEvent
import java.util.Locale

class AboutController : BaseController() {

    private val reportManager: ReportManager by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.about_screen, container, false)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as? AppCompatActivity)?.let {
            it.setSupportActionBar(toolbar)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationOnClickListener { router.popCurrentController() }

        val versionString = String.format(Locale.US,
            container.context.getText(R.string.about_version).toString(),
            BuildConfig.VERSION_NAME)
        view.findViewById<TextView>(R.id.about_version).text = versionString

        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        reportManager.reportEvent(ScreenEvent("about"))
    }
}