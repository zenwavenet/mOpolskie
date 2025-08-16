package net.gf.mopolskie.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.*
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class NetworkManager private constructor(private val context: Context) {
    
    companion object {
        @Volatile
        private var INSTANCE: NetworkManager? = null
        
        fun getInstance(context: Context): NetworkManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: NetworkManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    private var connectionListeners = mutableSetOf<NetworkConnectionListener>()
    private var isMonitoring = false
    private var currentNetworkStatus = false
    
    interface NetworkConnectionListener {
        fun onNetworkAvailable()
        fun onNetworkLost()
        fun onNetworkStatusChanged(isConnected: Boolean)
    }
    
    fun addNetworkListener(listener: NetworkConnectionListener) {
        connectionListeners.add(listener)
    }
    
    fun removeNetworkListener(listener: NetworkConnectionListener) {
        connectionListeners.remove(listener)
    }
    
    fun startMonitoring() {
        if (isMonitoring) return
        
        isMonitoring = true
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)

                    CoroutineScope(Dispatchers.IO).launch {
                        val hasInternet = hasInternetConnection()
                        withContext(Dispatchers.Main) {
                            if (hasInternet && !currentNetworkStatus) {
                                currentNetworkStatus = true
                                connectionListeners.forEach { it.onNetworkAvailable() }
                                connectionListeners.forEach { it.onNetworkStatusChanged(true) }
                            }
                        }
                    }
                }
                
                override fun onLost(network: Network) {
                    super.onLost(network)
                    currentNetworkStatus = false
                    connectionListeners.forEach { it.onNetworkLost() }
                    connectionListeners.forEach { it.onNetworkStatusChanged(false) }
                }
                
                override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                    super.onCapabilitiesChanged(network, networkCapabilities)

                    CoroutineScope(Dispatchers.IO).launch {
                        val hasInternet = hasInternetConnection()
                        withContext(Dispatchers.Main) {
                            if (hasInternet != currentNetworkStatus) {
                                currentNetworkStatus = hasInternet
                                if (hasInternet) {
                                    connectionListeners.forEach { it.onNetworkAvailable() }
                                } else {
                                    connectionListeners.forEach { it.onNetworkLost() }
                                }
                                connectionListeners.forEach { it.onNetworkStatusChanged(hasInternet) }
                            }
                        }
                    }
                }
            }
            
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                .build()
                
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback!!)
        }
        
        CoroutineScope(Dispatchers.IO).launch {
            val hasInternet = hasInternetConnection()
            withContext(Dispatchers.Main) {
                currentNetworkStatus = hasInternet
                connectionListeners.forEach { it.onNetworkStatusChanged(hasInternet) }
            }
        }
    }
    
    fun stopMonitoring() {
        if (!isMonitoring) return
        
        isMonitoring = false
        networkCallback?.let { callback ->
            try {
                connectivityManager.unregisterNetworkCallback(callback)
            } catch (e: Exception) {}
        }
        networkCallback = null
    }
    
    fun isNetworkAvailable(): Boolean {
        val networkCapabilities = connectivityManager.activeNetwork?.let { network ->
            connectivityManager.getNetworkCapabilities(network)
        }
        
        return networkCapabilities?.let { capabilities ->
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } ?: false
    }
    
    suspend fun hasInternetConnection(): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = URL("https://dns.google")
            val connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            connection.requestMethod = "HEAD"
            connection.connect()
            val responseCode = connection.responseCode
            connection.disconnect()
            responseCode == 200
        } catch (e: IOException) {
            false
        } catch (e: Exception) {
            false
        }
    }
    
    fun getCurrentNetworkStatus(): Boolean {
        return currentNetworkStatus
    }

    fun startPeriodicCheck(intervalMs: Long = 5000) {
        CoroutineScope(Dispatchers.IO).launch {
            while (isMonitoring) {
                val hasInternet = hasInternetConnection()
                withContext(Dispatchers.Main) {
                    if (hasInternet != currentNetworkStatus) {
                        currentNetworkStatus = hasInternet
                        if (hasInternet) {
                            connectionListeners.forEach { it.onNetworkAvailable() }
                        } else {
                            connectionListeners.forEach { it.onNetworkLost() }
                        }
                        connectionListeners.forEach { it.onNetworkStatusChanged(hasInternet) }
                    }
                }
                delay(intervalMs)
            }
        }
    }
}
