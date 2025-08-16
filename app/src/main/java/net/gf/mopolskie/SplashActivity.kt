package net.gf.mopolskie

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.gf.mopolskie.utils.NetworkManager
import net.gf.mopolskie.utils.setupModernStatusBar

class SplashActivity : ComponentActivity(), NetworkManager.NetworkConnectionListener {
    
    private lateinit var networkManager: NetworkManager
    private lateinit var statusText: TextView
    private lateinit var connectionText: TextView
    private lateinit var connectionIndicator: View
    private lateinit var progressBar: ProgressBar
    
    private var hasCheckedConnection = false
    private var splashTimeElapsed = false
    private var hasInternetConnection = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        
        setupModernStatusBar()
        initializeViews()
        
        networkManager = NetworkManager.getInstance(this)
        networkManager.addNetworkListener(this)
        networkManager.startMonitoring()
        networkManager.startPeriodicCheck()
        
        startSplashTimer()
        updateStatus("Inicjalizacja aplikacji...")
    }
    
    private fun initializeViews() {
        statusText = findViewById(R.id.statusText)
        connectionText = findViewById(R.id.connectionText)
        connectionIndicator = findViewById(R.id.connectionIndicator)
        progressBar = findViewById(R.id.progressBar)
    }
    
    private fun startSplashTimer() {
        lifecycleScope.launch {
            updateStatus("Inicjalizacja aplikacji...")
            delay(1000)

            updateStatus("Sprawdzanie połączenia z internetem...")
            delay(2000)

            updateStatus("Ładowanie danych...")
            delay(1500)

            updateStatus("Przygotowanie interfejsu...")
            delay(500)
            
            splashTimeElapsed = true
            checkAndProceed()
        }
    }
    
    private fun updateStatus(status: String) {
        statusText.text = status
    }
    
    private fun updateConnectionStatus(isConnected: Boolean, message: String) {
        connectionText.text = message
        connectionIndicator.backgroundTintList = getColorStateList(
            if (isConnected) R.color.success_color else R.color.error_color
        )
    }
    
    private fun checkAndProceed() {
        if (splashTimeElapsed) {
            if (hasInternetConnection) {
                proceedToMainActivity()
            } else {
                proceedToNoInternetActivity()
            }
        }
    }
    
    private fun proceedToMainActivity() {
        updateStatus("Uruchamianie aplikacji...")
        
        lifecycleScope.launch {
            delay(500)
            
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
    
    private fun proceedToNoInternetActivity() {
        updateStatus("Brak połączenia z internetem")
        
        lifecycleScope.launch {
            delay(1000)
            
            val intent = Intent(this@SplashActivity, NoInternetActivity::class.java)
            startActivity(intent)
            finish()

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

    override fun onNetworkAvailable() {
        runOnUiThread {
            hasInternetConnection = true
            updateConnectionStatus(true, "Połączono z internetem")
            
            if (!hasCheckedConnection) {
                hasCheckedConnection = true
                checkAndProceed()
            }
        }
    }
    
    override fun onNetworkLost() {
        runOnUiThread {
            hasInternetConnection = false
            updateConnectionStatus(false, "Brak połączenia z internetem")
            
            if (!hasCheckedConnection) {
                hasCheckedConnection = true
            }
        }
    }
    
    override fun onNetworkStatusChanged(isConnected: Boolean) {
        runOnUiThread {
            hasInternetConnection = isConnected
            updateConnectionStatus(
                isConnected,
                if (isConnected) "Połączono z internetem" else "Brak połączenia z internetem"
            )
            
            if (!hasCheckedConnection) {
                hasCheckedConnection = true
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        networkManager.removeNetworkListener(this)
        networkManager.stopMonitoring()
    }
}
