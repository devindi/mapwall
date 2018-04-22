@file:SuppressWarnings("detekt-kotlin:MatchingDeclarationName")
package com.devindi.wallpaper.misc

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelStores
import android.support.v4.app.FragmentActivity
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.archlifecycle.LifecycleController
import org.koin.Koin
import org.koin.KoinContext
import org.koin.android.architecture.ext.get
import org.koin.android.architecture.ext.getByName
import org.koin.core.parameter.Parameters
import org.koin.standalone.KoinComponent
import org.koin.standalone.StandAloneContext
import kotlin.reflect.KClass

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
 * Koin ViewModel factory
 */
object CustomKoinFactory : ViewModelProvider.Factory, KoinComponent {

    /**
     * Current Parameters
     */
    internal var parameters: Parameters = { emptyMap() }

    /**
     * Current BeanDefinition name
     */
    internal var name: String? = null

    /**
     * Create instance for ViewModelProvider Factory
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val beanName = name
        return if (beanName != null) {
            getByName(beanName, parameters)
        } else get(modelClass, parameters)
    }
}

/**
 * Lazy get a viewModel instance
 *
 * @param key - ViewModel Factory key (if have several instances from same ViewModel)
 * @param name - Koin BeanDefinition name (if have several ViewModel definition of the same type)
 * @param parameters - parameters to pass to the BeanDefinition
 */
inline fun <reified T : ViewModel> LifecycleController.viewModel(
        key: String? = null,
        name: String? = null,
        noinline parameters: Parameters = { emptyMap() }
): Lazy<T> {
    return viewModelByClass(T::class, key, name, parameters)
}

/**
 * Lazy get a viewModel instance
 *
 * @param clazz - Class of the BeanDefinition to retrieve
 * @param key - ViewModel Factory key (if have several instances from same ViewModel)
 * @param name - Koin BeanDefinition name (if have several ViewModel definition of the same type)
 * @param parameters - parameters to pass to the BeanDefinition
 */
fun <T : ViewModel> LifecycleController.viewModelByClass(
        clazz: KClass<T>,
        key: String? = null,
        name: String? = null,
        parameters: Parameters = { emptyMap() }
): Lazy<T> {
    return lazy { getViewModelByClass(clazz, key, name, parameters) }
}

/**
 * Get a viewModel instance
 *
 * @param fromActivity - create it from Activity (default false) - not used if on Activity
 * @param clazz - Class of the BeanDefinition to retrieve
 * @param key - ViewModel Factory key (if have several instances from same ViewModel)
 * @param name - Koin BeanDefinition name (if have several ViewModel definition of the same type)
 * @param parameters - parameters to pass to the BeanDefinition
 */
fun <T : ViewModel> LifecycleController.getViewModelByClass(
        clazz: KClass<T>,
        key: String? = null,
        name: String? = null,
        parameters: Parameters = { emptyMap() }
): T {
    CustomKoinFactory.apply {
        this.parameters = parameters
        this.name = name
    }
    Koin.logger.log("[ViewModel] get for Controller @ $this")
    val viewModelProvider = ViewModelProvider(ViewModelStores.of(this.activity as FragmentActivity), CustomKoinFactory)
    return if (key != null) viewModelProvider.get(
            key,
            clazz.java
    ) else viewModelProvider.get(clazz.java)
}

/**
 * get given dependency for ViewModel
 * @param name - bean name / optional
 */
inline fun <reified T> ViewModel.get(
        name: String = "",
        noinline parameters: Parameters = { emptyMap() }
): T = (StandAloneContext.koinContext as KoinContext).get<T>(name, parameters)
