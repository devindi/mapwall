package com.devindi.wallpaper.search

import android.arch.lifecycle.Observer
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import com.devindi.wallpaper.R
import com.devindi.wallpaper.misc.inject
import com.devindi.wallpaper.misc.viewModel

interface OnPlacePickedListener {
    fun onPlacePicked(place: Place)
}

class SearchController : LifecycleController() {

    private val viewModel: SearchViewModel by viewModel()
    private val googleApiClientObserver: GoogleApiClientLifecycleObserver by inject()

    private lateinit var search: EditText
    private lateinit var list: View
    private lateinit var suggestsContainer: ViewGroup

    init {
        lifecycle.addObserver(googleApiClientObserver)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        return inflater.inflate(R.layout.search_screen, container, false)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        search = view.findViewById(R.id.search_edit)
        list = view.findViewById(R.id.list)
        suggestsContainer = view.findViewById(R.id.suggests)
        search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //do nothing
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.requestSuggests(s.toString())
            }
        })

        view.findViewById<View>(R.id.modal_bg).setOnClickListener { router.popCurrentController() }

        viewModel.suggests().observe(this, Observer { showSuggests(it) })
        viewModel.place().observe(this, Observer { showPlace(it) })
    }

    override fun onChangeEnded(changeHandler: ControllerChangeHandler, changeType: ControllerChangeType) {
        if (changeType != ControllerChangeType.PUSH_ENTER) {
            return
        }
        search.requestFocus()
        val imm = search.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(search, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onDetach(view: View) {
        super.onDetach(view)
        viewModel.suggests().removeObservers(this)
    }

    private fun showSuggests(list: List<PlaceSuggest>?) {
        list?.forEachIndexed { index, suggest ->
            var itemView = suggestsContainer.getChildAt(index)
            if (itemView == null) {
                itemView = LayoutInflater.from(suggestsContainer.context)
                        .inflate(R.layout.search_suggest_item, suggestsContainer, false)
                suggestsContainer.addView(itemView)
            }
            itemView.findViewById<TextView>(R.id.title).text = suggest.title
            itemView.findViewById<TextView>(R.id.description).text = suggest.description
            itemView.setOnClickListener {
                viewModel.requestPlace(suggest)
            }
        }
        list?.let {
            while (suggestsContainer.childCount > it.size) {
                suggestsContainer.removeViewAt(it.size)
            }
        }
    }

    private fun showPlace(place: Place?) {
        place?.let {
            (targetController as OnPlacePickedListener).onPlacePicked(it)
            router.popCurrentController()
            viewModel.place().value = null
            viewModel.suggests().value = null
        }
    }
}
