package com.kuhu.xosstech.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kuhu.xosstech.data.Notification
import com.kuhu.xosstech.network.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotificationViewModel(baseUrl: String): ViewModel() {

    private val apiRepository = ApiRepository(baseUrl)
    private val _notificationList = MutableLiveData<List<Notification>>()
    val notificationList: LiveData<List<Notification>> get() = _notificationList

    fun notificationList(token: String) {
        GlobalScope.launch {
            try {
                val response = apiRepository.getNotification(token)
                withContext(Dispatchers.Main) {
                    if(response.status == "success")
                    {
                        _notificationList.value = response.data
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {

                }
            }
        }
    }
}