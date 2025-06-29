package net.gf.mopolskie

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class AttractionMuseumActivity : ComponentActivity() {
    private lateinit var viewModel: AttractionView2Model

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attraction)
        viewModel = ViewModelProvider(this)[AttractionView2Model::class.java]

        window.statusBarColor = resources.getColor(R.color.status_bar_color, theme)
        window.decorView.systemUiVisibility = 0

        val HelpButton = findViewById<LinearLayout>(R.id.help)
        HelpButton.setOnClickListener {
            val intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val MainButton = findViewById<LinearLayout>(R.id.pulpit)
        MainButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val ServicesButton = findViewById<LinearLayout>(R.id.services)
        ServicesButton.setOnClickListener {
            val intent = Intent(this, ServicesActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val MoreButton = findViewById<LinearLayout>(R.id.more)
        MoreButton.setOnClickListener {
            val intent = Intent(this, MoreActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        lifecycleScope.launch {
            val success = viewModel.fetchAttractions()
            if (success) {
                displayAttractionsWithCategories(viewModel.attractionCategories)
            } else {
                Toast.makeText(this@AttractionMuseumActivity, "Błąd podczas pobierania danych.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun displayAttractionsWithCategories(categories: Map<String, List<Attraction>>) {
        val attractionContainer = findViewById<LinearLayout>(R.id.attraction_container)

        if (attractionContainer == null) {
            println("Nie znaleziono widoku attraction_container!")
            return
        }

        categories.forEach { (categoryName, attractions) ->
            val categoryHeader = LayoutInflater.from(this).inflate(R.layout.category_header, null)
            val categoryTitle: TextView = categoryHeader.findViewById(R.id.category_title)
            categoryTitle.text = categoryName
            attractionContainer.addView(categoryHeader)

            attractions.forEach { attraction ->
                val attractionView = LayoutInflater.from(this).inflate(R.layout.attraction_item, null)
                val nameView: TextView = attractionView.findViewById(R.id.attraction_name)
                val addressView: TextView = attractionView.findViewById(R.id.attraction_address)
                val descriptionView: TextView = attractionView.findViewById(R.id.attraction_description)

                nameView.text = attraction.name
                addressView.text = attraction.address
                descriptionView.text = attraction.description

                if (attraction.url.isNotEmpty()) {
                    val urlContainer: LinearLayout = attractionView.findViewById(R.id.attraction_url_container)
                    val urlRow = LinearLayout(this).apply {
                        orientation = LinearLayout.VERTICAL
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            bottomMargin = 10
                        }
                        background = getDrawable(R.drawable.corner_phone_help)
                        setPadding(7.dpToPx(), 7.dpToPx(), 7.dpToPx(), 7.dpToPx())
                        gravity = android.view.Gravity.CENTER

                        setOnClickListener {
                            openUrl(attraction.url)
                        }
                    }

                    val urlText = TextView(this).apply {
                        text = "Strona internetowa"
                        setTextColor(resources.getColor(android.R.color.black, theme))
                        textSize = 14f
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        gravity = android.view.Gravity.CENTER
                    }


                    urlRow.addView(urlText)
                    urlContainer.addView(urlRow)
                }

                if (attraction.map.isNotEmpty()) {
                    val mapContainer: LinearLayout = attractionView.findViewById(R.id.attraction_map_container)
                    val mapRow = LinearLayout(this).apply {
                        orientation = LinearLayout.VERTICAL
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            bottomMargin = 8
                        }
                        background = getDrawable(R.drawable.corner_phone_help)
                        setPadding(7.dpToPx(), 7.dpToPx(), 7.dpToPx(), 7.dpToPx())
                        gravity = android.view.Gravity.CENTER

                        setOnClickListener {
                            openUrl(attraction.map)
                        }
                    }

                    val mapText = TextView(this).apply {
                        text = "Lokalizacja na mapie"
                        setTextColor(resources.getColor(android.R.color.black, theme))
                        textSize = 14f
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        gravity = android.view.Gravity.CENTER
                    }


                    mapRow.addView(mapText)
                    mapContainer.addView(mapRow)
                }

                attractionContainer.addView(attractionView)
            }

            if (categoryName != categories.keys.last()) {
                val separator = View(this).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2
                    ).apply {
                        topMargin = 16
                        bottomMargin = 16
                    }
                    setBackgroundColor(resources.getColor(R.color.separator_color, theme))
                }
                attractionContainer.addView(separator)
            }
        }
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Nie można otworzyć linku", Toast.LENGTH_SHORT).show()
        }
    }

    private fun Int.dpToPx(): Int {
        return (this * resources.displayMetrics.density).toInt()
    }
}

class AttractionView2Model : ViewModel() {
    val attractionCategories = mutableMapOf<String, List<Attraction>>()

    suspend fun fetchAttractions(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = AttractionApi2Service.create().getAttractions()
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

interface AttractionApi2Service {
    @GET("/__api/mopolskie/atrakcje/2")
    suspend fun getAttractions(): Response<Map<String, Map<String, Attraction>>>

    companion object {
        private const val BASE_URL = "https://api.stackflow.pl"

        fun create(): AttractionApi2Service {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AttractionApi2Service::class.java)
        }
    }
}