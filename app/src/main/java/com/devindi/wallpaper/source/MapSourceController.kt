package com.devindi.wallpaper.source

import android.arch.lifecycle.Observer
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import com.devindi.wallpaper.R
import com.devindi.wallpaper.misc.viewModel
import kotlinx.android.synthetic.main.map_source_screen.view.*
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import timber.log.Timber

class MapSourceController: LifecycleController(), OnItemClickListener {

    override fun onItemClick(position: Int, view: View) {
        Timber.d("OnItemClick $position")
    }

    private val viewModel: MapSourceViewModel by viewModel()

    private val adapter = SourcesAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.map_source_screen, container, false)

        val listView = view.list
        listView.layoutManager = LinearLayoutManager(container.context)
        listView.adapter = adapter

        view.findViewById<View>(R.id.btn_close).setOnClickListener {
            Timber.d("Click close")
            router.popCurrentController()
        }

        viewModel.mapSourceList.observe(this, Observer { list -> list?.let { adapter.setItems(it) }; Timber.d("items: $list") })

        return view
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        Timber.d("Attach")
        viewModel.currentMapSource.observe(this, Observer<OnlineTileSourceBase> { Timber.d("New tile source: $it") })
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        Timber.d("Detach")
    }
}