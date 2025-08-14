package net.gf.mopolskie.utils

import android.app.Activity
import android.os.Build
import android.view.WindowInsetsController
import androidx.core.view.WindowCompat
import net.gf.mopolskie.R

fun Activity.setupModernStatusBar() {
    window.statusBarColor = resources.getColor(R.color.status_bar_color, theme)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.insetsController?.let { controller ->
            controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility = 0
    }
}

fun Activity.overrideTransitionCompat() {
    @Suppress("DEPRECATION")
    overridePendingTransition(0, 0)
}
