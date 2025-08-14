package net.gf.mopolskie

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import kotlinx.coroutines.*
import net.gf.mopolskie.utils.overrideTransitionCompat
import net.gf.mopolskie.utils.setupModernStatusBar
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MainActivity : ComponentActivity() {
    private lateinit var dateText: TextView
    private lateinit var dayText: TextView
    private lateinit var timeText: TextView
    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupModernStatusBar()

        dateText = findViewById(R.id.dateText)
        dayText = findViewById(R.id.dayText)
        timeText = findViewById(R.id.timeText)

        startClock()

        val HelpButton = findViewById<LinearLayout>(R.id.help)
        HelpButton.setOnClickListener {
            val intent = Intent(this, HelpActivity::class.java)
            startActivity(intent)
            finish()
            overrideTransitionCompat()
        }

        val OtherButton = findViewById<LinearLayout>(R.id.other)
        OtherButton.setOnClickListener {
            val intent = Intent(this, ServicesActivity::class.java)
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
            val intent = Intent(this, WywozSmieciActivity::class.java)
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

    override fun onDestroy() {
        super.onDestroy()
        job?.cancel()
    }

    private fun startClock() {
        job = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                val timeState = getCurrentTime()
                dateText.text = timeState.date
                dayText.text = timeState.dayOfWeek
                timeText.text = timeState.time
                delay(100)
            }
        }
    }

    data class TimeState(val date: String, val dayOfWeek: String, val time: String)

    private fun getCurrentTime(): TimeState {
        val zone = TimeZone.getTimeZone("Europe/Warsaw").toZoneId()
        val now = ZonedDateTime.now(zone)

        val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val dayFormatter = DateTimeFormatter.ofPattern("EEEE", Locale("pl", "PL"))
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

        val formattedDay = now.format(dayFormatter).replaceFirstChar { it.uppercaseChar() }

        return TimeState(
            date = now.format(dateFormatter),
            dayOfWeek = formattedDay,
            time = now.format(timeFormatter)
        )
    }
}
