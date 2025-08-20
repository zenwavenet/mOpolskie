package net.gf.mopolskie

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.*
import net.gf.mopolskie.utils.BaseActivity
import net.gf.mopolskie.utils.overrideTransitionCompat
import net.gf.mopolskie.utils.setupModernStatusBar
import net.gf.mopolskie.adapters.SliderAdapter
import net.gf.mopolskie.manager.TrashManager
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : BaseActivity() {
    private lateinit var searchEditText: EditText
    private lateinit var viewPager: ViewPager2
    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var indicatorDots: LinearLayout
    private lateinit var trashManager: TrashManager
    private var currentDialog: android.app.AlertDialog? = null
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupModernStatusBar()

        viewPager = findViewById(R.id.viewPager)
        indicatorDots = findViewById(R.id.sliderIndicators)
        trashManager = TrashManager(this)

        setupViewPager()
        setupIndicators()

        checkOldFavorites()

        searchEditText = findViewById(R.id.searchEditText)
        startClock()
        setupSearchFunctionality()

        val HelpButton = findViewById<LinearLayout>(R.id.help)
        HelpButton.setOnClickListener {
            val intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
            finish()
            overrideTransitionCompat()
        }

        val OtherButton = findViewById<LinearLayout>(R.id.other)
        OtherButton.setOnClickListener {
            val intent = Intent(this, ServicesActivity::class.java)
            startActivity(intent)
            finish()
            overrideTransitionCompat()
        }

        val ServicesButton = findViewById<LinearLayout>(R.id.services)
        ServicesButton.setOnClickListener {
            val intent = Intent(this, ServicesActivity::class.java)
            startActivity(intent)
            finish()
            overrideTransitionCompat()
        }

        val MoreButton = findViewById<LinearLayout>(R.id.more)
        MoreButton.setOnClickListener {
            val intent = Intent(this, MoreActivity::class.java)
            startActivity(intent)
            finish()
            overrideTransitionCompat()
        }

        val SegButton = findViewById<LinearLayout>(R.id.segregacja)
        SegButton.setOnClickListener {
            val intent = Intent(this, SegregacjaActivity::class.java)
            startActivity(intent)
            finish()
            overrideTransitionCompat()
        }

        val StaButton = findViewById<LinearLayout>(R.id.starostwa)
        StaButton.setOnClickListener {
            val intent = Intent(this, StarostwoActivity::class.java)
            startActivity(intent)
            finish()
            overrideTransitionCompat()
        }

        val WywozButton = findViewById<LinearLayout>(R.id.wywoz)
        WywozButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciMethodActivity::class.java)
            startActivity(intent)
            finish()
            overrideTransitionCompat()
        }

        val AtrakcjeButton = findViewById<LinearLayout>(R.id.atrakcje)
        AtrakcjeButton.setOnClickListener {
            val intent = Intent(this, AttractionActivity::class.java)
            startActivity(intent)
            finish()
            overrideTransitionCompat()
        }
    }

    private fun setupViewPager() {
        sliderAdapter = SliderAdapter(
            trashManager = trashManager,
            onTrashTileInteraction = { action ->
                when (action) {
                    is SliderAdapter.TrashTileAction.SelectMunicipality -> {
                        showEndpointSelectionDialog()
                    }
                    is SliderAdapter.TrashTileAction.GoToWasteActivity -> {
                        navigateToWasteActivity()
                    }
                }
            }
        )
        viewPager.adapter = sliderAdapter
        viewPager.offscreenPageLimit = 1
    }

    private fun setupIndicators() {
        val indicatorCount = 2
        
        for (i in 0 until indicatorCount) {
            val indicator = View(this).apply {
                layoutParams = LinearLayout.LayoutParams(24, 24).apply {
                    setMargins(8, 0, 8, 0)
                }
                background = if (i == 0) {
                    getDrawable(R.drawable.indicator_active)
                } else {
                    getDrawable(R.drawable.indicator_inactive)
                }
            }
            indicatorDots.addView(indicator)
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateIndicators(position)
            }
        })
    }

    private fun updateIndicators(selectedPosition: Int) {
        for (i in 0 until indicatorDots.childCount) {
            val indicator = indicatorDots.getChildAt(i)
            indicator.background = if (i == selectedPosition) {
                getDrawable(R.drawable.indicator_active)
            } else {
                getDrawable(R.drawable.indicator_inactive)
            }
        }
    }

    private fun showEndpointSelectionDialog() {
        try {
            showSubjectSelectionDialog()
        } catch (e: Exception) {
            println("MainActivity: Błąd w showEndpointSelectionDialog: ${e.message}")
            e.printStackTrace()
            Toast.makeText(this, "Błąd dialogu wyboru: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun showSubjectSelectionDialog() {
        try {
            val subjects = listOf("Czysty Region")
            
            val listView = android.widget.ListView(this)
            val adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_list_item_1, subjects)
            listView.adapter = adapter
            
            listView.setOnItemClickListener { _, _, position, _ ->
                try {
                    currentDialog?.dismiss()
                    showMunicipalitySelectionDialog()
                } catch (e: Exception) {
                    println("MainActivity: Błąd w wyborze podmiotu: ${e.message}")
                    e.printStackTrace()
                }
            }
            
            val dialog = android.app.AlertDialog.Builder(this)
                .setTitle("Wybierz podmiot")
                .setView(listView)
                .setNegativeButton("Anuluj") { dialog, _ ->
                    dialog.dismiss()
                }
                .create()
                
            currentDialog = dialog
            dialog.show()
        } catch (e: Exception) {
            println("MainActivity: Błąd w showSubjectSelectionDialog: ${e.message}")
            e.printStackTrace()
            Toast.makeText(this, "Błąd dialogu wyboru podmiotu: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun showMunicipalitySelectionDialog() {
        try {
            val municipalities = listOf("Cisek", "Dobrodzień", "Kędzierzyn-Koźle")
            
            val listView = android.widget.ListView(this)
            val adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_list_item_1, municipalities)
            listView.adapter = adapter
            
            listView.setOnItemClickListener { _, _, position, _ ->
                try {
                    val selectedMunicipality = municipalities[position]
                    currentDialog?.dismiss()
                    showRegionSelectionDialog(selectedMunicipality)
                } catch (e: Exception) {
                    println("MainActivity: Błąd w wyborze gminy: ${e.message}")
                    e.printStackTrace()
                }
            }
            
            val dialog = android.app.AlertDialog.Builder(this)
                .setTitle("Wybierz gminę")
                .setView(listView)
                .setNegativeButton("Wstecz") { dialog, _ ->
                    dialog.dismiss()
                    showSubjectSelectionDialog()
                }
                .create()
                
            currentDialog = dialog
            dialog.show()
        } catch (e: Exception) {
            println("MainActivity: Błąd w showMunicipalitySelectionDialog: ${e.message}")
            e.printStackTrace()
            Toast.makeText(this, "Błąd dialogu wyboru gminy: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun showRegionSelectionDialog(municipality: String) {
        try {
            val endpoints = trashManager.getAllEndpoints()

            val filteredEndpoints = endpoints.filter { (_, name) ->
                when (municipality) {
                    "Cisek" -> name.startsWith("Cisek")
                    "Dobrodzień" -> name.startsWith("Dobrodzień")
                    "Kędzierzyn-Koźle" -> name.startsWith("Kędzierzyn-Koźle")
                    else -> false
                }
            }
            
            if (filteredEndpoints.isEmpty()) {
                Toast.makeText(this, "Brak dostępnych regionów dla $municipality", Toast.LENGTH_SHORT).show()
                return
            }
            
            val regionNames = filteredEndpoints.map { it.second }
            
            val listView = android.widget.ListView(this)
            val adapter = android.widget.ArrayAdapter(this, android.R.layout.simple_list_item_1, regionNames)
            listView.adapter = adapter
            
            listView.setOnItemClickListener { _, _, position, _ ->
                try {
                    val selectedEndpoint = filteredEndpoints[position]
                    currentDialog?.dismiss()
                    applySelectedEndpoint(selectedEndpoint)
                } catch (e: Exception) {
                    println("MainActivity: Błąd w wyborze regionu: ${e.message}")
                    e.printStackTrace()
                }
            }
            
            val dialog = android.app.AlertDialog.Builder(this)
                .setTitle("Wybierz region - $municipality")
                .setView(listView)
                .setNegativeButton("Wstecz") { dialog, _ ->
                    dialog.dismiss()
                    showMunicipalitySelectionDialog()
                }
                .create()
                
            currentDialog = dialog
            dialog.show()
        } catch (e: Exception) {
            println("MainActivity: Błąd w showRegionSelectionDialog: ${e.message}")
            e.printStackTrace()
            Toast.makeText(this, "Błąd dialogu wyboru regionu: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun applySelectedEndpoint(selectedEndpoint: Pair<Int, String>) {
        try {
            clearAllOldFavorites()
            
            trashManager.setSelectedEndpoint(selectedEndpoint.first)
            sliderAdapter.refreshTrashData()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Błąd wyboru: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupSearchFunctionality() {
        searchEditText.setOnClickListener {
            val intent = Intent(this, SearchResultsActivity::class.java)
            intent.putExtra("query", searchEditText.text.toString())
            startActivity(intent)
            overrideTransitionCompat()
        }
        
        searchEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val intent = Intent(this, SearchResultsActivity::class.java)
                intent.putExtra("query", searchEditText.text.toString())
                startActivity(intent)
                overrideTransitionCompat()

                searchEditText.clearFocus()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    private fun startClock() {
        job = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                val timeState = getCurrentTime()

                val adapterTimeState = SliderAdapter.TimeState(
                    date = timeState.date,
                    dayOfWeek = timeState.dayOfWeek,
                    time = timeState.time
                )
                sliderAdapter.updateTimeState(adapterTimeState)
                delay(1000)
            }
        }
    }

    data class TimeState(val date: String, val dayOfWeek: String, val time: String)

    private fun getCurrentTime(): TimeState {
        val zone = TimeZone.getTimeZone("Europe/Warsaw").toZoneId()
        val now = ZonedDateTime.now(zone)

        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val dayFormatter = DateTimeFormatter.ofPattern("EEEE", Locale("pl", "PL"))
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

        val formattedDay = now.format(dayFormatter).replaceFirstChar { it.uppercaseChar() }

        return TimeState(
            date = now.format(dateFormatter),
            dayOfWeek = formattedDay,
            time = now.format(timeFormatter)
        )
    }
    
    private fun checkOldFavorites() {
        if (trashManager.isMigrationDone()) {
            return
        }

        val oldPrefs = getSharedPreferences("FavoriteRegions", MODE_PRIVATE)
        val allKeys = oldPrefs.all

        for ((key, value) in allKeys) {
            if (key.startsWith("favorite_") && value == true) {
                val region = key.removePrefix("favorite_")

                val endpointId = convertRegionToEndpoint(region)
                if (endpointId != null) {
                    trashManager.setSelectedEndpoint(endpointId)

                    oldPrefs.edit().clear().apply()
                    
                    sliderAdapter.refreshTrashData()
                    return
                } else {
                    println("MainActivity: Nie można skonwertować regionu '$region'")
                }
            }
        }

        val kkPrefs = getSharedPreferences("FavoriteRegionsKK", MODE_PRIVATE)
        val kkKeys = kkPrefs.all

        for ((key, value) in kkKeys) {
            if (key.startsWith("favorite_") && value == true) {
                val region = key.removePrefix("favorite_")

                val endpointId = convertRegionToEndpoint(region)
                if (endpointId != null) {
                    trashManager.setSelectedEndpoint(endpointId)

                    kkPrefs.edit().clear().apply()
                    
                    sliderAdapter.refreshTrashData()
                    return
                }
            }
        }

        trashManager.setMigrationDone()
    }
    
    private fun clearAllOldFavorites() {
        val oldPrefs = getSharedPreferences("FavoriteRegions", MODE_PRIVATE)
        oldPrefs.edit().clear().apply()

        val kkPrefs = getSharedPreferences("FavoriteRegionsKK", MODE_PRIVATE)
        kkPrefs.edit().clear().apply()
    }
    
    private fun convertRegionToEndpoint(region: String): Int? {
        val result = when (region) {
            "kobylice" -> 364
            "kobylice_w" -> 362  
            "kobylice_f" -> 462
            "bycznica" -> 363
            "bycznica_w" -> 361
            "bycznica_f" -> 463

            "kuzniczka" -> 443
            "azoty" -> 449
            "azoty_w" -> 453
            "blachownia" -> 448
            "cisowa" -> 450
            "kozle" -> 444
            "kozle_w" -> 456
            "lenartowice" -> 447
            "pogorzelec" -> 442
            "pogorzelec_w" -> 455
            "slawiecice" -> 445
            "srodmiescie" -> 446
            "srodmiescie_w_gr" -> 452
            "srodmiescie_w_wp" -> 451
            "srodmiescie_w_chemik" -> 454
            "firmy" -> 499

            "baki_j" -> 436
            "baki_w" -> 439
            "baki_f" -> 497
            "blachow_j" -> 435
            "blachow_w" -> 440
            "blachow_f" -> 496
            "dobrodzien_j" -> 438
            "dobrodzien_w" -> 441
            "dobrodzien_f" -> 494
            "warlow_j" -> 437
            "warlow_w" -> 434
            "warlow_f" -> 495
            
            else -> null
        }
        return result
    }
    
    private fun navigateToWasteActivity() {
        val intent = if (trashManager.isCisekMunicipality()) {
            Intent(this, WywozSmieciCisekActivity::class.java)
        } else if (trashManager.isDobrodzienMunicipality()) {
            Intent(this, WywozSmieciDobrodzienActivity::class.java)
        } else {
            Intent(this, WywozSmieciKKActivity::class.java)
        }
        startActivity(intent)
        overrideTransitionCompat()
    }
}
