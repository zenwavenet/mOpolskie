package net.gf.mopolskie
import net.gf.mopolskie.utils.overrideTransitionCompat
import net.gf.mopolskie.utils.setupModernStatusBar
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
class WywozSmieciActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waste)
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
        val CisekButton = findViewById<LinearLayout>(R.id.cisek)
        CisekButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciCisekActivity::class.java)
            startActivity(intent)
            finish()
            overrideTransitionCompat()
        }
        val KKButton = findViewById<LinearLayout>(R.id.kedzierzyn_kozle)
        KKButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciKKActivity::class.java)
            startActivity(intent)
            finish()
            overrideTransitionCompat()
        }
    }
}
