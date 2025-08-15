package net.gf.mopolskie.endpoints

import net.gf.mopolskie.R

object SegregacjaEndpoints {
    val SEGREGACJA_LAYOUTS = mapOf(
        "black" to R.layout.activity_segregacja_black,
        "brown" to R.layout.activity_segregacja_brown,
        "blue" to R.layout.activity_segregacja_blue,
        "yellow" to R.layout.activity_segregacja_yellow,
        "green" to R.layout.activity_segregacja_green,
        "purple" to R.layout.activity_segregacja_purple,
        "pink" to R.layout.activity_segregacja_pink
    )

    fun getSegregacjaLayout(color: String): Int? {
        return SEGREGACJA_LAYOUTS[color]
    }

    fun isValidColor(color: String): Boolean {
        return SEGREGACJA_LAYOUTS.containsKey(color)
    }

    fun getAllColors(): Set<String> {
        return SEGREGACJA_LAYOUTS.keys
    }
}
