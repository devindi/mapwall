package com.devindi.wallpaper.source

import android.net.Uri
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.devindi.wallpaper.R
import com.devindi.wallpaper.model.map.MapSource
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.map_source_item.view.*

class SourcesAdapter(private val clickListener: OnItemClickListener): RecyclerView.Adapter<SourceViewHolder>() {

    private var oldItems = emptyList<MapSource>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.map_source_item, parent, false)
        return SourceViewHolder(view, clickListener)
    }

    override fun getItemCount() = oldItems.size

    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {
        holder.titleView.text = oldItems[position].title
        Picasso.with(holder.itemView.context).load(createThumbUri(oldItems[position])).into(holder.imgView)
    }

    fun setItems(items: List<MapSource>) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val old = oldItems[oldItemPosition]
                val new = items[newItemPosition]
                return old == new
            }

            override fun getOldListSize() = oldItems.size

            override fun getNewListSize() = items.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return areItemsTheSame(oldItemPosition, newItemPosition)
            }
        })

        diff.dispatchUpdatesTo(this)
        oldItems = items
    }

    private fun createThumbUri(mapSource: MapSource): Uri {
        return Uri.parse("osm://mapnik?latN=0&latS=1&lonW=-1&lonE=1")
    }
}

class SourceViewHolder(val view: View, listener: OnItemClickListener): RecyclerView.ViewHolder(view) {
    val titleView: TextView = view.title
    val imgView: ImageView = view.imageView

    init {
        view.setOnClickListener {
            listener.onItemClick(adapterPosition, it)
        }
    }
}

interface OnItemClickListener {
    fun onItemClick(position: Int, view: View)
}