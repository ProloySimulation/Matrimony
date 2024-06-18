package com.kuhu.xosstech.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kuhu.xosstech.data.MatchRequest
import com.kuhu.xosstech.data.UserProfile
import com.kuhu.xosstech.network.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RequestViewModel(baseUrl: String) : ViewModel() {

    private val apiRepository = ApiRepository(baseUrl)

    private val _acceptedReuquest = MutableLiveData<List<UserProfile>>()
    val acceptedRequest: LiveData<List<UserProfile>> get() = _acceptedReuquest

    private val _matchReuquest = MutableLiveData<List<MatchRequest>>()
    val matchRequest: LiveData<List<MatchRequest>> get() = _matchReuquest

    private val _isRequestAccepted = MutableLiveData<Boolean>()
    val isRequestAccepted: LiveData<Boolean>
        get() = _isRequestAccepted

    val requestStatus = MutableLiveData<String>()

    fun setRequestAccepted(isAccepted: Boolean) {
        _isRequestAccepted.value = isAccepted
    }

    fun matchRequest(token: String) {
        GlobalScope.launch {
            try {
                val response = apiRepository.matchRequest(token)
                withContext(Dispatchers.Main) {
                    if(response.status == "success")
                    {
                        _matchReuquest.value = response.matchData
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {

                }
            }
        }
    }

    fun acceptRequest(token:String,userId: Int,updatedFields: Map<String, String>) {
        GlobalScope.launch {
            try {
                val response = apiRepository.acceptRequest(token,userId,updatedFields)
                withContext(Dispatchers.Main) {
                    if(response.status == "success")
                    {
                       requestStatus.value = response.status
                        matchRequest(token)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {

                }
            }
        }
    }

    fun acceptedList(token: String) {
        GlobalScope.launch {
            try {
                val response = apiRepository.acceptedList(token)
                withContext(Dispatchers.Main) {
                    if(response.status == "success")
                    {
                        _acceptedReuquest.value = response.data
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {

                }
            }
        }
    }

}