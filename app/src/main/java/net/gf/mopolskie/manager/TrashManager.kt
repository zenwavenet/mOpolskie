package net.gf.mopolskie.manager

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class TrashManager(context: Context) {
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences("trash_preferences", Context.MODE_PRIVATE)
    
    private val wasteManager = WasteManager()
    
    companion object {
        private const val SELECTED_WASTE_ENDPOINT_ID = "selected_waste_endpoint_id"
        private const val SELECTED_WASTE_ENDPOINT_NAME = "selected_waste_endpoint_name"
    }

    private val endpointToMunicipalityMap = mapOf(
        364 to "Cisek - Kobylice\n(jednorodzinne)",
        362 to "Cisek - Kobylice\n(wielorodzinne)",
        462 to "Cisek - Kobylice\n(firmy)",
        363 to "Cisek - Bycznica\n(jednorodzinne)",
        361 to "Cisek - Bycznica\n(wielorodzinne)", 
        463 to "Cisek - Bycznica\n(firmy)",

        443 to "Kędzierzyn-Koźle - Kuźniczka\n(jednorodzinne)",
        449 to "Kędzierzyn-Koźle - Azoty\n(jednorodzinne)",
        453 to "Kędzierzyn-Koźle - Azoty\n(wielorodzinne)",
        448 to "Kędzierzyn-Koźle - Blachownia\n(jednorodzinne)",
        450 to "Kędzierzyn-Koźle - Cisowa\n(jednorodzinne)",
        444 to "Kędzierzyn-Koźle - Koźle\n(jednorodzinne)",
        456 to "Kędzierzyn-Koźle - Koźle\n(wielorodzinne)",
        447 to "Kędzierzyn-Koźle - Lenartowice\n(jednorodzinne)",
        442 to "Kędzierzyn-Koźle - Pogorzelec\n(jednorodzinne)",
        455 to "Kędzierzyn-Koźle - Pogorzelec\n(wielorodzinne)",
        445 to "Kędzierzyn-Koźle - Sławięcice\n(jednorodzinne)",
        446 to "Kędzierzyn-Koźle - Śródmieście\n(jednorodzinne)",
        452 to "Kędzierzyn-Koźle - Grunwaldzka\n(wielorodzinne)",
        451 to "Kędzierzyn-Koźle - Waryńskiego/Piłsudskiego\n(wielorodzinne)",
        454 to "Kędzierzyn-Koźle - Chemik\n(wielorodzinne)",
        499 to "Kędzierzyn-Koźle\n(firmy)"
    )
    
    fun getSelectedEndpointId(): Int? {
        val endpointId = sharedPreferences.getInt(SELECTED_WASTE_ENDPOINT_ID, -1)
        return if (endpointId != -1) endpointId else null
    }
    
    fun getSelectedMunicipalityName(): String? {
        val endpointId = getSelectedEndpointId()
        return if (endpointId != null) {
            endpointToMunicipalityMap[endpointId]
        } else {
            null
        }
    }
    
    fun isCisekMunicipality(): Boolean {
        val endpointId = getSelectedEndpointId()
        return endpointId != null && endpointId in listOf(364, 362, 462, 363, 361, 463)
    }
    
    fun setSelectedEndpoint(endpointId: Int) {
        val municipalityName = endpointToMunicipalityMap[endpointId]
        
        if (municipalityName != null) {
            sharedPreferences.edit().clear().apply()

            sharedPreferences.edit().apply {
                putInt(SELECTED_WASTE_ENDPOINT_ID, endpointId)
                putString(SELECTED_WASTE_ENDPOINT_NAME, municipalityName)
                putBoolean("migration_done", true)
                apply()
            }

            val saved = getSelectedEndpointId()
            val savedName = getSelectedMunicipalityName()
        } else {
            println("TrashManager: BŁĄD - nie znaleziono nazwy dla endpoint $endpointId")
        }
    }
    
    fun getAllEndpoints(): List<Pair<Int, String>> {
        return endpointToMunicipalityMap.toList().sortedBy { it.second }
    }
    
    fun setMigrationDone() {
        sharedPreferences.edit().putBoolean("migration_done", true).apply()
    }
    
    fun isMigrationDone(): Boolean {
        return sharedPreferences.getBoolean("migration_done", false)
    }
    
    fun resetAllFavorites() {
        sharedPreferences.edit().clear().apply()
    }
    
    fun resetMigrationFlag() {
        sharedPreferences.edit().remove("migration_done").apply()
    }
    
    fun forceFullReset() {
        sharedPreferences.edit().clear().apply()
    }

    suspend fun getTodayAndTomorrowWaste(): Pair<String, String> = withContext(Dispatchers.IO) {
        try {
            val endpointId = getSelectedEndpointId()
            if (endpointId != null) {
                val success = wasteManager.fetchWaste(endpointId)
                if (success) {
                    val items = wasteManager.getWasteItems()

                    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    val tomorrow = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                        Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)
                    )
                    
                    val todayWasteTypes = mutableListOf<String>()
                    val tomorrowWasteTypes = mutableListOf<String>()
                    
                    items.forEach { item ->
                        when (item.date) {
                            today -> todayWasteTypes.add(item.wasteType)
                            tomorrow -> tomorrowWasteTypes.add(item.wasteType)
                        }
                    }
                    
                    val todayText = if (todayWasteTypes.isNotEmpty()) {
                        todayWasteTypes.joinToString(", ")
                    } else {
                        "Brak wywozu"
                    }
                    
                    val tomorrowText = if (tomorrowWasteTypes.isNotEmpty()) {
                        tomorrowWasteTypes.joinToString(", ")
                    } else {
                        "Brak wywozu"
                    }
                    
                    Pair(todayText, tomorrowText)
                } else {
                    Pair("Błąd pobierania", "Błąd pobierania")
                }
            } else {
                Pair("Wybierz gminę", "Wybierz gminę")
            }
        } catch (e: Exception) {
            Pair("Błąd: ${e.message}", "Błąd: ${e.message}")
        }
    }
}
