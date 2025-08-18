package net.gf.mopolskie

import net.gf.mopolskie.utils.overrideTransitionCompat
import net.gf.mopolskie.utils.setupModernStatusBar
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.ComponentActivity

class ServicesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_uslugi)
        setupModernStatusBar()

        val HelpButton = findViewById<LinearLayout>(R.id.help)
        HelpButton.setOnClickListener {
            val intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
            finish()
            overrideTransitionCompat()
        }

        val MainButton = findViewById<LinearLayout>(R.id.pulpit)
        MainButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
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

        val WordButton = findViewById<LinearLayout>(R.id.word)
        WordButton.setOnClickListener {
            val intent = Intent(this, WordActivity::class.java)
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
}
