package com.kuhu.xosstech.network

import com.kuhu.xosstech.data.*
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiRepository(private val baseUrl: String) {

    private val apiInterface: ApiInterface by lazy {
        createApiService()
    }

    private fun createApiService(): ApiInterface {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiInterface::class.java)
    }

    suspend fun registerEmail(email: String): ApiResponse {
        val emailRequest = EmailRequest(email)
        return apiInterface.registerEmail(emailRequest)
    }

    suspend fun loginEmail(email: String): ApiResponse {
        val emailRequest = EmailRequest(email)
        return apiInterface.loginEmail(emailRequest)
    }

    suspend fun verifyOtp(otp: String,email:String): ApiResponse {
        val otpRequest = OtpVerify(email,otp)
        return apiInterface.verifyOtp(otpRequest)
    }

    suspend fun updateProfile(updatedFields: Map<String, String>,token:String): ApiResponse {
        return apiInterface.profileUpdate(token,updatedFields)
    }

    suspend fun suggestedProfile(token:String): SuggestedProfile {
        return apiInterface.suggestedList(token)
    }

    suspend fun sendRequest(token:String,id:Int): ApiResponse {
        return apiInterface.sendRequest(token,id)
    }

    suspend fun matchRequest(token:String): MatchList {
        return apiInterface.matchRequest(token)
    }

    suspend fun getNotification(token:String): NotificationsResponse {
        return apiInterface.getNotification(token)
    }

    suspend fun userProfile(token:String): Profile {
        return apiInterface.userProfile(token)
    }

    suspend fun acceptRequest(token:String,id:Int,updatedFields: Map<String, String>): ApiResponse {
        return apiInterface.acceptRequest(token,id,updatedFields)
    }

    suspend fun acceptedList(token:String): SuggestedProfile {
        return apiInterface.acceptedList(token)
    }

    suspend fun searchList(token:String,updatedFields: Map<String, String>): SuggestedProfile {
        return apiInterface.searchList(token,updatedFields)
    }

    suspend fun otherProfile(token:String,id:Int): Profile {
        return apiInterface.otherProfile(token,id)
    }

    suspend fun uploadProfile(token:String,filePart: MultipartBody.Part): ApiResponse {
        return apiInterface.uploadProfile(token,filePart)
    }

    suspend fun sendMessage(token:String,updatedFields: Map<String, String>): ApiResponse {
        return apiInterface.sendSms(token,updatedFields)
    }

    suspend fun receiveMessage(token:String,id:Int): MessageResponse {
        return apiInterface.receiveMessage(token,id)
    }

    suspend fun profileViews(token:String): ProfileViewList {
        return apiInterface.profileViewList(token)
    }

    suspend fun logOut(token:String): ApiResponse {
        return apiInterface.logOut(token)
    }

    suspend fun sendBdAppsSubscription(mobileNumber: String): ApiResponse {
        return apiInterface.bdAppsSubscription(mobileNumber)
    }
}