package com.devindi.wallpaper.misc

import android.text.Editable
import android.text.TextWatcher

class AfterTextChangedListener(val observer: (text: String) -> Unit) : TextWatcher {
    override fun afterTextChanged(s: Editable?) {
        observer(s.toString())
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        //do nothing
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        //do nothing
    }
}
