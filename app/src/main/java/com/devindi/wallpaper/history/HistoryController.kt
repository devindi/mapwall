package com.devindi.wallpaper.history

import android.arch.lifecycle.Observer
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devindi.wallpaper.R
import com.devindi.wallpaper.misc.BaseController
import com.devindi.wallpaper.misc.inject
import com.devindi.wallpaper.model.config.ConfigManager
import kotlinx.android.synthetic.main.history_screen.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class HistoryController : BaseController() {

    private val viewModel: HistoryViewModel by viewModel()
    private val configManager: ConfigManager by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.history_screen, container, false)

        val listView = view.list
        val adapter = HistoryAdapter(configManager)
        listView.layoutManager = LinearLayoutManager(activity)
        listView.adapter = adapter

        viewModel.historyLiveData.observe(this, Observer {
            adapter.items = it!!
        })

        return view
    }
}
