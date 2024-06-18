package com.kuhu.xosstech.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kuhu.xosstech.network.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(baseUrl: String): ViewModel() {

    private val apiRepository = ApiRepository(baseUrl)
    val responseMessage = MutableLiveData<String>()
    val loginMessage = MutableLiveData<String>()

    fun registerEmail(email: String) {
        GlobalScope.launch {
            try {
                val response = apiRepository.registerEmail(email)
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

    fun loginEmail(email: String) {
        GlobalScope.launch {
            try {
                val response = apiRepository.loginEmail(email)
                withContext(Dispatchers.Main) {
                    loginMessage.value = response.message
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    loginMessage.value = "Error: ${e.message}"
                }
            }
        }
    }
}