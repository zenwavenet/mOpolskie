package net.gf.mopolskie.base

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import net.gf.mopolskie.*

abstract class BaseNavigationActivity : ComponentActivity() {
    
    abstract fun getLayoutResourceId(): Int
    abstract fun setupRegionButtons()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResourceId())
        
        setupStatusBar()
        setupCommonNavigationButtons()
        setupRegionButtons()
    }

    private fun setupStatusBar() {
        window.statusBarColor = resources.getColor(R.color.status_bar_color, theme)
        window.decorView.systemUiVisibility = 0
    }

    private fun setupCommonNavigationButtons() {
        findViewById<LinearLayout>(R.id.help)?.setOnClickListener {
            startActivityWithTransition(HelpActivity::class.java)
        }

        findViewById<LinearLayout>(R.id.pulpit)?.setOnClickListener {
            startActivityWithTransition(MainActivity::class.java)
        }

        findViewById<LinearLayout>(R.id.services)?.setOnClickListener {
            startActivityWithTransition(ServicesActivity::class.java)
        }

        findViewById<LinearLayout>(R.id.more)?.setOnClickListener {
            startActivityWithTransition(MoreActivity::class.java)
        }
    }

    protected fun startActivityWithTransition(activityClass: Class<*>) {
        val intent = Intent(this, activityClass)
        startActivity(intent)
        finish()
        overridePendingTransition(0, 0)
    }

    protected fun setupRegionButton(buttonId: Int, activityClass: Class<*>) {
        findViewById<LinearLayout>(buttonId)?.setOnClickListener {
            startActivityWithTransition(activityClass)
        }
    }
}
