package net.gf.mopolskie

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.ComponentActivity

class ServicesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uslugi)

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

        val MoreButton = findViewById<LinearLayout>(R.id.more)
        MoreButton.setOnClickListener {
            val intent = Intent(this, MoreActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val WordButton = findViewById<LinearLayout>(R.id.word)
        WordButton.setOnClickListener {
            val intent = Intent(this, WordActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val SegButton = findViewById<LinearLayout>(R.id.segregacja)
        SegButton.setOnClickListener {
            val intent = Intent(this, SegregacjaActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val StaButton = findViewById<LinearLayout>(R.id.starostwa)
        StaButton.setOnClickListener {
            val intent = Intent(this, StarostwoActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }
    }
}
