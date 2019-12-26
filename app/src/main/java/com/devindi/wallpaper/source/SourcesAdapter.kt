package com.devindi.wallpaper.source

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.devindi.wallpaper.R
import com.devindi.wallpaper.model.map.MapSource
import com.devindi.wallpaper.model.map.buildTileRequest
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.map_source_item.view.*

class SourcesAdapter(private val sourceListener: OnSourceSelected) :
    RecyclerView.Adapter<SourceViewHolder>(), OnItemClickListener {

    private var items = emptyList<MapSource>()
    private var selectedItem: MapSource = MapSource("", "")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SourceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.map_source_item, parent, false)
        return SourceViewHolder(view, this)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: SourceViewHolder, position: Int) {
        holder.titleView.text = items[position].title
        Picasso.with(holder.itemView.context)
            .load(Uri.parse(buildTileRequest(items[position].id)))
            .into(holder.imgView)
        holder.mark.visibility = if (position == items.indexOf(selectedItem)) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }
    }

    override fun onItemClick(position: Int, view: View) {
        val selectedItem = items[position]
        if (selectedItem == this.selectedItem) {
            sourceListener.onSourceSelected(selectedItem, false)
            return
        }
        sourceListener.onSourceSelected(selectedItem, true)
        changeCurrentMapSource(selectedItem)
    }

    fun setItems(items: List<MapSource>) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                val old = this@SourcesAdapter.items[oldItemPosition]
                val new = items[newItemPosition]
                return old == new
            }

            override fun getOldListSize() = this@SourcesAdapter.items.size

            override fun getNewListSize() = items.size

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return true
            }
        })

        diff.dispatchUpdatesTo(this)
        this.items = items
    }

    fun setCurrentItem(selectedSource: MapSource) {
        if (selectedItem == selectedSource) {
            return
        }
        changeCurrentMapSource(selectedSource)
    }

    private fun changeCurrentMapSource(selectedSource: MapSource) {
        val diff = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return true
            }

            override fun getOldListSize(): Int {
                return items.size
            }

            override fun getNewListSize(): Int {
                return items.size
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                if (items[oldItemPosition] == selectedSource) return false
                return true
            }
        })
        diff.dispatchUpdatesTo(this)

        selectedItem = selectedSource
    }
}

class SourceViewHolder(val view: View, listener: OnItemClickListener) :
    RecyclerView.ViewHolder(view) {
    val titleView: TextView = view.title
    val imgView: ImageView = view.imageView
    val mark: View = view.selected

    init {
        view.setOnClickListener {
            listener.onItemClick(adapterPosition, it)
        }
    }
}

interface OnItemClickListener {
    fun onItemClick(position: Int, view: View)
}

interface OnSourceSelected {
    fun onSourceSelected(source: MapSource, changed: Boolean)
}
