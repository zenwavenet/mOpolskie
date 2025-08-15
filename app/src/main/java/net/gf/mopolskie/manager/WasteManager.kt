package net.gf.mopolskie.manager

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class WasteManager {
    private val items = mutableListOf<WasteItem>()
    private val apiService = WasteApiService.create()

    suspend fun fetchWaste(endpointId: Int): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = apiService.getWasteSchedule(endpointId)
            if (response.isSuccessful) {
                items.clear()
                items.addAll(response.body()?.data ?: emptyList())
                true
            } else false
        } catch (e: Exception) {
            false
        }
    }

    fun getWasteItems(): List<WasteItem> = items.toList()
}

interface WasteApiService {
    @GET("/__api/mopolskie/wywoz/{endpointId}")
    suspend fun getWasteSchedule(@retrofit2.http.Path("endpointId") endpointId: Int): Response<WasteResponse>

    companion object {
        private const val BASE_URL = "https://api.stackflow.pl"

        fun create(): WasteApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WasteApiService::class.java)
        }
    }
}

data class WasteResponse(
    val success: Boolean,
    val data: List<WasteItem>,
    val count: Int,
    val url: String
)

data class WasteItem(
    val date: String,
    val wasteType: String,
    val backgroundColor: String,
    val borderColor: String,
    val rawTitle: String
)

object WasteUtils {
    fun rgbToHex(rgb: String): String {
        return try {
            val values = rgb.removePrefix("rgb(").removeSuffix(")").split(",")
            val r = values[0].trim().toInt()
            val g = values[1].trim().toInt()
            val b = values[2].trim().toInt()
            String.format("#%02x%02x%02x", r, g, b)
        } catch (e: Exception) {
            "#cccccc"
        }
    }
}
