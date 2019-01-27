package com.devindi.wallpaper.model.map

class TilesRange(private val from: Int, to: Int, zoom: Int) : Iterable<Int> {

    val actualTo = if (to < from) to + Math.pow(2.0, zoom.toDouble()).toInt() else to

    override fun iterator(): Iterator<Int> {
        return object : IntIterator() {

            private var hasNext: Boolean = from <= actualTo
            private var next = if (hasNext) from else actualTo

            override fun hasNext(): Boolean = hasNext

            override fun nextInt(): Int {
                val value = next
                if (value == actualTo) {
                    if (!hasNext) throw kotlin.NoSuchElementException()
                    hasNext = false
                } else {
                    next += 1
                }
                return value
            }
        }
    }

    fun size() = actualTo - from
}
