package net.gf.mopolskie

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.ComponentActivity

class StarostwoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_starostwo)

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

        val BrzegButton = findViewById<LinearLayout>(R.id.brzeg)
        BrzegButton.setOnClickListener {
            val intent = Intent(this, StarostwoBrzegActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val GlubczyceButton = findViewById<LinearLayout>(R.id.glubczyce)
        GlubczyceButton.setOnClickListener {
            val intent = Intent(this, StarostwoGłubczyceActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val KedzierzynKozleButton = findViewById<LinearLayout>(R.id.kedzierzynsko_kozielski)
        KedzierzynKozleButton.setOnClickListener {
            val intent = Intent(this, StarostwoKedzierzynskoKozielskieActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val KluczborskiButton = findViewById<LinearLayout>(R.id.kluczborski)
        KluczborskiButton.setOnClickListener {
            val intent = Intent(this, StarostwoKluczborskiActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val KrapkowickiButton = findViewById<LinearLayout>(R.id.krapkowicki)
        KrapkowickiButton.setOnClickListener {
            val intent = Intent(this, StarostwoKrapkowickiActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val NamyslowskiButton = findViewById<LinearLayout>(R.id.namyslowski)
        NamyslowskiButton.setOnClickListener {
            val intent = Intent(this, StarostwoNamyslowskiActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val NyskiButton = findViewById<LinearLayout>(R.id.nyski)
        NyskiButton.setOnClickListener {
            val intent = Intent(this, StarostwoNyskiActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val OleskiButton = findViewById<LinearLayout>(R.id.oleski)
        OleskiButton.setOnClickListener {
            val intent = Intent(this, StarostwoOleskiActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val OpolskiButton = findViewById<LinearLayout>(R.id.opolski)
        OpolskiButton.setOnClickListener {
            val intent = Intent(this, StarostwoOpolskiActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val PrudnickiButton = findViewById<LinearLayout>(R.id.prudnicki)
        PrudnickiButton.setOnClickListener {
            val intent = Intent(this, StarostwoPrudnickiActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val StrzeleckiButton = findViewById<LinearLayout>(R.id.strzelecki)
        StrzeleckiButton.setOnClickListener {
            val intent = Intent(this, StarostwoStrzeleckiActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }
    }
}
