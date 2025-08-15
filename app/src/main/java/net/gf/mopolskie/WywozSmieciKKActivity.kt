package net.gf.mopolskie
import net.gf.mopolskie.utils.overrideTransitionCompat
import net.gf.mopolskie.utils.setupModernStatusBar
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import net.gf.mopolskie.endpoints.WasteEndpoints
import net.gf.mopolskie.manager.WasteItem
import net.gf.mopolskie.manager.WasteManager
import net.gf.mopolskie.manager.WasteUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
class WywozSmieciKKActivity : ComponentActivity() {
    private lateinit var wasteManager: WasteManager
    private var selectedRegion: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        wasteManager = WasteManager()
        selectedRegion = intent.getStringExtra("region")
        
        if (selectedRegion != null) {
            showRegionData(selectedRegion!!)
        } else {
            showRegionMenu()
        }
    }
    private fun showRegionMenu() {
        setContentView(R.layout.activity_waste_kedzierzyn_kozle)
        setupStatusBar()
        setupCommonNavigation()
        setupRegionButtons()
    }
    private fun showRegionData(region: String) {
        val regionConfig = getRegionConfig(region)
        setContentView(regionConfig.layoutId)
        setupStatusBar()
        setupCommonNavigation()
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
        setupRegionButton(R.id.kuzniczka, "kuzniczka")
        setupRegionButton(R.id.azoty, "azoty")
        setupRegionButton(R.id.blachownia, "blachownia")
        setupRegionButton(R.id.cisowa, "cisowa")
        setupRegionButton(R.id.kozle, "kozle")
        setupRegionButton(R.id.lenartowice, "lenartowice")
        setupRegionButton(R.id.pogorzelec, "pogorzelec")
        setupRegionButton(R.id.slawiecice, "slawiecice")
        setupRegionButton(R.id.srodmiescie, "srodmiescie")
        
        setupRegionButton(R.id.azotyw, "azoty_w")
        setupRegionButton(R.id.kozlew, "kozle_w")
        setupRegionButton(R.id.pogorzelecw, "pogorzelec_w")
        setupRegionButton(R.id.srodmiesciewgr, "srodmiescie_w_gr")
        setupRegionButton(R.id.srodmiesciewwp, "srodmiescie_w_wp")
        setupRegionButton(R.id.srodmiesciewchemik, "srodmiescie_w_chemik")
        setupRegionButton(R.id.firmy, "firmy")
    }
    private fun setupRegionButton(buttonId: Int, region: String) {
        findViewById<LinearLayout>(buttonId)?.setOnClickListener {
            val intent = Intent(this, WywozSmieciKKActivity::class.java)
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
                Toast.makeText(this@WywozSmieciKKActivity, "Nie udało się pobrać danych.", Toast.LENGTH_SHORT).show()
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
            "kuzniczka" -> RegionConfig(R.layout.waste_kedzierzyn_kozle_kuzniczka, WasteEndpoints.KUZNICZKA)
            "azoty" -> RegionConfig(R.layout.waste_kedzierzyn_kozle_azoty, WasteEndpoints.AZOTY)
            "azoty_w" -> RegionConfig(R.layout.waste_kedzierzyn_kozle_azoty_w, WasteEndpoints.AZOTY_W)
            "blachownia" -> RegionConfig(R.layout.waste_kedzierzyn_kozle_blachownia, WasteEndpoints.BLACHOWNIA)
            "cisowa" -> RegionConfig(R.layout.waste_kedzierzyn_kozle_cisowa, WasteEndpoints.CISOWA)
            "kozle" -> RegionConfig(R.layout.waste_kedzierzyn_kozle_kozle, WasteEndpoints.KOZLE)
            "kozle_w" -> RegionConfig(R.layout.waste_kedzierzyn_kozle_kozle_w, WasteEndpoints.KOZLE_W)
            "lenartowice" -> RegionConfig(R.layout.waste_kedzierzyn_kozle_lenartowice, WasteEndpoints.LENARTOWICE)
            "pogorzelec" -> RegionConfig(R.layout.waste_kedzierzyn_kozle_pogorzelec, WasteEndpoints.POGORZELEC)
            "pogorzelec_w" -> RegionConfig(R.layout.waste_kedzierzyn_kozle_pogorzelec_w, WasteEndpoints.POGORZELEC_W)
            "slawiecice" -> RegionConfig(R.layout.waste_kedzierzyn_kozle_slawiecice, WasteEndpoints.SLAWIECICE)
            "srodmiescie" -> RegionConfig(R.layout.waste_kedzierzyn_kozle_srodmiescie, WasteEndpoints.SRODMIESCIE)
            "srodmiescie_w_gr" -> RegionConfig(R.layout.waste_kedzierzyn_kozle_srodmiescie_w_gr, WasteEndpoints.SRODMIESCIE_W_GR)
            "srodmiescie_w_wp" -> RegionConfig(R.layout.waste_kedzierzyn_kozle_srodmiescie_w_wp, WasteEndpoints.SRODMIESCIE_W_WP)
            "srodmiescie_w_chemik" -> RegionConfig(R.layout.waste_kedzierzyn_kozle_srodmiescie_w_chemik, WasteEndpoints.SRODMIESCIE_W_CHEMIK)
            "firmy" -> RegionConfig(R.layout.waste_kedzierzyn_kozle_firmy, WasteEndpoints.FIRMY)
            else -> RegionConfig(R.layout.waste_kedzierzyn_kozle_kuzniczka, WasteEndpoints.KUZNICZKA)
        }
    }
    data class RegionConfig(
        val layoutId: Int,
        val endpointId: Int
    )
}
