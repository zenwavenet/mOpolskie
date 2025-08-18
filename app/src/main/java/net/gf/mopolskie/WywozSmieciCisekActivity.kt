package net.gf.mopolskie

import net.gf.mopolskie.utils.overrideTransitionCompat
import net.gf.mopolskie.utils.setupModernStatusBar
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.view.View
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import net.gf.mopolskie.endpoints.WasteEndpoints
import net.gf.mopolskie.manager.WasteItem
import net.gf.mopolskie.manager.WasteManager
import net.gf.mopolskie.manager.WasteUtils
import net.gf.mopolskie.manager.TrashManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WywozSmieciCisekActivity : ComponentActivity() {
    private lateinit var wasteManager: WasteManager
    private lateinit var trashManager: TrashManager
    private lateinit var sharedPreferences: SharedPreferences
    private var selectedRegion: String? = null
    private var isFavorite: Boolean = false
    private var currentEndpointId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        wasteManager = WasteManager()
        trashManager = TrashManager(this)
        sharedPreferences = getSharedPreferences("FavoriteRegions", MODE_PRIVATE)
        selectedRegion = intent.getStringExtra("region")
        
        if (selectedRegion != null) {
            showRegionData(selectedRegion!!)
        } else {
            showRegionMenu()
        }
    }

    private fun showRegionMenu() {
        setContentView(R.layout.activity_waste_cisek)
        setupStatusBar()
        setupCommonNavigation()
        setupRegionButtons()
    }

    private fun showRegionData(region: String) {
        val regionConfig = getRegionConfig(region)
        currentEndpointId = regionConfig.endpointId
        setContentView(regionConfig.layoutId)
        setupStatusBar()
        setupCommonNavigation()
        setupFavoriteButton(region)
        loadWasteData(regionConfig.endpointId)
    }

    private fun setupStatusBar() {
        setupModernStatusBar()
    }

    private fun setupCommonNavigation() {
        findViewById<LinearLayout>(R.id.help)?.setOnClickListener {
            startActivityWithTransition(HelpActivity::class.java)
        }

        findViewById<LinearLayout>(R.id.pulpit)?.setOnClickListener {
            startActivityWithTransition(MainActivity::class.java)
        }

        findViewById<LinearLayout>(R.id.services)?.setOnClickListener {
            startActivityWithTransition(ServicesActivity::class.java)
        }

        findViewById<LinearLayout>(R.id.more)?.setOnClickListener {
            startActivityWithTransition(MoreActivity::class.java)
        }
    }

    private fun setupRegionButtons() {
        setupRegionButton(R.id.bycznicaw, "bycznica_w")
        setupRegionButton(R.id.bycznica, "bycznica")
        setupRegionButton(R.id.bycznicaf, "bycznica_f")
        setupRegionButton(R.id.cisek, "kobylice")
        setupRegionButton(R.id.cisekf, "kobylice_f")
        setupRegionButton(R.id.cisekw, "kobylice_w")
    }

    private fun setupRegionButton(buttonId: Int, region: String) {
        findViewById<LinearLayout>(buttonId)?.setOnClickListener {
            val intent = Intent(this, WywozSmieciCisekActivity::class.java)
            intent.putExtra("region", region)
            startActivity(intent)
            finish()
            overrideTransitionCompat()
        }
    }

    private fun loadWasteData(endpointId: Int) {
        lifecycleScope.launch {
            val success = wasteManager.fetchWaste(endpointId)
            if (success) {
                showWaste(wasteManager.getWasteItems())
            } else {
                Toast.makeText(this@WywozSmieciCisekActivity, "Nie udało się pobrać danych.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showWaste(items: List<WasteItem>) {
        val container = findViewById<LinearLayout>(R.id.waste_container) ?: return
        val todayBlock = findViewById<LinearLayout>(R.id.today_block) ?: return
        val todayDateText = findViewById<TextView>(R.id.date_time) ?: return
        val todayWasteText = findViewById<TextView>(R.id.waste_type) ?: return
        val nextDateText = findViewById<TextView>(R.id.date_time_next) ?: return
        val nextWasteText = findViewById<TextView>(R.id.waste_type_next) ?: return
        val calendarText = findViewById<TextView>(R.id.text_calendar) ?: return
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

            val bg = Color.parseColor(WasteUtils.rgbToHex(item.backgroundColor))

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

    private fun startActivityWithTransition(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)

        startActivity(intent)
        finish()
        overrideTransitionCompat()
    }

    private fun getRegionConfig(region: String): RegionConfig {
        return when (region) {
            "bycznica_w" -> RegionConfig(R.layout.waste_cisek_bycznica_w, WasteEndpoints.CISEK_BYCZNICA_W)
            "bycznica" -> RegionConfig(R.layout.waste_cisek_bycznica, WasteEndpoints.CISEK_BYCZNICA)
            "bycznica_f" -> RegionConfig(R.layout.waste_cisek_bycznica_f, WasteEndpoints.CISEK_BYCZNICA_F)
            "kobylice" -> RegionConfig(R.layout.waste_cisek_kobylice, WasteEndpoints.CISEK_KOBYLICE)
            "kobylice_w" -> RegionConfig(R.layout.waste_cisek_kobylice_w, WasteEndpoints.CISEK_KOBYLICE_W)
            "kobylice_f" -> RegionConfig(R.layout.waste_cisek_kobylice_f, WasteEndpoints.CISEK_KOBYLICE_F)
            else -> RegionConfig(R.layout.waste_cisek_kobylice, WasteEndpoints.CISEK_KOBYLICE)
        }
    }

    private fun setupFavoriteButton(region: String) {
        val favoriteButton = findViewById<LinearLayout>(R.id.favoriteButton)
        val favoriteIcon = findViewById<View>(R.id.favoriteIcon)
        val favoriteText = findViewById<TextView>(R.id.favoriteText)
        
        if (favoriteButton != null && favoriteIcon != null && favoriteText != null) {

            val currentSelectedEndpoint = trashManager.getSelectedEndpointId()
            isFavorite = (currentSelectedEndpoint == currentEndpointId)

            updateFavoriteUI(favoriteIcon, favoriteText)
            
            favoriteButton.setOnClickListener {
                if (isFavorite) {
                    Toast.makeText(this, "Ta gmina jest już ulubiona", Toast.LENGTH_SHORT).show()
                } else {
                    trashManager.setSelectedEndpoint(currentEndpointId)
                    isFavorite = true
                    updateFavoriteUI(favoriteIcon, favoriteText)
                    
                    val municipalityName = trashManager.getSelectedMunicipalityName()
                    Toast.makeText(this, "Ulubiona gmina: $municipalityName", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            println("WywozSmieciCisekActivity: Nie znaleziono elementów UI dla favoriteButton")
        }
    }

    private fun updateFavoriteUI(favoriteIcon: View, favoriteText: TextView) {
        if (isFavorite) {
            favoriteIcon.setBackgroundResource(R.drawable.ic_heart_filled)
            favoriteIcon.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#FF4444"))
            favoriteText.text = "Ulubione"
        } else {
            favoriteIcon.setBackgroundResource(R.drawable.ic_heart_outline)
            favoriteIcon.backgroundTintList = android.content.res.ColorStateList.valueOf(Color.parseColor("#999999"))
            favoriteText.text = "Polub"
        }
    }

    data class RegionConfig(
        val layoutId: Int,
        val endpointId: Int
    )
}
