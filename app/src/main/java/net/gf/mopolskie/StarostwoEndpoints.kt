package net.gf.mopolskie

object StarostwoEndpoints {
    val STAROSTWO_ENDPOINTS = mapOf(
        "brzeg" to 1,
        "glubczyce" to 2,
        "kedzierzynsko_kozielski" to 3,
        "kluczborski" to 4,
        "krapkowicki" to 5,
        "namyslowski" to 6,
        "nyski" to 7,
        "oleski" to 8,
        "opolski" to 9,
        "prudnicki" to 10,
        "strzelecki" to 11
    )

    val STAROSTWO_LAYOUTS = mapOf(
        "brzeg" to R.layout.activity_brzeg_starostwo,
        "glubczyce" to R.layout.activity_glubczyce_starostwo,
        "kedzierzynsko_kozielski" to R.layout.activity_kedzierzynsko_kozielski_starostwo,
        "kluczborski" to R.layout.activity_kluczborski_starostwo,
        "krapkowicki" to R.layout.activity_krapkowicki_starostwo,
        "namyslowski" to R.layout.activity_namyslowski_starostwo,
        "nyski" to R.layout.activity_nyski_starostwo,
        "oleski" to R.layout.activity_oleski_starostwo,
        "opolski" to R.layout.activity_opolski_starostwo,
        "prudnicki" to R.layout.activity_prudnicki_starostwo,
        "strzelecki" to R.layout.activity_strzelecki_starostwo
    )

    val STAROSTWO_RESPONSE_KEYS = mapOf(
        "brzeg" to "Brzeski",
        "glubczyce" to "Głubczycki",
        "kedzierzynsko_kozielski" to "KedzierzynskoKozielski",
        "kluczborski" to "Kluczborski",
        "krapkowicki" to "Krapkowicki",
        "namyslowski" to "Namyslowski",
        "nyski" to "Nyski",
        "oleski" to "Oleski",
        "opolski" to "Opolski",
        "prudnicki" to "Prudnicki",
        "strzelecki" to "Strzelecki"
    )

    fun getStarostwoEndpoint(starostwo: String): Int? {
        return STAROSTWO_ENDPOINTS[starostwo]
    }

    fun getStarostwoLayout(starostwo: String): Int? {
        return STAROSTWO_LAYOUTS[starostwo]
    }

    fun getStarostwoResponseKey(starostwo: String): String? {
        return STAROSTWO_RESPONSE_KEYS[starostwo]
    }

    fun isValidStarostwo(starostwo: String): Boolean {
        return STAROSTWO_ENDPOINTS.containsKey(starostwo)
    }

    fun getAllStarostwa(): Set<String> {
        return STAROSTWO_ENDPOINTS.keys
    }
}
