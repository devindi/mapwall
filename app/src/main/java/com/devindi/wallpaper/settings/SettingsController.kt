package com.devindi.wallpaper.settings

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.RouterTransaction
import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.devindi.wallpaper.R
import com.devindi.wallpaper.misc.BaseController
import com.devindi.wallpaper.misc.ReportManager
import com.devindi.wallpaper.misc.inject
import com.devindi.wallpaper.model.analytics.ScreenEvent
import com.devindi.wallpaper.settings.size.edit.DIMENSION_KEY
import com.devindi.wallpaper.settings.size.edit.EditSizeController
import com.devindi.wallpaper.settings.size.edit.TITLE_KEY
import com.devindi.wallpaper.source.OnItemClickListener
import kotlinx.android.synthetic.main.settings_screen.view.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class SettingsController : BaseController(), OnItemClickListener {

    private val reportManager: ReportManager by inject()
    private val viewModel: SettingsViewModel by viewModel()
    private val adapter = SettingsAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.settings_screen, container, false)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        (activity as AppCompatActivity).let {
            it.setSupportActionBar(toolbar)
            it.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationOnClickListener { router.popCurrentController() }

        val listView = view.settings_list
        listView.layoutManager = LinearLayoutManager(activity)
        listView.adapter = adapter
        reportManager.reportEvent(ScreenEvent("settings"))

        viewModel.settings().observe(this) {
            settings ->
            adapter.items = settings
        }
    }

    override fun onItemClick(position: Int, view: View) {
        val args = Bundle()
        args.putString(DIMENSION_KEY, adapter.items[position].key)
        args.putInt(TITLE_KEY, adapter.items[position].titleId)
        router.pushController(
            RouterTransaction.with(EditSizeController(args))
                .pushChangeHandler(FadeChangeHandler(false))
                .popChangeHandler(FadeChangeHandler())
        )
    }
}
