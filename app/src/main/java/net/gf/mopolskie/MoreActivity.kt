package net.gf.mopolskie
import net.gf.mopolskie.utils.overrideTransitionCompat
import net.gf.mopolskie.utils.setupModernStatusBar
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
class MoreActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more)
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
        val TechnicalButton = findViewById<LinearLayout>(R.id.technical_help)
        TechnicalButton.setOnClickListener {
            val intent = Intent(this, HelpTechnicActivity::class.java)
            startActivity(intent)
            finish()
            overrideTransitionCompat()
        }
        val AboutButton = findViewById<LinearLayout>(R.id.about)
        AboutButton.setOnClickListener {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
            finish()
            overrideTransitionCompat()
        }
        findViewById<LinearLayout>(R.id.github)?.setOnClickListener {
            openWebsite("https://github.com/gofluxpl/mOpolskie")
        }
        findViewById<LinearLayout>(R.id.discord)?.setOnClickListener {
            openWebsite("https://discord.gg/VY8d7auENv")
        }
    }
    private fun openWebsite(url: String) {
        Intent(Intent.ACTION_VIEW, Uri.parse(url)).also { startActivity(it) }
    }
}
