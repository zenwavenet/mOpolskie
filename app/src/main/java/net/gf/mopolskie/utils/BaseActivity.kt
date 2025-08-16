package net.gf.mopolskie.utils

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.gf.mopolskie.NoInternetActivity

abstract class BaseActivity : ComponentActivity(), NetworkManager.NetworkConnectionListener {
    
    private lateinit var networkManager: NetworkManager
    private var wasConnected = true
    private var connectionLostTime = 0L
    private val CONNECTION_TIMEOUT = 10000L // 10 sekund
    
    override fun onResume() {
        super.onResume()
        initializeNetworkMonitoring()
    }
    
    override fun onPause() {
        super.onPause()
        cleanupNetworkMonitoring()
    }
    
    private fun initializeNetworkMonitoring() {
        networkManager = NetworkManager.getInstance(this)
        networkManager.addNetworkListener(this)
        networkManager.startMonitoring()
        networkManager.startPeriodicCheck()
        
        // Sprawdź początkowy stan
        lifecycleScope.launch {
            val isConnected = networkManager.hasInternetConnection()
            if (!isConnected) {
                handleConnectionLost()
            }
        }
    }
    
    private fun cleanupNetworkMonitoring() {
        if (::networkManager.isInitialized) {
            networkManager.removeNetworkListener(this)
        }
    }
    
    override fun onNetworkAvailable() {
        runOnUiThread {
            wasConnected = true
            connectionLostTime = 0L
            onConnectionRestored()
        }
    }
    
    override fun onNetworkLost() {
        runOnUiThread {
            if (wasConnected) {
                connectionLostTime = System.currentTimeMillis()
                wasConnected = false
                
                // Czekaj CONNECTION_TIMEOUT przed pokazaniem ekranu braku internetu
                lifecycleScope.launch {
                    delay(CONNECTION_TIMEOUT)
                    
                    // Sprawdź czy nadal brak połączenia
                    if (!wasConnected && System.currentTimeMillis() - connectionLostTime >= CONNECTION_TIMEOUT) {
                        handleConnectionLost()
                    }
                }
            }
        }
    }
    
    override fun onNetworkStatusChanged(isConnected: Boolean) {
        runOnUiThread {
            if (isConnected && !wasConnected) {
                wasConnected = true
                connectionLostTime = 0L
                onConnectionRestored()
            } else if (!isConnected && wasConnected) {
                connectionLostTime = System.currentTimeMillis()
                wasConnected = false
                
                // Czekaj CONNECTION_TIMEOUT przed pokazaniem ekranu braku internetu
                lifecycleScope.launch {
                    delay(CONNECTION_TIMEOUT)
                    
                    // Sprawdź czy nadal brak połączenia
                    if (!wasConnected && System.currentTimeMillis() - connectionLostTime >= CONNECTION_TIMEOUT) {
                        handleConnectionLost()
                    }
                }
            }
        }
    }
    
    private fun handleConnectionLost() {
        // Nie przechodź do NoInternetActivity jeśli już jesteś w SplashActivity lub NoInternetActivity
        if (this::class.simpleName in listOf("SplashActivity", "NoInternetActivity")) {
            return
        }
        
        val intent = Intent(this, NoInternetActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        
        // Animacja przejścia
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
    
    // Metody które mogą być nadpisane przez aktywności potomne
    protected open fun onConnectionRestored() {
        // Domyślnie nic nie rób - aktywności potomne mogą to nadpisać
    }
    
    protected open fun onConnectionLost() {
        // Domyślnie nic nie rób - aktywności potomne mogą to nadpisać
    }
    
    // Metoda pomocnicza do sprawdzenia czy jest połączenie
    protected fun isConnectedToInternet(): Boolean {
        return if (::networkManager.isInitialized) {
            networkManager.getCurrentNetworkStatus()
        } else {
            false
        }
    }
}
