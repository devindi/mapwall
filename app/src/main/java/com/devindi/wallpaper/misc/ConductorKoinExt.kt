@file:SuppressWarnings("detekt-kotlin:MatchingDeclarationName")

package com.devindi.wallpaper.misc

import com.bluelinelabs.conductor.Controller
import org.koin.core.KoinContext
import org.koin.core.parameter.ParameterDefinition
import org.koin.core.parameter.emptyParameterDefinition
import org.koin.core.scope.Scope
import org.koin.standalone.StandAloneContext

/**
 *
 *
 *
 */

/**
 * inject lazily given dependency for conductor's controller
 * @param name - bean canonicalName
 * @param parameters - injection parameters
 */
inline fun <reified T : Any> Controller.inject(
    name: String = "",
    scope: Scope? = null,
    noinline parameters: ParameterDefinition = emptyParameterDefinition()
) = lazy { getKoin().get<T>(name, scope, parameters) }


/**
 * Access to Koin context
 */
fun Controller.getKoin(): KoinContext = (StandAloneContext.koinContext as KoinContext)