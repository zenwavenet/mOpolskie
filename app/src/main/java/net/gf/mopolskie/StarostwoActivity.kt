package net.gf.mopolskie

import net.gf.mopolskie.utils.overrideTransitionCompat
import net.gf.mopolskie.utils.setupModernStatusBar
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import net.gf.mopolskie.endpoints.StarostwoEndpoints
import net.gf.mopolskie.manager.StarostwoLocation
import net.gf.mopolskie.manager.StarostwoManager

class StarostwoActivity : ComponentActivity() {
    private lateinit var starostwoManager: StarostwoManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val starostwo = intent.getStringExtra("starostwo")
        
        if (starostwo != null && StarostwoEndpoints.isValidStarostwo(starostwo)) {
            showStarostwoDetails(starostwo)
        } else {
            showStarostwoMenu()
        }
    }
    
    private fun showStarostwoMenu() {
        setContentView(R.layout.activity_starostwo)
        setupModernStatusBar()
        setupNavigationButtons()
        setupStarostwoButtons()
    }
    
    private fun showStarostwoDetails(starostwo: String) {
        val layoutId = StarostwoEndpoints.getStarostwoLayout(starostwo)
        if (layoutId != null) {
            setContentView(layoutId)
            setupModernStatusBar()
            setupNavigationButtons()
            loadStarostwoData(starostwo)
        } else {
            showStarostwoMenu()
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
    
    private fun setupStarostwoButtons() {
        findViewById<LinearLayout>(R.id.brzeg)?.setOnClickListener {
            navigateToStarostwo("brzeg")
        }
        
        findViewById<LinearLayout>(R.id.glubczyce)?.setOnClickListener {
            navigateToStarostwo("glubczyce")
        }
        
        findViewById<LinearLayout>(R.id.kedzierzynsko_kozielski)?.setOnClickListener {
            navigateToStarostwo("kedzierzynsko_kozielski")
        }
        
        findViewById<LinearLayout>(R.id.kluczborski)?.setOnClickListener {
            navigateToStarostwo("kluczborski")
        }
        
        findViewById<LinearLayout>(R.id.krapkowicki)?.setOnClickListener {
            navigateToStarostwo("krapkowicki")
        }
        
        findViewById<LinearLayout>(R.id.namyslowski)?.setOnClickListener {
            navigateToStarostwo("namyslowski")
        }
        
        findViewById<LinearLayout>(R.id.nyski)?.setOnClickListener {
            navigateToStarostwo("nyski")
        }
        
        findViewById<LinearLayout>(R.id.oleski)?.setOnClickListener {
            navigateToStarostwo("oleski")
        }
        
        findViewById<LinearLayout>(R.id.opolski)?.setOnClickListener {
            navigateToStarostwo("opolski")
        }
        
        findViewById<LinearLayout>(R.id.prudnicki)?.setOnClickListener {
            navigateToStarostwo("prudnicki")
        }
        
        findViewById<LinearLayout>(R.id.strzelecki)?.setOnClickListener {
            navigateToStarostwo("strzelecki")
        }
    }
    
    private fun navigateToStarostwo(starostwo: String) {
        val intent = Intent(this, StarostwoActivity::class.java)
        intent.putExtra("starostwo", starostwo)
        startActivity(intent)
        finish()
        overrideTransitionCompat()
    }
    
    private fun loadStarostwoData(starostwo: String) {
        starostwoManager = ViewModelProvider(this)[StarostwoManager::class.java]
        
        lifecycleScope.launch {
            val success = starostwoManager.fetchStarostwoData(starostwo)
            if (success) {
                displayStarostwoData(starostwoManager.locations)
            } else {
                Toast.makeText(
                    this@StarostwoActivity, 
                    "Błąd podczas pobierania danych starostwa.", 
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun displayStarostwoData(locations: List<StarostwoLocation>) {
        val container = findViewById<LinearLayout>(R.id.starostwa_container)
        if (container == null) return
        
        locations.forEach { location ->
            val locationView = LayoutInflater.from(this).inflate(R.layout.starostwo_item, null)
            
            locationView.findViewById<TextView>(R.id.starostwo_branch)?.text = location.branch
            locationView.findViewById<TextView>(R.id.starostwo_city)?.text = "${location.city}, ${location.address}"
            
            val openContainer = locationView.findViewById<LinearLayout>(R.id.starostwo_open_container)
            location.open?.forEach { (day, hours) ->
                val row = createRowView(R.drawable.clock_five, "$day: $hours")
                openContainer?.addView(row)
            }

            val phoneContainer = locationView.findViewById<LinearLayout>(R.id.starostwo_phone_container)
            location.phone?.let {
                val row = createRowView(R.drawable.phone_call, it)
                phoneContainer?.addView(row)
            }
            
            val emailContainer = locationView.findViewById<LinearLayout>(R.id.starostwo_email_container)
            location.email?.let { email ->
                val row = createEmailRow(email)
                emailContainer?.addView(row)
            }

            val departmentsContainer = locationView.findViewById<LinearLayout>(R.id.starostwo_departments_container)
            location.departments?.forEach { (departmentName, services) ->
                val departmentHeader = createDepartmentHeader(departmentName)
                departmentsContainer?.addView(departmentHeader)

                services.forEach { service ->
                    val serviceRow = createServiceRow(service)
                    departmentsContainer?.addView(serviceRow)
                }
            }
            
            container.addView(locationView)
        }
    }
    
    private fun createRowView(iconRes: Int, text: String): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 8
            }

            addView(View(this@StarostwoActivity).apply {
                layoutParams = LinearLayout.LayoutParams(48, 48).apply {
                    rightMargin = 12
                }
                setBackgroundResource(iconRes)
                backgroundTintList = getColorStateList(R.color.icon_color)
            })

            addView(TextView(this@StarostwoActivity).apply {
                this.text = text
                setTextColor(resources.getColor(android.R.color.black, theme))
                textSize = 14f
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                gravity = Gravity.CENTER_VERTICAL
            })
        }
    }
    
    private fun createEmailRow(email: String): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 8
            }

            addView(View(this@StarostwoActivity).apply {
                layoutParams = LinearLayout.LayoutParams(48, 48).apply {
                    rightMargin = 12
                }
                setBackgroundResource(R.drawable.at)
                backgroundTintList = getColorStateList(R.color.icon_color)
            })

            addView(TextView(this@StarostwoActivity).apply {
                text = email
                setTextColor(resources.getColor(android.R.color.black, theme))
                textSize = 14f
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                gravity = Gravity.CENTER_VERTICAL
            })
        }
    }
    
    private fun createDepartmentHeader(departmentName: String): TextView {
        return TextView(this).apply {
            text = departmentName
            setTextColor(resources.getColor(android.R.color.black, theme))
            textSize = 16f
            setTypeface(null, android.graphics.Typeface.BOLD)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 16
                bottomMargin = 8
            }
        }
    }
    
    private fun createServiceRow(service: String): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                bottomMargin = 4
            }

            addView(TextView(this@StarostwoActivity).apply {
                text = "• "
                setTextColor(resources.getColor(android.R.color.black, theme))
                textSize = 14f
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
            })

            addView(TextView(this@StarostwoActivity).apply {
                text = service
                setTextColor(resources.getColor(android.R.color.black, theme))
                textSize = 14f
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            })
        }
    }
}
