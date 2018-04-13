package com.devindi.wallpaper.misc

import android.arch.lifecycle.ViewModel
import com.bluelinelabs.conductor.Controller
import org.koin.KoinContext
import org.koin.core.parameter.Parameters
import org.koin.standalone.StandAloneContext

/**
 * inject lazily given dependency for Controller
 * @param name - bean name / optional
 */
inline fun <reified T> Controller.inject(
        name: String = "",
        noinline parameters: Parameters = { emptyMap() }
): Lazy<T> = lazy { get<T>(name, parameters) }

/**
 * get given dependency for Controller
 * @param name - bean name / optional
 */
inline fun <reified T> Controller.get(
        name: String = "",
        noinline parameters: Parameters = { emptyMap() }
): T = (StandAloneContext.koinContext as KoinContext).get<T>(name, parameters)

/**
 * get given dependency for ViewModel
 * @param name - bean name / optional
 */
inline fun <reified T> ViewModel.get(
        name: String = "",
        noinline parameters: Parameters = { emptyMap() }
): T = (StandAloneContext.koinContext as KoinContext).get<T>(name, parameters)
