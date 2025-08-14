package net.gf.mopolskie
import net.gf.mopolskie.utils.overrideTransitionCompat
import net.gf.mopolskie.utils.setupModernStatusBar
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
class AttractionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attraction_main)
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
        val ServicesButton = findViewById<LinearLayout>(R.id.services)
        ServicesButton.setOnClickListener {
            val intent = Intent(this, ServicesActivity::class.java)
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
        val CastleButton = findViewById<LinearLayout>(R.id.castle)
        CastleButton.setOnClickListener {
            val intent = Intent(this, AttractionCastleActivity::class.java)
            startActivity(intent)
            finish()
            overrideTransitionCompat()
        }
        val MuseumButton = findViewById<LinearLayout>(R.id.museum)
        MuseumButton.setOnClickListener {
            val intent = Intent(this, AttractionMuseumActivity::class.java)
            startActivity(intent)
            finish()
            overrideTransitionCompat()
        }
        val NatureButton = findViewById<LinearLayout>(R.id.nature)
        NatureButton.setOnClickListener {
            val intent = Intent(this, AttractionNatureActivity::class.java)
            startActivity(intent)
            finish()
            overrideTransitionCompat()
        }
        val OtherButton = findViewById<LinearLayout>(R.id.other)
        OtherButton.setOnClickListener {
            val intent = Intent(this, AttractionOtherActivity::class.java)
            startActivity(intent)
            finish()
            overrideTransitionCompat()
        }
    }
}
