package com.devindi.wallpaper.sample.dialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import com.devindi.wallpaper.R

class DialogController(): LifecycleController() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
        val view = inflater.inflate(R.layout.dialog_screen, container, false)

        view.findViewById<View>(R.id.dialog_window).setOnClickListener {
            router.popCurrentController()
        }

        view.findViewById<View>(R.id.dismiss).setOnClickListener {
            router.popCurrentController()
        }

        return view
    }

}