package com.devindi.wallpaper.misc

import android.widget.SeekBar

/**
 * Simple [SeekBar.OnSeekBarChangeListener] implementation with progress observer only.
 *
 * Usage sample:
 * seekBar.setOnSeekBarChangeListener(SimpleSeekBarChangeListener { value, fromUser ->
 *   if (fromUser) {
 *       updateValue(value)
 *   }
 * })
 *
 */
class SimpleSeekBarChangeListener(private val observer: (progress: Int, fromUser: Boolean) -> Unit) : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        observer(progress, fromUser)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
        //do nothing
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        //do nothing
    }
}
