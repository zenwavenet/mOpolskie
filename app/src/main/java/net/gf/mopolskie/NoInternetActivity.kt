package net.gf.mopolskie

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.gf.mopolskie.utils.NetworkManager
import net.gf.mopolskie.utils.setupModernStatusBar

class NoInternetActivity : ComponentActivity(), NetworkManager.NetworkConnectionListener {
    
    private lateinit var networkManager: NetworkManager
    private lateinit var connectionStatusText: TextView
    private lateinit var connectionStatusIndicator: View
    private lateinit var retryButton: LinearLayout
    private lateinit var settingsButton: LinearLayout
    private lateinit var retryProgressBar: ProgressBar
    
    private var isRetrying = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_internet)
        
        setupModernStatusBar()
        initializeViews()
        setupClickListeners()
        
        networkManager = NetworkManager.getInstance(this)
        networkManager.addNetworkListener(this)
        networkManager.startMonitoring()
        networkManager.startPeriodicCheck()
        
        // Sprawdź początkowy stan połączenia
        checkInitialConnectionState()
    }
    
    private fun initializeViews() {
        connectionStatusText = findViewById(R.id.connectionStatusText)
        connectionStatusIndicator = findViewById(R.id.connectionStatusIndicator)
        retryButton = findViewById(R.id.retryButton)
        settingsButton = findViewById(R.id.settingsButton)
        retryProgressBar = findViewById(R.id.retryProgressBar)
    }
    
    private fun setupClickListeners() {
        retryButton.setOnClickListener {
            if (!isRetrying) {
                retryConnection()
            }
        }
        
        settingsButton.setOnClickListener {
            openNetworkSettings()
        }
    }
    
    private fun checkInitialConnectionState() {
        lifecycleScope.launch {
            val isConnected = networkManager.hasInternetConnection()
            updateConnectionStatus(isConnected)
            
            if (isConnected) {
                // Jeśli jest połączenie, wróć do głównej aktywności
                returnToMainActivity()
            }
        }
    }
    
    private fun retryConnection() {
        isRetrying = true
        retryProgressBar.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            updateConnectionStatus(false, "Sprawdzanie połączenia...")
            
            // Sprawdź połączenie kilka razy z krótkimi przerwami
            repeat(3) { attempt ->
                delay(1000)
                val isConnected = networkManager.hasInternetConnection()
                
                if (isConnected) {
                    updateConnectionStatus(true, "Połączenie przywrócone!")
                    delay(1000)
                    returnToMainActivity()
                    return@launch
                } else {
                    updateConnectionStatus(false, "Próba ${attempt + 1}/3...")
                }
            }
            
            // Jeśli nadal brak połączenia
            updateConnectionStatus(false, "Brak połączenia")
            isRetrying = false
            retryProgressBar.visibility = View.GONE
        }
    }
    
    private fun updateConnectionStatus(isConnected: Boolean, customMessage: String? = null) {
        val message = customMessage ?: if (isConnected) "Połączono z internetem" else "Brak połączenia"
        
        connectionStatusText.text = message
        connectionStatusIndicator.backgroundTintList = getColorStateList(
            if (isConnected) R.color.success_color else R.color.error_color
        )
    }
    
    private fun openNetworkSettings() {
        try {
            // Spróbuj otworzyć ustawienia Wi-Fi
            val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            startActivity(intent)
        } catch (e: Exception) {
            try {
                // Jeśli nie ma ustawień Wi-Fi, otwórz ogólne ustawienia sieci
                val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(intent)
            } catch (e2: Exception) {
                // Ostatnia próba - ogólne ustawienia
                val intent = Intent(Settings.ACTION_SETTINGS)
                startActivity(intent)
            }
        }
    }
    
    private fun returnToMainActivity() {
        lifecycleScope.launch {
            val intent = Intent(this@NoInternetActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
            
            // Animacja przejścia
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
    
    // NetworkManager.NetworkConnectionListener implementation
    override fun onNetworkAvailable() {
        runOnUiThread {
            if (!isRetrying) {
                updateConnectionStatus(true)
                
                // Automatyczne przejście po 2 sekundach
                lifecycleScope.launch {
                    delay(2000)
                    returnToMainActivity()
                }
            }
        }
    }
    
    override fun onNetworkLost() {
        runOnUiThread {
            updateConnectionStatus(false)
        }
    }
    
    override fun onNetworkStatusChanged(isConnected: Boolean) {
        runOnUiThread {
            updateConnectionStatus(isConnected)
            
            if (isConnected && !isRetrying) {
                // Automatyczne przejście po 2 sekundach jeśli jest połączenie
                lifecycleScope.launch {
                    delay(2000)
                    returnToMainActivity()
                }
            }
        }
    }
    
    override fun onResume() {
        super.onResume()
        // Sprawdź połączenie po powrocie z ustawień
        lifecycleScope.launch {
            delay(1000) // Krótkie opóźnienie na wypadek zmiany ustawień
            val isConnected = networkManager.hasInternetConnection()
            updateConnectionStatus(isConnected)
            
            if (isConnected) {
                delay(1000)
                returnToMainActivity()
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        networkManager.removeNetworkListener(this)
    }
    
    override fun onBackPressed() {
        // Pozwól na wyjście z aplikacji
        super.onBackPressed()
        finishAffinity() // Zamknij całą aplikację
    }
}
