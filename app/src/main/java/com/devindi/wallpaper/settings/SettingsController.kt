package com.devindi.wallpaper.settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.RouterTransaction
import com.devindi.wallpaper.R
import com.devindi.wallpaper.misc.BaseController
import com.devindi.wallpaper.misc.ReportManager
import com.devindi.wallpaper.misc.inject
import com.devindi.wallpaper.model.analytics.ScreenEvent
import com.devindi.wallpaper.settings.model.DIMENSION_HEIGHT
import com.devindi.wallpaper.settings.model.DIMENSION_WIDTH
import com.devindi.wallpaper.settings.model.SettingsManager
import com.devindi.wallpaper.settings.size.edit.DIMENSION_KEY
import com.devindi.wallpaper.settings.size.edit.EditSizeController
import com.devindi.wallpaper.settings.size.edit.TITLE_KEY
import com.devindi.wallpaper.source.OnItemClickListener
import kotlinx.android.synthetic.main.settings_screen.view.*

class SettingsController : BaseController(), OnItemClickListener {

    private val reportManager: ReportManager by inject()
    private val settingsManager: SettingsManager by inject()

    private val items = listOf(
        settingsManager.getIntField(DIMENSION_HEIGHT),
        settingsManager.getIntField(DIMENSION_WIDTH))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.settings_screen, container, false)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).let {
            it.setSupportActionBar(toolbar)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationOnClickListener { router.popCurrentController() }

        val listView = view.settings_list
        listView.layoutManager = LinearLayoutManager(activity)
        listView.adapter = SettingsAdapter(items, this)

        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        reportManager.reportEvent(ScreenEvent("settings"))
    }

    override fun onItemClick(position: Int, view: View) {
        val args = Bundle()
        args.putString(DIMENSION_KEY, items[position].key)
        args.putInt(TITLE_KEY, items[position].titleId)
        router.pushController(RouterTransaction.with(EditSizeController(args)))
    }
}
