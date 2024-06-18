package com.kuhu.xosstech.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StoryBoardViewModel : ViewModel() {

    private val _currentPage = MutableLiveData<Int>()
    val currentPage: LiveData<Int> get() = _currentPage

    init {
        _currentPage.value = 0
    }

    fun nextPage() {
        _currentPage.value = (_currentPage.value ?: 0) + 1
    }

    fun skip() {
        // Handle skip logic, navigate to the new activity
    }
}