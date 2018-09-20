package com.devindi.wallpaper.source

import android.arch.lifecycle.Observer
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import com.devindi.wallpaper.R
import com.devindi.wallpaper.misc.BaseController
import com.devindi.wallpaper.misc.ReportManager
import com.devindi.wallpaper.misc.inject
import com.devindi.wallpaper.model.analytics.ChooseMapEvent
import com.devindi.wallpaper.model.analytics.ScreenEvent
import com.devindi.wallpaper.model.map.MapSource
import kotlinx.android.synthetic.main.map_source_screen.view.*
import org.koin.android.viewmodel.ext.android.viewModel

class MapSourceController : BaseController() {

    private val viewModel: MapSourceViewModel by viewModel()
    private val reportManager by inject<ReportManager>()

    private val adapter = SourcesAdapter(object : OnSourceSelected {
        override fun onSourceSelected(source: MapSource, changed: Boolean) {
            if (changed) {
                viewModel.setCurrentSource(source)
                reportManager.reportEvent(ChooseMapEvent(source.id))
            }
            router.popCurrentController()
        }
    })

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.map_source_screen, container, false)

        val listView = view.list
        listView.layoutManager = LinearLayoutManager(container.context)
        listView.adapter = adapter

        view.findViewById<View>(R.id.btn_close).setOnClickListener {
            router.popCurrentController()
        }

        view.findViewById<View>(R.id.dialog_window).setOnClickListener {
            router.popCurrentController()
        }

        viewModel.mapSourceList.observe(
            this,
            Observer<List<MapSource>> { list -> list?.let { adapter.setItems(it) } }
        )
        viewModel.currentMapSource.observe(
            this,
            Observer { selected -> selected?.let { adapter.setCurrentItem(it) } }
        )

        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        reportManager.reportEvent(ScreenEvent("map source"))
    }
}
