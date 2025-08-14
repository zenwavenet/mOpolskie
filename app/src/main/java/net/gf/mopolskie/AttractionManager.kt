package net.gf.mopolskie

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class AttractionManager : ViewModel() {
    val attractionCategories = mutableMapOf<String, List<Attraction>>()
    
    suspend fun fetchAttractionData(category: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val endpoint = AttractionEndpoints.getAttractionEndpoint(category) ?: return@withContext false
                
                val response = AttractionApiService.create().getAttractions(endpoint)
                if (response.isSuccessful) {
                    response.body()?.let { attractionResponse ->
                        attractionCategories.clear()
                        attractionResponse.forEach { (categoryName, attractionMap) ->
                            attractionCategories[categoryName] = attractionMap.values.toList()
                        }
                    }
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                false
            }
        }
    }
}

interface AttractionApiService {
    @GET("/__api/mopolskie/atrakcje/{endpoint}")
    suspend fun getAttractions(@Path("endpoint") endpoint: Int): Response<Map<String, Map<String, Attraction>>>
    
    companion object {
        private const val BASE_URL = "https://api.stackflow.pl"
        
        fun create(): AttractionApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AttractionApiService::class.java)
        }
    }
}

data class Attraction(
    val name: String,
    val address: String,
    val description: String,
    val url: String,
    val map: String
)
