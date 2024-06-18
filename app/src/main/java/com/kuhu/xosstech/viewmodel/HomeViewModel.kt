package com.kuhu.xosstech.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kuhu.xosstech.data.UserProfile
import com.kuhu.xosstech.network.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class HomeViewModel(baseUrl: String) : ViewModel() {

    private val _suggestedProfiles = MutableLiveData<List<UserProfile>>()
    val suggestedProfiles: LiveData<List<UserProfile>> get() = _suggestedProfiles

    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile: LiveData<UserProfile> get() = _userProfile

    val responseMessage = MutableLiveData<String>()

    private val apiRepository = ApiRepository(baseUrl)

    fun suggestedList(token: String) {
        GlobalScope.launch {
            try {
                val response = apiRepository.suggestedProfile(token)
                withContext(Dispatchers.Main) {
                    if(response.status == "success")
                    {
                        _suggestedProfiles.value = response.data
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {

                }
            }
        }
    }

    fun sendRequest(token: String,id:Int) {
        GlobalScope.launch {
            try {
                val response = apiRepository.sendRequest(token,id)
                withContext(Dispatchers.Main) {
                    if(response.status == "success")
                    {
                        responseMessage.value = response.message
                    }

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    responseMessage.value = "Error: ${e.message}"
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

    fun sendBdappsSubscription(mobileNumber: String) {
        GlobalScope.launch {
            try {
                val response = apiRepository.sendBdAppsSubscription(mobileNumber)
                withContext(Dispatchers.Main) {
                    responseMessage.value = response.subscriptionStatus
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    responseMessage.value = "Error: ${e.message}"
                    Log.e("subscribeerror",e.toString())
                }
            }
        }
    }

    //bkash

    fun isWithinOneMonth(subscriptionDate: String?): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:SSS 'GMT'Z")
        sdf.timeZone = TimeZone.getTimeZone("GMT") // Set the time zone as per your requirement

        return try {
            val date = sdf.parse(subscriptionDate)
            val currentDate = Calendar.getInstance().time
            val differenceInMillis = currentDate.time - date.time
            val differenceInDays = differenceInMillis / (1000 * 60 * 60 * 24)
            differenceInDays <= 30
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}