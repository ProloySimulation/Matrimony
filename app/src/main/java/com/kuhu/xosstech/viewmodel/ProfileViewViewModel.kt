package com.kuhu.xosstech.viewmodel

import androidx.lifecycle.ViewModel
import com.kuhu.xosstech.network.ApiRepository

class ProfileViewViewModel(baseUrl: String) : ViewModel() {

    private val apiRepository = ApiRepository(baseUrl)

}