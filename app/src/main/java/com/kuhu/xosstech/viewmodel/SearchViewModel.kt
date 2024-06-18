package com.kuhu.xosstech.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kuhu.xosstech.data.UserProfile
import com.kuhu.xosstech.network.ApiRepository
import com.kuhu.xosstech.util.SharedPreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(baseUrl: String,private val sharedPreferences: SharedPreferencesManager) : ViewModel() {

    private val apiRepository = ApiRepository(baseUrl)

    private val _searchList = MutableLiveData<List<UserProfile>>()
    val searchList: LiveData<List<UserProfile>> get() = _searchList

    //Back Button
    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean> get() = _navigateBack

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    val searchQuery = MutableLiveData<String>()

    init {
        _navigateBack.value = false
    }

    fun onBackButtonClicked() {
        _navigateBack.value = true
    }

    fun setSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun onSearchIconClick() {
        // Call your searchList method here
        val token = "Bearer " + sharedPreferences.getLoginToken().toString()
        val query = searchQuery.value ?: ""
        searchList(token, mapOf("name" to query))
    }

    fun searchList(token: String,updatedFields: Map<String, String>) {
        _isLoading.value = true
        GlobalScope.launch {
            try {
                val response = apiRepository.searchList(token, updatedFields)
                withContext(Dispatchers.Main) {
                    if(response.status == "success")
                    {
                        _searchList.value = response.data
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {

                }
            }
            finally {
                withContext(Dispatchers.Main) {
                    _isLoading.value = false
                }
            }
        }
    }
}