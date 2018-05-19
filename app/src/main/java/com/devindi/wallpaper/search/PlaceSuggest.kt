package com.devindi.wallpaper.search

data class PlaceSuggest(val query: String, val title: String, val description: String, val id: String?)

data class Place(val lat: Double, val lon: Double)
