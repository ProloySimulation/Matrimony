package com.kuhu.xosstech.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kuhu.xosstech.data.ProfileView
import com.kuhu.xosstech.data.UserProfile
import com.kuhu.xosstech.network.ApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MatchViewModel(baseUrl: String) :ViewModel() {

    private val apiRepository = ApiRepository(baseUrl)

    private val _profileView = MutableLiveData<List<ProfileView>>()
    val profileRequest: LiveData<List<ProfileView>> get() = _profileView

    //Back Button
    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean> get() = _navigateBack

    private val _matchReuquest = MutableLiveData<List<UserProfile>>()
    val matchRequest: LiveData<List<UserProfile>> get() = _matchReuquest

    init {
        _navigateBack.value = false
    }

    fun onBackButtonClicked() {
        _navigateBack.value = true
    }

    fun getProfileView(token: String) {
        GlobalScope.launch {
            try {
                val response = apiRepository.profileViews(token)
                withContext(Dispatchers.Main) {
                    if (response.status == "success") {
                        _profileView.value = response.matchData
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("errorresult", e.toString())
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
                        _matchReuquest.value = response.data
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {

                }
            }
        }
    }

}