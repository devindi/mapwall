package com.devindi.wallpaper.search

import android.arch.lifecycle.Observer
import android.content.Context
import android.support.design.widget.Snackbar
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
import com.devindi.wallpaper.misc.ReportManager
import com.devindi.wallpaper.misc.inject
import com.devindi.wallpaper.misc.viewModel
import com.devindi.wallpaper.model.analytics.FailedSearchEvent
import com.devindi.wallpaper.model.analytics.ScreenEvent
import com.devindi.wallpaper.model.analytics.SuccessSearchEvent

interface OnPlacePickedListener {
    fun onPlacePicked(place: Place)
}

class SearchController : LifecycleController() {

    private val viewModel: SearchViewModel by viewModel()
    private val googleApiClientObserver: GoogleApiClientLifecycleObserver by inject()
    private val reportManager: ReportManager by inject()

    private lateinit var search: EditText
    private lateinit var list: View
    private lateinit var suggestsContainer: ViewGroup

    init {
        lifecycle.addObserver(googleApiClientObserver)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.search_screen, container, false)
        view.findViewById<View>(R.id.search_background).setOnClickListener { cancel() }
        view.findViewById<View>(R.id.back_button).setOnClickListener { cancel() }
        search = view.findViewById(R.id.search_edit)
        list = view.findViewById(R.id.list)
        suggestsContainer = view.findViewById(R.id.suggests)
        search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // do nothing
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.requestSuggests(s.toString())
            }
        })
        viewModel.suggests().observe(this, Observer { showSuggests(it) })
        viewModel.place().observe(this, Observer { showPlace(it) })
        viewModel.googleErrorData.observe(
            this,
            Observer {
                Snackbar.make(view, "Something wrong $it", Snackbar.LENGTH_LONG).show()
            })
        return view
    }

    override fun onChangeEnded(
        changeHandler: ControllerChangeHandler,
        changeType: ControllerChangeType
    ) {
        if (changeType != ControllerChangeType.PUSH_ENTER) {
            return
        }
        search.requestFocus()
        val imm = search.context.getSystemService(Context.INPUT_METHOD_SERVICE)
            as InputMethodManager
        imm.showSoftInput(search, InputMethodManager.SHOW_IMPLICIT)
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        reportManager.reportEvent(ScreenEvent("search"))
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
                reportManager.reportEvent(SuccessSearchEvent(suggest.query))
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
            close()
            viewModel.place().value = null
            viewModel.suggests().value = null
        }
    }

    private fun cancel() {
        val query = search.text.toString()
        if (!query.isEmpty()) {
            reportManager.reportEvent(FailedSearchEvent(query))
        }
        close()
    }

    private fun close() {
        val imm = search.context.getSystemService(Context.INPUT_METHOD_SERVICE)
            as InputMethodManager
        imm.hideSoftInputFromWindow(search.windowToken, 0)
        router.popCurrentController()
    }
}
