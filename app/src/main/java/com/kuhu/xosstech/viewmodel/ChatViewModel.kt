package com.kuhu.xosstech.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kuhu.xosstech.data.Message
import com.kuhu.xosstech.network.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(baseUrl: String) : ViewModel() {

    private val apiRepository = ApiRepository(baseUrl)

    private val _messageList = MutableLiveData<List<Message>>()
    val messageList: LiveData<List<Message>> get() = _messageList

    //Back Button
    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean> get() = _navigateBack

    init {
        _navigateBack.value = false
    }

    fun onBackButtonClicked() {
        _navigateBack.value = true
    }

    fun messageSend(token: String,updatedFields: Map<String, String>) {

        GlobalScope.launch {
            try {
                val response = apiRepository.sendMessage(token, updatedFields)
                withContext(Dispatchers.Main) {
                    if(response.status == "success")
                    {
                        updatedFields["receiver_id"]?.let { messageList(token, it.toInt()) }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("chaterror",e.toString())
                }
            }
        }
    }

    fun messageList(token: String,id:Int) {

        GlobalScope.launch {
            try {
                val response = apiRepository.receiveMessage(token, id)
                withContext(Dispatchers.Main) {
                    if(response.status == "success")
                    {
                        _messageList.value = response.data
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {

                }
            }
        }
    }
}