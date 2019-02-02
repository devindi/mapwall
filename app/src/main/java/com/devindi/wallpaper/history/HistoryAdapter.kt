package com.devindi.wallpaper.history

import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.devindi.wallpaper.R
import com.devindi.wallpaper.model.config.ConfigManager
import com.devindi.wallpaper.model.history.WallpaperEntry
import com.devindi.wallpaper.model.map.buildTileRequest
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.history_item.view.*
import timber.log.Timber
import java.text.SimpleDateFormat

class HistoryAdapter(
    private val configManager: ConfigManager
) : RecyclerView.Adapter<HistoryAdapter.HistoryItemVh>() {

    var items: List<WallpaperEntry> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemVh {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.history_item, parent, false)
        return HistoryItemVh(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: HistoryItemVh, position: Int) {
        val locationFormat = "%.2f %.2f"
        val timeFormat = SimpleDateFormat()

        val entry = items[position]
        val mapSource = configManager.config.availableSources.find { it.id == entry.mapSourceId }!!

        val tileRequest = buildTileRequest(
            mapSource.id,
            entry.width,
            entry.height,
            15,
            entry.centerLat,
            entry.centerLon)
        Timber.d("Binding history item $entry, $mapSource $tileRequest")
        Picasso.with(holder.itemView.context)
            .load(Uri.parse(tileRequest))
            .into(holder.mapPreview)
        holder.mapTitle.text = mapSource.title
        holder.wallpaperCoordinates.text = String.format(
            locationFormat, entry.centerLat, entry.centerLon)
        holder.tileLabel.text = timeFormat.format(entry.createdAt.time)
    }

    class HistoryItemVh(view: View) : RecyclerView.ViewHolder(view) {
        val mapPreview: ImageView = view.map_preview
        val mapTitle: TextView = view.style_title
        val wallpaperCoordinates: TextView = view.location_title
        val tileLabel: TextView = view.created_at
    }
}
