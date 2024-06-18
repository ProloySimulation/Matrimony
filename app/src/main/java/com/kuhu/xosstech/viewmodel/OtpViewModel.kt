package com.kuhu.xosstech.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kuhu.xosstech.data.Data
import com.kuhu.xosstech.data.UserProfile
import com.kuhu.xosstech.network.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OtpViewModel(baseUrl: String): ViewModel() {

    private val apiRepository = ApiRepository(baseUrl)
    val responseMessage = MutableLiveData<Data>()

    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile: LiveData<UserProfile> get() = _userProfile

    fun otpVerify(otp:String,email: String) {
        GlobalScope.launch {
            try {
                val response = apiRepository.verifyOtp(otp,email)
                withContext(Dispatchers.Main) {
                    if(response.status == "success")
                    {
                        responseMessage.value = response.data
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
//                    responseMessage.value = null
                }
            }
        }
    }

    fun getUserProfile(token: String) {
        GlobalScope.launch {
            try {
                val response = apiRepository.userProfile(token)
                withContext(Dispatchers.Main) {
                    if (response.status == "success") {
                        _userProfile.value = response.userProfile
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("errorresult", e.toString())
                }
            }
        }
    }
}