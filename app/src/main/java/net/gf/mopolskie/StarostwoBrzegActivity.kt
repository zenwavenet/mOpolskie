package net.gf.mopolskie

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class StarostwoBrzegActivity : ComponentActivity() {
    private lateinit var viewModel: StarostwoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brzeg_starostwo)
        viewModel = ViewModelProvider(this)[StarostwoViewModel::class.java]

        window.statusBarColor = resources.getColor(R.color.status_bar_color, theme)
        window.decorView.systemUiVisibility = 0

        findViewById<LinearLayout>(R.id.help).setOnClickListener {
            startActivity(Intent(this, HelpActivity::class.java))
            finish()
            overridePendingTransition(0, 0)
        }

        findViewById<LinearLayout>(R.id.pulpit).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            overridePendingTransition(0, 0)
        }

        findViewById<LinearLayout>(R.id.services).setOnClickListener {
            startActivity(Intent(this, ServicesActivity::class.java))
            finish()
            overridePendingTransition(0, 0)
        }

        findViewById<LinearLayout>(R.id.more).setOnClickListener {
            startActivity(Intent(this, MoreActivity::class.java))
            finish()
            overridePendingTransition(0, 0)
        }

        lifecycleScope.launch {
            viewModel.fetchWords()
            displayWords(viewModel.words)
        }
    }

    private fun displayWords(words: List<StarostwoLocation>) {
        val wordContainer = findViewById<LinearLayout>(R.id.starostwa_container)
        if (wordContainer == null) return

        words.forEach { word ->
            val wordView = LayoutInflater.from(this).inflate(R.layout.starostwo_item, null)
            wordView.findViewById<TextView>(R.id.starostwo_branch).text = word.branch
            wordView.findViewById<TextView>(R.id.starostwo_city).text = "${word.city}, ${word.address}"

            val openContainer = wordView.findViewById<LinearLayout>(R.id.starostwo_open_container)
            word.open?.forEach { (day, hours) ->
                val row = createRowView(R.drawable.clock_five, "$day: $hours")
                openContainer.addView(row)
            }

            val phoneContainer = wordView.findViewById<LinearLayout>(R.id.starostwo_phone_container)
            word.phone?.let {
                val row = createRowView(R.drawable.phone_call, "$it")
                phoneContainer.addView(row)
            }

            val emailContainer: LinearLayout = wordView.findViewById(R.id.starostwo_email_container)
            word.email?.let { email ->
                val row = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        bottomMargin = 8
                    }
                }

                val icon = View(this).apply {
                    layoutParams = LinearLayout.LayoutParams(48, 48).apply {
                        rightMargin = 12
                    }
                    setBackgroundResource(R.drawable.at)
                    backgroundTintList = getColorStateList(R.color.icon_color)
                }

                val emailText = TextView(this).apply {
                    text = "$email"
                    setTextColor(resources.getColor(android.R.color.black, theme))
                    textSize = 14f
                    layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                    gravity = android.view.Gravity.CENTER_VERTICAL
                }

                row.addView(icon)
                row.addView(emailText)
                emailContainer.addView(row)
            }

            val departmentsContainer: LinearLayout = wordView.findViewById(R.id.starostwo_departments_container)
            word.departments?.forEach { (floor, rooms) ->
                val floorLabel = TextView(this).apply {
                    text = "$floor:"
                    setTextColor(resources.getColor(android.R.color.black, theme))
                    textSize = 14f
                    setPadding(0, 8, 0, 2)
                }
                departmentsContainer.addView(floorLabel)

                rooms.forEach { room ->
                    val roomText = TextView(this).apply {
                        text = "・$room"
                        setTextColor(resources.getColor(android.R.color.darker_gray, theme))
                        textSize = 13f
                        setPadding(16, 2, 0, 2)
                    }
                    departmentsContainer.addView(roomText)
                }
            }

            wordContainer.addView(wordView)
        }
    }

    private fun createRowView(iconRes: Int, text: String): LinearLayout {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { bottomMargin = 8 }
        }

        val icon = View(this).apply {
            layoutParams = LinearLayout.LayoutParams(48, 48).apply { rightMargin = 12 }
            setBackgroundResource(iconRes)
            backgroundTintList = getColorStateList(R.color.icon_color)
        }

        val textView = TextView(this).apply {
            this.text = text
            setTextColor(resources.getColor(android.R.color.black, theme))
            textSize = 14f
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
            gravity = Gravity.CENTER_VERTICAL
        }

        row.addView(icon)
        row.addView(textView)
        return row
    }
}

class StarostwoViewModel : ViewModel() {
    val words = mutableListOf<StarostwoLocation>()

    suspend fun fetchWords() {
        withContext(Dispatchers.IO) {
            val response = ApiStarostwoService.create().getStarostwo1()
            if (response.isSuccessful) {
                val map = response.body()?.Brzeski ?: return@withContext
                for ((_, obj) in map) {
                    val json = ApiStarostwoService.gson.toJsonTree(obj).asJsonObject
                    val location = ApiStarostwoService.gson.fromJson(json, StarostwoLocation::class.java)
                    words.add(location)
                }
            }
        }
    }
}

interface ApiStarostwoService {
    @GET("/__api/mopolskie/starostwo/1")
    suspend fun getStarostwo1(): Response<StarostwoResponse>

    companion object {
        private const val BASE_URL = "http://192.168.1.103:3000"
        val gson = com.google.gson.GsonBuilder().create()

        fun create(): ApiStarostwoService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(ApiStarostwoService::class.java)
        }
    }
}

data class StarostwoResponse(
    val Brzeski: Map<String, Any>
)

data class StarostwoLocation(
    val name: String,
    val branch: String,
    val city: String,
    val address: String,
    val phone: String?,
    val email: String?,
    val open: Map<String, String>?,
    val departments: Map<String, List<String>>?
)
