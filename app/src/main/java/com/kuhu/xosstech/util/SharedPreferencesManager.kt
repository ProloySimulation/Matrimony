package com.kuhu.xosstech.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "MyPreferences"
        private const val KEY_LOGIN_TOKEN = "LoginToken"
        private const val KEY_USER_ID = "UserId"
    }

    fun saveLoginToken(mobileNumber: String) {
        sharedPreferences.edit().putString(KEY_LOGIN_TOKEN, mobileNumber).apply()
    }

    fun getLoginToken(): String? {
        return sharedPreferences.getString(KEY_LOGIN_TOKEN, null)
    }

    fun clearLoginToken(){
        sharedPreferences.edit().remove(KEY_LOGIN_TOKEN).apply()
    }

    fun saveUserId(userId: Int) {
        sharedPreferences.edit().putInt(KEY_USER_ID, userId).apply()
    }

    fun getUserId(): Int? {
        return sharedPreferences.getInt(KEY_USER_ID, 0)
    }
}