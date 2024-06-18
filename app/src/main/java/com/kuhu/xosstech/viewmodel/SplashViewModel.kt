package com.kuhu.xosstech.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.kuhu.xosstech.util.SharedPreferencesManager

class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferencesManager = SharedPreferencesManager(application)

    fun checkToken(): Boolean {
        val token = sharedPreferencesManager.getLoginToken()
        return !token.isNullOrEmpty()
    }
}