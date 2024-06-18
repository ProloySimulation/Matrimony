package com.kuhu.xosstech

import android.app.Application
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

class Application: Application() {

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Disable force dark mode
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}