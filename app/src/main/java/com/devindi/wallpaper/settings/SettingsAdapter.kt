package com.devindi.wallpaper.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devindi.wallpaper.R
import com.devindi.wallpaper.settings.model.BooleanField
import com.devindi.wallpaper.settings.model.IntField
import com.devindi.wallpaper.settings.model.SettingsField
import com.devindi.wallpaper.settings.size.SizeViewHolder
import com.devindi.wallpaper.settings.target.TargetViewHolder
import com.devindi.wallpaper.source.OnItemClickListener

class SettingsAdapter(
    private val clickListener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<SettingsField<*>> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.settings_simple_item, parent, false)
                SizeViewHolder(view, clickListener)
            }
            1 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.settings_checkbox_item, parent, false)
                TargetViewHolder(view)
            }
            else -> throw IllegalStateException("Unknown view type: $viewType")
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is IntField -> 0
            is BooleanField -> 1
            else -> throw IllegalStateException("Unknown view item: ${items[position]}")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            0 -> (holder as SizeViewHolder).bindSettingsField(items[position] as IntField)
            1 -> (holder as TargetViewHolder).bindSettingsField(items[position] as BooleanField)
        }
    }
}
