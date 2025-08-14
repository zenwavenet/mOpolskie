package net.gf.mopolskie

import net.gf.mopolskie.utils.overrideTransitionCompat
import net.gf.mopolskie.utils.setupModernStatusBar
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity

class SegregacjaActivity : ComponentActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Sprawdź czy przekazano parametr koloru
        val color = intent.getStringExtra("color")
        
        if (color != null && SegregacjaEndpoints.isValidColor(color)) {
            showColorDetails(color)
        } else {
            showColorMenu()
        }
    }
    
    private fun showColorMenu() {
        setContentView(R.layout.activity_segregacja)
        setupModernStatusBar()
        setupNavigationButtons()
        setupColorButtons()
    }
    
    private fun showColorDetails(color: String) {
        val layoutId = SegregacjaEndpoints.getSegregacjaLayout(color)
        if (layoutId != null) {
            setContentView(layoutId)
            setupModernStatusBar()
            setupNavigationButtons()
            setupBackButton()
        } else {
            showColorMenu()
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
    
    private fun setupColorButtons() {
        findViewById<LinearLayout>(R.id.black)?.setOnClickListener {
            navigateToColor("black")
        }
        
        findViewById<LinearLayout>(R.id.brown)?.setOnClickListener {
            navigateToColor("brown")
        }
        
        findViewById<LinearLayout>(R.id.blue)?.setOnClickListener {
            navigateToColor("blue")
        }
        
        findViewById<LinearLayout>(R.id.yellow)?.setOnClickListener {
            navigateToColor("yellow")
        }
        
        findViewById<LinearLayout>(R.id.green)?.setOnClickListener {
            navigateToColor("green")
        }
        
        findViewById<LinearLayout>(R.id.purple)?.setOnClickListener {
            navigateToColor("purple")
        }
        
        findViewById<LinearLayout>(R.id.pink)?.setOnClickListener {
            navigateToColor("pink")
        }
    }
    
    private fun setupBackButton() {
        findViewById<TextView>(R.id.back)?.setOnClickListener {
            val intent = Intent(this, SegregacjaActivity::class.java)
            startActivity(intent)
            finish()
            overrideTransitionCompat()
        }
    }
    
    private fun navigateToColor(color: String) {
        val intent = Intent(this, SegregacjaActivity::class.java)
        intent.putExtra("color", color)
        startActivity(intent)
        finish()
        overrideTransitionCompat()
    }
}
