package com.devindi.wallpaper.search

import android.text.style.CharacterStyle
import com.google.android.gms.location.places.AutocompletePrediction

/**
 * author   : Eugene Dudnik
 * date     : 10/11/18
 * e-mail   : esdudnik@gmail.com
 */

class TestAutocompletePrediction constructor(private val placeIdVariable: String, private val primaryTextVariable: CharSequence,
                                             private val secondaryTextVariable: CharSequence) : AutocompletePrediction {

    override fun isDataValid(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun freeze(): AutocompletePrediction {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPrimaryText(p0: CharacterStyle?): CharSequence {
        return primaryTextVariable
    }

    override fun getPlaceId(): String? {
        return placeIdVariable
    }

    override fun getPlaceTypes(): MutableList<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSecondaryText(p0: CharacterStyle?): CharSequence {
        return secondaryTextVariable
    }

    override fun getFullText(p0: CharacterStyle?): CharSequence {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
