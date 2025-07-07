package net.gf.mopolskie

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.ComponentActivity

class WywozSmieciCisekActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waste_cisek)

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

        val BycznicaWButton = findViewById<LinearLayout>(R.id.bycznicaw)
        BycznicaWButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciCisekBycznicaWActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val BycznicaButton = findViewById<LinearLayout>(R.id.bycznica)
        BycznicaButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciCisekBycznicaActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val BycznicaFButton = findViewById<LinearLayout>(R.id.bycznicaf)
        BycznicaFButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciCisekBycznicaFActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val KobyliceButton = findViewById<LinearLayout>(R.id.cisek)
        KobyliceButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciCisekKobyliceActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val KobyliceFButton = findViewById<LinearLayout>(R.id.cisekf)
        KobyliceFButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciCisekKobyliceFActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val KobyliceWButton = findViewById<LinearLayout>(R.id.cisekw)
        KobyliceWButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciCisekKobyliceWActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }
    }
}
