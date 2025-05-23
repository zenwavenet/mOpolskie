package net.gf.mopolskie

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.ComponentActivity

class WywozSmieciKKActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waste_kedzierzyn_kozle)

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

        val KuzniczkaButton = findViewById<LinearLayout>(R.id.kuzniczka)
        KuzniczkaButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciKKKuzniczkaActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val AzotyButton = findViewById<LinearLayout>(R.id.azoty)
        AzotyButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciKKAzotyActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val BlachowniaButton = findViewById<LinearLayout>(R.id.blachownia)
        BlachowniaButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciKKBlachowniaActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val CisowaButton = findViewById<LinearLayout>(R.id.cisowa)
        CisowaButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciKKCisowaActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val KozleButton = findViewById<LinearLayout>(R.id.kozle)
        KozleButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciKKKozleActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val LenartowiceButton = findViewById<LinearLayout>(R.id.lenartowice)
        LenartowiceButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciKKLenartowiceActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val PogorzelecButton = findViewById<LinearLayout>(R.id.pogorzelec)
        PogorzelecButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciKKPogorzelecActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val SlawieciceButton = findViewById<LinearLayout>(R.id.slawiecice)
        SlawieciceButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciKKSlawieciceActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val SrodmiescieButton = findViewById<LinearLayout>(R.id.srodmiescie)
        SrodmiescieButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciKKSrodmiescieActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val AzotyWButton = findViewById<LinearLayout>(R.id.azotyw)
        AzotyWButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciKKAzotyWActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val KozleWButton = findViewById<LinearLayout>(R.id.kozlew)
        KozleWButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciKKKozleWActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val PogorzelecWButton = findViewById<LinearLayout>(R.id.pogorzelecw)
        PogorzelecWButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciKKPogorzelecWActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val SrodmiescieWGrButton = findViewById<LinearLayout>(R.id.srodmiesciewgr)
        SrodmiescieWGrButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciKKSrodmiescieWGrActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val SrodmiescieWWPButton = findViewById<LinearLayout>(R.id.srodmiesciewwp)
        SrodmiescieWWPButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciKKSrodmiescieWWPActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val SrodmiescieWChemikButton = findViewById<LinearLayout>(R.id.srodmiesciewchemik)
        SrodmiescieWChemikButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciKKSrodmiescieWChemikActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }

        val FirmyButton = findViewById<LinearLayout>(R.id.firmy)
        FirmyButton.setOnClickListener {
            val intent = Intent(this, WywozSmieciKKFirmyActivity::class.java)
            startActivity(intent)
            finish()
            overridePendingTransition(0, 0)
        }
    }
}
