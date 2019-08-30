package com.devindi.wallpaper.settings.target

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Switch
import android.widget.TextView
import com.devindi.wallpaper.settings.model.BooleanField
import kotlinx.android.synthetic.main.settings_checkbox_item.view.*

class TargetViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val titleLabel: TextView = view.settings_title
    private val valueSwitch: Switch = view.settings_value

    fun bindSettingsField(value: BooleanField) {
        titleLabel.setText(value.titleId)
        valueSwitch.isChecked = value.get()
        valueSwitch.setOnCheckedChangeListener { _, isChecked -> value.set(isChecked) }
    }
}
