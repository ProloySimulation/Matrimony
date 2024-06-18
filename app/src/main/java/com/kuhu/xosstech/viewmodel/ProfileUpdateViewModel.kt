package com.kuhu.xosstech.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kuhu.xosstech.network.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileUpdateViewModel(baseUrl: String): ViewModel()  {

    private val apiRepository: ApiRepository = ApiRepository(baseUrl)
    val responseMessage = MutableLiveData<String>()

    fun updateProfile(updatedFields: Map<String, String>,token:String) {
        GlobalScope.launch {
            try {
                val response = apiRepository.updateProfile(updatedFields,token)
                withContext(Dispatchers.Main) {
                    responseMessage.value = response.message
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    responseMessage.value = "Error: ${e.message}"
                }
            }
        }
    }
}