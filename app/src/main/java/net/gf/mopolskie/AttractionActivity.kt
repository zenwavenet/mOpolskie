package net.gf.mopolskie

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.ComponentActivity

class AttractionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attraction_main)

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

        val CastleButton = findViewById<LinearLayout>(R.id.castle)
        CastleButton.setOnClickListener {
            val intent = Intent(this, AttractionCastleActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val MuseumButton = findViewById<LinearLayout>(R.id.museum)
        MuseumButton.setOnClickListener {
            val intent = Intent(this, AttractionMuseumActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val NatureButton = findViewById<LinearLayout>(R.id.nature)
        NatureButton.setOnClickListener {
            val intent = Intent(this, AttractionNatureActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val OtherButton = findViewById<LinearLayout>(R.id.other)
        OtherButton.setOnClickListener {
            val intent = Intent(this, AttractionOtherActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }
    }
}