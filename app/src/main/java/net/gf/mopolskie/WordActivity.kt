package net.gf.mopolskie
import net.gf.mopolskie.utils.overrideTransitionCompat
import net.gf.mopolskie.utils.setupModernStatusBar
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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
class WordActivity : ComponentActivity() {
    private lateinit var viewModel: WordViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word)
        viewModel = ViewModelProvider(this)[WordViewModel::class.java]
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
        lifecycleScope.launch {
            val success = viewModel.fetchWords()
            if (success) {
                displayWords(viewModel.words)
            } else {
                Toast.makeText(this@WordActivity, "Błąd podczas pobierania danych.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun displayWords(words: List<Word>) {
        val wordContainer = findViewById<LinearLayout>(R.id.word_container)
        if (wordContainer == null) {
            println("Nie znaleziono widoku word_container!")
            return
        }
        words.forEach { word ->
            val wordView = LayoutInflater.from(this).inflate(R.layout.word_item, null)
            val branchView: TextView = wordView.findViewById(R.id.word_branch)
            val cityView: TextView = wordView.findViewById(R.id.word_city)
            branchView.text = word.branch
            cityView.text = word.city + " " + word.address
            val openContainer: LinearLayout = wordView.findViewById(R.id.word_open_container)
            word.open.forEach { (day, hours) ->
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
                    setBackgroundResource(R.drawable.clock_five)
                    backgroundTintList = getColorStateList(R.color.icon_color)
                }
                val openText = TextView(this).apply {
                    text = "$day: $hours"
                    setTextColor(resources.getColor(android.R.color.black, theme))
                    textSize = 14f
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    )
                    gravity = android.view.Gravity.CENTER_VERTICAL
                }
                row.addView(icon)
                row.addView(openText)
                openContainer.addView(row)
            }
            val phoneContainer: LinearLayout = wordView.findViewById(R.id.word_phone_container)
            word.phone.forEach { (type, number) ->
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
                    setBackgroundResource(R.drawable.phone_call)
                    backgroundTintList = getColorStateList(R.color.icon_color)
                }
                val phoneText = TextView(this).apply {
                    text = "$type: $number"
                    setTextColor(resources.getColor(android.R.color.black, theme))
                    textSize = 14f
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1f
                    )
                    gravity = android.view.Gravity.CENTER_VERTICAL
                }
                row.addView(icon)
                row.addView(phoneText)
                phoneContainer.addView(row)
            }
            wordContainer.addView(wordView)
        }
    }
}
class WordViewModel : ViewModel() {
    val words = mutableListOf<Word>()
    suspend fun fetchWords(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = ApiService.create().getWords()
                if (response.isSuccessful) {
                    response.body()?.word?.let {
                        words.clear()
                        words.addAll(it.values)
                    }
                    true
                } else {
                    false
                }
            } catch (e: Exception) {
                false
            }
        }
    }
}
interface ApiService {
    @GET("/__api/mopolskie/word")
    suspend fun getWords(): Response<WordResponse>
    companion object {
        private const val BASE_URL = "https://api.stackflow.pl"
        fun create(): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
data class WordResponse(
    val word: Map<String, Word>
)
data class Word(
    val name: String,
    val branch: String,
    val city: String,
    val address: String,
    val open: Map<String, String>,
    val phone: Map<String, String>
)
