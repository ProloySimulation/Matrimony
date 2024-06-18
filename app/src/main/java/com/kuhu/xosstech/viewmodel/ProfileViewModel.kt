package com.kuhu.xosstech.viewmodel

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kuhu.xosstech.data.ProfileView
import com.kuhu.xosstech.data.UserProfile
import com.kuhu.xosstech.network.ApiRepository
import com.kuhu.xosstech.util.Event
import com.kuhu.xosstech.util.SharedPreferencesManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class ProfileViewModel(baseUrl: String) :ViewModel() {

    private val apiRepository = ApiRepository(baseUrl)

    // This private MutableLiveData triggers the opening of the gallery
    private val _openGalleryEvent = MutableLiveData<Event<Unit>>()

    //LogOut Response
    val logoutStatus = MutableLiveData<String>()

    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile: LiveData<UserProfile> get() = _userProfile

    private val _profileView = MutableLiveData<List<ProfileView>>()
    val profileRequest: LiveData<List<ProfileView>> get() = _profileView

    private val _userId = MutableLiveData<Int>()
    val userId: LiveData<Int> get() = _userId

    private val _userIdGreaterThanZero = MediatorLiveData<Boolean>()
    val userIdGreaterThanZero: LiveData<Boolean> get() = _userIdGreaterThanZero

    //Back Button
    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean> get() = _navigateBack

    val responseMessage = MutableLiveData<String>()

    // ObservableFields for two-way data binding
    val profileSummary = ObservableField<String>()
    val designation = ObservableField<String>()
    val annualIncome = ObservableField<String>()
    val companyName = ObservableField<String>()
    val fullName = ObservableField<String>()
    val height = ObservableField<String>()
    val drinking = ObservableField<String>()
    val diet = ObservableField<String>()
    val smoking = ObservableField<String>()
    val fatherName = ObservableField<String>()
    val motherName = ObservableField<String>()

    // Edit Mood
    val isEditing = ObservableBoolean()

    val openGalleryEvent: LiveData<Event<Unit>>
        get() = _openGalleryEvent


    private lateinit var sharedPreferences: SharedPreferencesManager

    fun setSharedPreferences(manager: SharedPreferencesManager) {
        this.sharedPreferences = manager
    }

    init {
        isEditing.set(false)
        _navigateBack.value = false
        _userIdGreaterThanZero.addSource(_userId) {
            _userIdGreaterThanZero.value = it > 0
        }
    }

    fun onBackButtonClicked() {
        _navigateBack.value = true
    }

    fun setUserId(userId: Int) {
        _userId.value = userId
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

    fun updateProfile(token: String) {
        val updatedFields = mapOf(
            "profile_summary" to (profileSummary.get() ?: ""),
            "designation" to (designation.get() ?: ""),
            "income" to (annualIncome.get() ?: ""),
            "company_name" to (companyName.get() ?: ""),
            "name" to (fullName.get() ?: ""),
            "height" to (height.get() ?: ""),
            "smoking" to (smoking.get() ?: ""),
            "drinking" to (drinking.get() ?: ""),
            "father_name" to (fatherName.get() ?: ""),
            "mother_name" to (motherName.get() ?: "")
        )

        GlobalScope.launch {
            try {
                val response = apiRepository.updateProfile(updatedFields, token)
                withContext(Dispatchers.Main) {
                    // Handle the response if needed
                    isEditing.set(false)
                // Set back to view mode after save
                    getUserProfile(token)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("errorresult", e.toString())
                }
            }
        }
    }

    fun uploadProfile(token: String,filePart: MultipartBody.Part) {
        GlobalScope.launch {
            try {
                val response = apiRepository.uploadProfile(token,filePart)
                withContext(Dispatchers.Main) {
                    if(response.status == "success")
                    {
                        Log.e("profileupdate",response.toString())
                    }

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("profileupdate",e.toString())
                }
            }
        }
    }

    fun getOtherProfile(token: String,id:Int) {
        GlobalScope.launch {
            try {
                val response = apiRepository.otherProfile(token,id)
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

    fun logOut(token: String) {
        GlobalScope.launch {
            try {
                val response = apiRepository.logOut(token)
                withContext(Dispatchers.Main) {
                    if (response.status == "success") {
                        logoutStatus.value = response.status
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                }
            }
        }
    }

    // Method to handle the edit button click
    fun onEditButtonClick() {
        if (isEditing.get()) {
            // Save mode, perform save action
            updateProfile("Bearer " + sharedPreferences.getLoginToken().toString())
        }

        // Toggle between edit and save modes
        isEditing.set(!isEditing.get())
    }

    fun onImageViewClicked() {
        // Notify the Activity to handle gallery opening and permissions
        _openGalleryEvent.value = Event(Unit)
    }
}