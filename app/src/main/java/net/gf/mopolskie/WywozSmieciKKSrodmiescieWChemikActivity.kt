package net.gf.mopolskie

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WywozSmieciKKSrodmiescieWChemikActivity : ComponentActivity() {
    private lateinit var viewModel: Waste14ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.waste_kedzierzyn_kozle_srodmiescie_w_chemik)
        viewModel = ViewModelProvider(this)[Waste14ViewModel::class.java]

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
            Toast.makeText(this@WywozSmieciKKSrodmiescieWChemikActivity, "Trwa parsowanie danych...", Toast.LENGTH_SHORT).show()
            val success = viewModel.fetchWaste()
            if (success) {
                showWaste(viewModel.items)
            } else {
                Toast.makeText(this@WywozSmieciKKSrodmiescieWChemikActivity, "Nie udało się pobrać danych", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showWaste(items: List<WasteItem>) {
        val container = findViewById<LinearLayout>(R.id.waste_container)
        val todayBlock = findViewById<LinearLayout>(R.id.today_block)
        val todayDateText = findViewById<TextView>(R.id.date_time)
        val todayWasteText = findViewById<TextView>(R.id.waste_type)
        val nextDateText = findViewById<TextView>(R.id.date_time_next)
        val nextWasteText = findViewById<TextView>(R.id.waste_type_next)
        val calendarText = findViewById<TextView>(R.id.text_calendar)

        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val displayDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val dayOfWeekFormat = SimpleDateFormat("EEEE", Locale.getDefault())

        val todayWasteTypes = mutableListOf<String>()
        val addedDates = mutableSetOf<String>()
        val nextDates = mutableListOf<WasteItem>()

        val todayDateParsed = dateFormat.parse(today)

        container.removeAllViews()

        items.forEach { item ->
            val itemDate = dateFormat.parse(item.date) ?: return@forEach

            if (item.date == today) {
                todayWasteTypes.add(item.wasteType)
                todayDateText.text = "Dzisiaj, ${displayDateFormat.format(itemDate)}"
            } else if (itemDate.after(todayDateParsed)) {
                nextDates.add(item)
            }

            if (!addedDates.contains(item.date)) {
                var dayOfWeek = dayOfWeekFormat.format(itemDate)
                dayOfWeek = dayOfWeek.replaceFirstChar { it.uppercase() }
                val fullDateText = "$dayOfWeek, ${displayDateFormat.format(itemDate)}"

                val dateHeaderView = LayoutInflater.from(this).inflate(R.layout.date_header_item, container, false)
                val dateTextView = dateHeaderView.findViewById<TextView>(R.id.date_text)
                dateTextView.text = fullDateText

                if (item.date == today) {
                    dateTextView.setBackgroundColor(Color.YELLOW)
                }

                container.addView(dateHeaderView)
                addedDates.add(item.date)
            }

            val wasteView = LayoutInflater.from(this).inflate(R.layout.waste_item, container, false)
            wasteView.findViewById<TextView>(R.id.waste_type).text = item.wasteType
            val bg = Color.parseColor(rgbToHex(item.backgroundColor))

            when (bg) {
                Color.parseColor("#545353") -> wasteView.setBackgroundResource(R.drawable.corner_popiol)
                Color.parseColor("#000000") -> wasteView.setBackgroundResource(R.drawable.corner_zmieszane)
                Color.parseColor("#3c5f34") -> wasteView.setBackgroundResource(R.drawable.corner_szklo)
                Color.parseColor("#f0901f") -> wasteView.setBackgroundResource(R.drawable.corner_tworzywa_sztuczne)
                Color.parseColor("#5a2f2a") -> wasteView.setBackgroundResource(R.drawable.corner_bio)
                Color.parseColor("#024b8c") -> wasteView.setBackgroundResource(R.drawable.corner_papier)
                else -> wasteView.setBackgroundResource(R.drawable.default_background)
            }

            container.addView(wasteView)
        }

        if (todayWasteTypes.isNotEmpty()) {
            todayWasteText.text = todayWasteTypes.joinToString(", ")
        } else {
            val todayFormatted = displayDateFormat.format(Date())
            todayDateText.text = "Dzisiaj, $todayFormatted"
            todayWasteText.text = "Dzisiaj nic nie jest do wywiezienia"
        }

        if (nextDates.isNotEmpty()) {
            val sorted = nextDates.sortedBy {
                dateFormat.parse(it.date)
            }

            val nearestDate = sorted.first().date
            val nearestWasteTypes = sorted.filter { it.date == nearestDate }.map { it.wasteType }

            val nextDateParsed = dateFormat.parse(nearestDate)!!
            val nextDateFormatted = displayDateFormat.format(nextDateParsed)
            val dayOfWeek = dayOfWeekFormat.format(nextDateParsed).replaceFirstChar { it.uppercase() }

            nextDateText.text = "$dayOfWeek, $nextDateFormatted"
            nextWasteText.text = nearestWasteTypes.joinToString(", ")
        } else {
            nextDateText.text = "Brak danych"
            nextWasteText.text = "-"
        }

        todayBlock.visibility = LinearLayout.VISIBLE
        calendarText.visibility = LinearLayout.VISIBLE
    }
}

class Waste14ViewModel : ViewModel() {
    val items = mutableListOf<WasteItem>()

    suspend fun fetchWaste(): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = WasteApi14Service.create().getWasteSchedule()
            if (response.isSuccessful) {
                items.clear()
                items.addAll(response.body()?.data ?: emptyList())
                true
            } else false
        } catch (e: Exception) {
            false
        }
    }
}

interface WasteApi14Service {
    @GET("/__api/mopolskie/wywoz/454")
    suspend fun getWasteSchedule(): Response<WasteResponse>

    companion object {
        private const val BASE_URL = "https://api.goflux.pl"

        fun create(): WasteApi14Service {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WasteApi14Service::class.java)
        }
    }
}

