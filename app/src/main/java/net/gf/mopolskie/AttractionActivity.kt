package net.gf.mopolskie

import net.gf.mopolskie.utils.overrideTransitionCompat
import net.gf.mopolskie.utils.setupModernStatusBar
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import net.gf.mopolskie.endpoints.AttractionEndpoints
import net.gf.mopolskie.manager.Attraction
import net.gf.mopolskie.manager.AttractionManager

class AttractionActivity : ComponentActivity() {
    private lateinit var attractionManager: AttractionManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val category = intent.getStringExtra("category")
        
        if (category != null && AttractionEndpoints.isValidCategory(category)) {
            showCategoryDetails(category)
        } else {
            showCategoryMenu()
        }
    }
    
    private fun showCategoryMenu() {
        setContentView(R.layout.activity_attraction_main)
        setupModernStatusBar()
        setupNavigationButtons()
        setupCategoryButtons()
    }
    
    private fun showCategoryDetails(category: String) {
        val layoutId = AttractionEndpoints.getAttractionLayout(category)
        if (layoutId != null) {
            setContentView(layoutId)
            setupModernStatusBar()
            setupNavigationButtons()
            loadAttractionData(category)
        } else {
            showCategoryMenu()
        }
    }
    
    private fun setupNavigationButtons() {
        findViewById<LinearLayout>(R.id.help)?.setOnClickListener {
            startActivity(Intent(this, HelpActivity::class.java))
            finish()
            overrideTransitionCompat()
        }
        
        findViewById<LinearLayout>(R.id.pulpit)?.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            overrideTransitionCompat()
        }
        
        findViewById<LinearLayout>(R.id.services)?.setOnClickListener {
            startActivity(Intent(this, ServicesActivity::class.java))
            finish()
            overrideTransitionCompat()
        }
        
        findViewById<LinearLayout>(R.id.more)?.setOnClickListener {
            startActivity(Intent(this, MoreActivity::class.java))
            finish()
            overrideTransitionCompat()
        }
    }
    
    private fun setupCategoryButtons() {
        findViewById<LinearLayout>(R.id.castle)?.setOnClickListener {
            navigateToCategory("castle")
        }
        
        findViewById<LinearLayout>(R.id.museum)?.setOnClickListener {
            navigateToCategory("museum")
        }
        
        findViewById<LinearLayout>(R.id.nature)?.setOnClickListener {
            navigateToCategory("nature")
        }
        
        findViewById<LinearLayout>(R.id.other)?.setOnClickListener {
            navigateToCategory("other")
        }
    }
    
    private fun navigateToCategory(category: String) {
        val intent = Intent(this, AttractionActivity::class.java)
        intent.putExtra("category", category)
        startActivity(intent)
        finish()
        overrideTransitionCompat()
    }
    
    private fun loadAttractionData(category: String) {
        attractionManager = ViewModelProvider(this)[AttractionManager::class.java]
        
        lifecycleScope.launch {
            val success = attractionManager.fetchAttractionData(category)
            if (success) {
                displayAttractionsWithCategories(attractionManager.attractionCategories)
            } else {
                Toast.makeText(
                    this@AttractionActivity, 
                    "Błąd podczas pobierania danych atrakcji.", 
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun displayAttractionsWithCategories(categories: Map<String, List<Attraction>>) {
        val attractionContainer = findViewById<LinearLayout>(R.id.attraction_container)
        if (attractionContainer == null) {
            return
        }
        
        categories.forEach { (categoryName, attractions) ->
            val categoryHeader = LayoutInflater.from(this).inflate(R.layout.category_header, null)
            val categoryTitle: TextView = categoryHeader.findViewById(R.id.category_title)
            categoryTitle.text = categoryName
            attractionContainer.addView(categoryHeader)

            attractions.forEachIndexed { index, attraction ->
                val attractionView = LayoutInflater.from(this).inflate(R.layout.attraction_item, null)
                
                val nameView: TextView = attractionView.findViewById(R.id.attraction_name)
                val addressView: TextView = attractionView.findViewById(R.id.attraction_address)
                val descriptionView: TextView = attractionView.findViewById(R.id.attraction_description)
                
                nameView.text = attraction.name
                addressView.text = attraction.address
                descriptionView.text = attraction.description

                if (attraction.url.isNotEmpty()) {
                    val urlContainer: LinearLayout = attractionView.findViewById(R.id.attraction_url_container)
                    val urlRow = createUrlRow(attraction.url)
                    urlContainer.addView(urlRow)
                }
                
                attractionContainer.addView(attractionView)

                if (index < attractions.size - 1) {
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
    }
    
    private fun createUrlRow(url: String): LinearLayout {
        return LinearLayout(this).apply {
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
                openUrl(url)
            }
            
            addView(TextView(this@AttractionActivity).apply {
                text = "Strona WWW"
                setTextColor(resources.getColor(android.R.color.black, theme))
                textSize = 14f
                gravity = android.view.Gravity.CENTER
            })
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
