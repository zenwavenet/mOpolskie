package net.gf.mopolskie

object AttractionEndpoints {
    val ATTRACTION_ENDPOINTS = mapOf(
        "castle" to 1,
        "museum" to 2, 
        "nature" to 3,
        "other" to 4
    )
 
    val ATTRACTION_LAYOUTS = mapOf(
        "castle" to R.layout.activity_attraction,
        "museum" to R.layout.activity_attraction,
        "nature" to R.layout.activity_attraction,
        "other" to R.layout.activity_attraction
    )

    fun getAttractionEndpoint(category: String): Int? {
        return ATTRACTION_ENDPOINTS[category]
    }

    fun getAttractionLayout(category: String): Int? {
        return ATTRACTION_LAYOUTS[category]
    }

    fun isValidCategory(category: String): Boolean {
        return ATTRACTION_ENDPOINTS.containsKey(category)
    }

    fun getAllCategories(): Set<String> {
        return ATTRACTION_ENDPOINTS.keys
    }
}
