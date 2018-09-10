package com.devindi.wallpaper.settings.size

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.devindi.wallpaper.settings.model.IntField
import com.devindi.wallpaper.source.OnItemClickListener
import kotlinx.android.synthetic.main.settings_simple_item.view.*

class SizeViewHolder(view: View, clickListener: OnItemClickListener) : RecyclerView.ViewHolder(view) {

    private val titleLabel: TextView = view.settings_title
    private val valueLabel: TextView = view.settings_value

    init {
        view.setOnClickListener{
            clickListener.onItemClick(adapterPosition, it)
        }
    }

    fun bindSettingsField(value: IntField) {
        titleLabel.setText(value.titleId)
        valueLabel.text = "${value.get(1000)} px"
    }
}
