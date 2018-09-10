MapWall
=======

[![Build Status](https://travis-ci.org/devindi/mapwall.svg?branch=master)](https://travis-ci.org/devindi/mapwall)
[![GitHub license](https://img.shields.io/github/license/devindi/mapwall.svg)](https://github.com/devindi/mapwall/blob/master/LICENSE)

Android application to create wallpaper from map. Inspired by [Background Generator by Alvar Carto](https://alvarcarto.com/phone-background/). Application based on OpenStreetMap so any tile-based map source may be used to create wallpaper. Currently Mapwall works with some styles from [Mapbox](https://www.mapbox.com/), [Thunderforest](https://www.thunderforest.com/), [Stamen](http://maps.stamen.com/)


<img height="500" src="https://github.com/devindi/mapwall/blob/master/files/screenshots/lockscreen-italy.png" />  <img height="500" src="https://github.com/devindi/mapwall/blob/master/files/screenshots/screen-gibraltar.png" />

<a href="https://play.google.com/store/apps/details?id=com.devindi.wallpaper">
  <img height="50" alt="Get it on Google Play"
      src="https://play.google.com/intl/en_us/badges/images/apps/en-play-badge.png" /></a>

Getting started
---------------

1. Make sure you've installed [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) and [Android Studio](https://developer.android.com/studio/index.html), a _Standard Setup_ would work.
2. Clone this GitHub repository.
3. In Android Studio open the project from the local repository as a **Gradle project** (this will auto-generate `local.properties` with the SDK location).
4. Make sure you have an emulation device setup in AVD Manager (_Tools → Android → AVD Manager_).
5. Run.

Notes:

* To enable error reporting/analytics to have to provide [fabric key](https://docs.fabric.io/android/fabric/settings/api-keys.html). Place it at `local.properties` with key `fabricKey`
* To enable google location search you have to provide google api key. Place it at `local.properties` with key `googleKey`
* To enable maps from Thunderforest you have to provide [API key](https://www.thunderforest.com/docs/apikeys/). Place it at `local.properties` with key `thunderforest`

Contributing
------------

Feel free to help this project. We have a list of [open issues](https://github.com/devindi/mapwall/issues) and a lack of tests.

License
-------

Project is licensed under [Apache License 2.0](https://github.com/devindi/mapwall/blob/master/LICENSE)
