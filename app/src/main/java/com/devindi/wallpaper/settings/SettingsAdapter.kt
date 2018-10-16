package com.devindi.wallpaper.settings

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.devindi.wallpaper.R
import com.devindi.wallpaper.settings.model.IntField
import com.devindi.wallpaper.settings.size.SizeViewHolder
import com.devindi.wallpaper.source.OnItemClickListener

class SettingsAdapter(
    private val clickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<IntField> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.settings_simple_item, parent, false)
        return SizeViewHolder(view, clickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as SizeViewHolder).bindSettingsField(items[position])
    }
}
