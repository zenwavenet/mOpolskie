package net.gf.mopolskie

import net.gf.mopolskie.utils.overrideTransitionCompat
import net.gf.mopolskie.utils.setupModernStatusBar
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity

class HelpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help)
        setupModernStatusBar()

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

        setupCallButton(R.id.call_112, "112")
        setupCallButton(R.id.call_997, "997")
        setupCallButton(R.id.call_996, "996")
        setupCallButton(R.id.call_987, "987")
        setupCallButton(R.id.call_998, "998")
        setupCallButton(R.id.call_999, "999")
        setupCallButton(R.id.call_116123, "116123")
        setupCallButton(R.id.call_116111, "116111")
        setupCallButton(R.id.call_981, "981")
        setupCallButton(R.id.call_985, "985")
        setupCallButton(R.id.call_991, "991")
        setupCallButton(R.id.call_992, "992")
        setupCallButton(R.id.call_993, "993")
        setupCallButton(R.id.call_994, "994")
        setupCallButton(R.id.call_995, "995")
        setupCallButton(R.id.call_116000, "116000")
        setupCallButton(R.id.call_116006, "116006")
        setupCallButton(R.id.call_116117, "116117")
        setupCallButton(R.id.call_988, "988")
    }

    private fun setupCallButton(buttonId: Int, phoneNumber: String) {
        val button = findViewById<TextView>(buttonId)
        button.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            startActivity(intent)
        }
    }
}
