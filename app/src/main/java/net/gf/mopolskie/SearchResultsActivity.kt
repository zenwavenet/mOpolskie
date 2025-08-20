package net.gf.mopolskie

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.widget.doOnTextChanged
import net.gf.mopolskie.utils.overrideTransitionCompat
import net.gf.mopolskie.utils.setupModernStatusBar

class SearchResultsActivity : ComponentActivity() {
    
    private lateinit var searchEditText: EditText
    private lateinit var searchQueryText: TextView
    private lateinit var resultsCountText: TextView
    private lateinit var searchResultsContainer: LinearLayout
    private lateinit var noResultsContainer: LinearLayout

    private val searchableItems = listOf(
        SearchItem("Urząd Miejski", "urząd", "services"),
        SearchItem("Apteka", "apteka zdrowie lekarstwa", "services"),
        SearchItem("Usługi", "usługi dokumenty", "services"),
        SearchItem("Pomoc", "pomoc numery alarmowe", "help"),
        SearchItem("Numery alarmowe", "alarmowe pogotowie policja straż", "help"),
        SearchItem("Pogotowie", "pogotowie 999 ratunkowe", "help"),
        SearchItem("Policja", "policja 997", "help"),
        SearchItem("Straż pożarna", "straż pożarna 998", "help"),
        SearchItem("Urzędy", "urzędy dokumenty", "services"),
        SearchItem("Dokumenty", "dokumenty dowód paszport", "services"),
        SearchItem("Administracja", "administracja urząd", "services"),
        SearchItem("Więcej", "więcej opcje", "more"),
        SearchItem("Mapa", "mapa lokalizacja", "more"),
        SearchItem("Kontakt", "kontakt telefon", "more"),
        SearchItem("Informacje", "informacje o aplikacji", "more")
    )
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)
        setupModernStatusBar()
        
        initializeViews()
        setupSearchFunctionality()
        setupNavigation()

        val query = intent.getStringExtra("query") ?: ""
        searchEditText.setText(query)
        if (query.isNotEmpty()) {
            performSearch(query)
        }
    }
    
    private fun initializeViews() {
        searchEditText = findViewById(R.id.searchEditText)
        searchQueryText = findViewById(R.id.searchQueryText)
        resultsCountText = findViewById(R.id.resultsCountText)
        searchResultsContainer = findViewById(R.id.searchResultsContainer)
        noResultsContainer = findViewById(R.id.noResultsContainer)
    }
    
    private fun setupSearchFunctionality() {
        searchEditText.doOnTextChanged { text, _, _, _ ->
            val query = text.toString().trim()
            if (query.length >= 2) {
                performSearch(query)
            } else if (query.isEmpty()) {
                clearResults()
            }
        }
    }
    
    private fun performSearch(query: String) {
        searchQueryText.text = "Wyniki wyszukiwania dla: \"$query\""
        
        val results = searchableItems.filter { item ->
            item.name.contains(query, ignoreCase = true) || 
            item.keywords.contains(query, ignoreCase = true)
        }
        
        displayResults(results)
    }
    
    private fun displayResults(results: List<SearchItem>) {
        searchResultsContainer.removeAllViews()
        resultsCountText.text = "Znaleziono ${results.size} wyników"
        
        if (results.isEmpty()) {
            noResultsContainer.visibility = android.view.View.VISIBLE
            searchResultsContainer.visibility = android.view.View.GONE
        } else {
            noResultsContainer.visibility = android.view.View.GONE
            searchResultsContainer.visibility = android.view.View.VISIBLE
            
            results.forEach { item ->
                addResultItem(item)
            }
        }
    }
    
    private fun addResultItem(item: SearchItem) {
        val resultLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(20, 15, 20, 15)
            background = getDrawable(R.drawable.corner_bg_card)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 10)
            }
            
            setOnClickListener {
                navigateToSection(item.category)
            }
        }

        val iconView = android.view.View(this).apply {
            val iconResource = when(item.category) {
                "services" -> R.drawable.folder
                "help" -> R.drawable.emergency_call
                "more" -> R.drawable.circle_ellipsis
                else -> R.drawable.search
            }
            background = getDrawable(iconResource)
            backgroundTintList = getColorStateList(R.color.primary_color)
            layoutParams = LinearLayout.LayoutParams(48, 48).apply {
                setMargins(0, 0, 20, 0)
                gravity = android.view.Gravity.CENTER_VERTICAL
            }
        }

        val textLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
        }
        
        val titleText = TextView(this).apply {
            text = item.name
            textSize = 16f
            setTextColor(getColor(R.color.text_primary))
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }
        
        val categoryText = TextView(this).apply {
            text = when(item.category) {
                "services" -> "Usługi"
                "help" -> "Pomoc"
                "more" -> "Więcej"
                else -> ""
            }
            textSize = 12f
            setTextColor(getColor(R.color.text_secondary))
        }
        
        textLayout.addView(titleText)
        textLayout.addView(categoryText)
        
        resultLayout.addView(iconView)
        resultLayout.addView(textLayout)
        
        searchResultsContainer.addView(resultLayout)
    }
    
    private fun navigateToSection(category: String) {
        val intent = when(category) {
            "services" -> Intent(this, ServicesActivity::class.java)
            "help" -> Intent(this, HelpActivity::class.java)
            "more" -> Intent(this, MoreActivity::class.java)
            else -> Intent(this, MainActivity::class.java)
        }
        
        startActivity(intent)
        finish()
        overrideTransitionCompat()
    }
    
    private fun clearResults() {
        searchQueryText.text = "Wyniki wyszukiwania dla: \"\""
        resultsCountText.text = "Znaleziono 0 wyników"
        searchResultsContainer.removeAllViews()
        noResultsContainer.visibility = android.view.View.GONE
        searchResultsContainer.visibility = android.view.View.VISIBLE
    }
    
    private fun setupNavigation() {
        findViewById<LinearLayout>(R.id.backButton).setOnClickListener {
            finish()
            overrideTransitionCompat()
        }

        findViewById<LinearLayout>(R.id.pulpit).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            overrideTransitionCompat()
        }
        
        findViewById<LinearLayout>(R.id.services).setOnClickListener {
            startActivity(Intent(this, ServicesActivity::class.java))
            finish()
            overrideTransitionCompat()
        }
        
        findViewById<LinearLayout>(R.id.help).setOnClickListener {
            startActivity(Intent(this, HelpActivity::class.java))
            finish()
            overrideTransitionCompat()
        }
        
        findViewById<LinearLayout>(R.id.more).setOnClickListener {
            startActivity(Intent(this, MoreActivity::class.java))
            finish()
            overrideTransitionCompat()
        }
    }
    
    data class SearchItem(
        val name: String,
        val keywords: String,
        val category: String
    )
}
