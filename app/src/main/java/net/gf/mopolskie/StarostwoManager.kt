package net.gf.mopolskie

import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class StarostwoManager : ViewModel() {
    val locations = mutableListOf<StarostwoLocation>()
    
    suspend fun fetchStarostwoData(starostwo: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val endpoint = StarostwoEndpoints.getStarostwoEndpoint(starostwo) ?: return@withContext false
                val responseKey = StarostwoEndpoints.getStarostwoResponseKey(starostwo) ?: return@withContext false
                
                val response = StarostwoApiService.create().getStarostwo(endpoint)
                if (response.isSuccessful) {
                    val responseBody = response.body() ?: return@withContext false
                    val map = extractDataFromResponse(responseBody, responseKey) ?: return@withContext false
                    
                    locations.clear()
                    for ((_, obj) in map) {
                        val json = StarostwoApiService.gson.toJsonTree(obj).asJsonObject
                        val location = StarostwoApiService.gson.fromJson(json, StarostwoLocation::class.java)
                        locations.add(location)
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
    
    private fun extractDataFromResponse(responseBody: Any, responseKey: String): Map<String, Any>? {
        return try {
            val json = StarostwoApiService.gson.toJsonTree(responseBody).asJsonObject
            val dataObject = json.get(responseKey)?.asJsonObject ?: return null
            
            val result = mutableMapOf<String, Any>()
            for ((key, value) in dataObject.entrySet()) {
                result[key] = StarostwoApiService.gson.fromJson(value, Any::class.java)
            }
            result
        } catch (e: Exception) {
            null
        }
    }
}

interface StarostwoApiService {
    @GET("/__api/mopolskie/starostwo/{endpoint}")
    suspend fun getStarostwo(@Path("endpoint") endpoint: Int): Response<Any>
    
    companion object {
        private const val BASE_URL = "https://api.stackflow.pl"
        val gson = GsonBuilder().create()
        
        fun create(): StarostwoApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(StarostwoApiService::class.java)
        }
    }
}

data class StarostwoLocation(
    val name: String,
    val branch: String,
    val city: String,
    val address: String,
    val phone: String?,
    val email: String?,
    val open: Map<String, String>?,
    val departments: Map<String, List<String>>?
)
