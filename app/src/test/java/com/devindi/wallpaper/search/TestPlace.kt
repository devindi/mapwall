package com.devindi.wallpaper.search


import android.net.Uri
import com.google.android.gms.location.places.Place
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import java.util.*

/**
 * author   : Eugene Dudnik
 * date     : 10/11/18
 * e-mail   : esdudnik@gmail.com
 */
class TestPlace constructor(private val latLngVariable: LatLng): Place{
    override fun isDataValid(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRating(): Float {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPriceLevel(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getName(): CharSequence {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getId(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLocale(): Locale {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getWebsiteUri(): Uri? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun freeze(): Place {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAttributions(): CharSequence? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPlaceTypes(): MutableList<Int> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAddress(): CharSequence? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPhoneNumber(): CharSequence? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getViewport(): LatLngBounds? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getLatLng(): LatLng {
        return latLngVariable
    }

}
