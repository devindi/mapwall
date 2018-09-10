package com.devindi.wallpaper.settings.size.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import com.devindi.wallpaper.R
import com.devindi.wallpaper.misc.*
import com.devindi.wallpaper.model.DeviceInfo
import com.devindi.wallpaper.settings.model.DIMENSION_HEIGHT
import com.devindi.wallpaper.settings.model.DIMENSION_WIDTH
import kotlinx.android.synthetic.main.settings_size_edit.view.*

const val TITLE_KEY = "title"
const val DIMENSION_KEY = "dimen"

class EditSizeController(args: Bundle) : FixedLifecycleController(args) {

    private val viewModel: EditSizeViewModel by viewModel()
    private val deviceInfo: DeviceInfo by inject()
    private val screenSize = when (args.getString(DIMENSION_KEY)) {
        DIMENSION_HEIGHT -> deviceInfo.screenHeight()
        DIMENSION_WIDTH -> deviceInfo.screenWidth()
        else -> throw IllegalArgumentException("Invalid dimension")
    }

    private lateinit var textInput: EditText
    private lateinit var seekInput: SeekBar
    private lateinit var sizePreview: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.settings_size_edit, container, false)
    }

    override fun onAttach(view: View) {
        view.size_edit_title.setText(args.getInt(TITLE_KEY))
        viewModel.loadSize(args.getString(DIMENSION_KEY))

        seekInput = view.seek_input
        textInput = view.input
        sizePreview = view.alter_size_label

        seekInput.setOnSeekBarChangeListener(SimpleSeekBarChangeListener { value, fromUser ->
            if (fromUser) {
                setSizeFromSeek((value + screenSize*0.5).toInt())
            }
        })

        textInput.addTextChangedListener(AfterTextChangedListener { text ->
            if (text.isNotEmpty()) {
                setSizeFromInput(text.toInt())
            } else {
                setSizeFromInput(0)
            }
        })

        seekInput.max = ((screenSize * 10) - screenSize * 0.5).toInt()

        view.ok.setOnClickListener {
            viewModel.setSize(textInput.text.toString().toInt())
            router.popCurrentController()
        }
        view.cancel.setOnClickListener {
            router.popCurrentController()
        }

        viewModel.wallpaperSize().nonNull().observe(this) { size -> initViews(size) }
    }

    private fun setSizeFromSeek(size: Int) {
        textInput.setText(size.toString())
        updatePreview(size)
    }

    private fun setSizeFromInput(size: Int) {
        seekInput.progress = (size - screenSize * 0.5).toInt()
        updatePreview(size)
    }

    private fun initViews(size: Int) {
        seekInput.progress = (size - screenSize * 0.5).toInt()
        textInput.setText(size.toString())
        updatePreview(size)
    }

    private fun updatePreview(size: Int) {
        val multiplier = size / screenSize.toDouble()
        sizePreview.text = String.format("(screen * %.2f)", multiplier)
    }
}
