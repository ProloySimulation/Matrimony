package com.kuhu.xosstech.network

import com.kuhu.xosstech.data.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiInterface {

    @POST("/projects/Matrimony/public/api/register")
    suspend fun registerEmail(@Body requestBody: EmailRequest): ApiResponse

    @POST("/projects/Matrimony/public/api/otp-login")
    suspend fun loginEmail(@Body requestBody: EmailRequest): ApiResponse

    @POST("/projects/Matrimony/public/api/verify-otp")
    suspend fun verifyOtp(@Body requestBody: OtpVerify): ApiResponse

    @PATCH("/projects/Matrimony/public/api/profile-info")
    suspend fun profileUpdate(
        @Header("Authorization") token: String,
        @Body requestBody: Map<String, String>): ApiResponse

    @GET("/projects/Matrimony/public/api/profile-suggest")
    suspend fun suggestedList(@Header("Authorization") token: String): SuggestedProfile

    @POST("/projects/Matrimony/public/api/send-match-request/{id}")
    suspend fun sendRequest(@Header("Authorization") token: String,
    @Path("id") id:Int): ApiResponse

    @GET("/projects/Matrimony/public/api/match-requests")
    suspend fun matchRequest(@Header("Authorization") token: String): MatchList

    @GET("/projects/Matrimony/public/api/notifications")
    suspend fun getNotification(@Header("Authorization") token: String): NotificationsResponse

    @GET("/projects/Matrimony/public/api/profile-info")
    suspend fun userProfile(@Header("Authorization") token: String): Profile

    @PATCH("/projects/Matrimony/public/api/respond-to-match-request/{id}")
    suspend fun acceptRequest(@Header("Authorization") token: String,
                              @Path("id") id:Int,
                              @Body requestBody: Map<String, String>): ApiResponse

    @GET("/projects/Matrimony/public/api/match-lists")
    suspend fun acceptedList(@Header("Authorization") token: String): SuggestedProfile

    @POST("/projects/Matrimony/public/api/search")
    suspend fun searchList(@Header("Authorization") token: String,
                           @Body requestBody: Map<String, String>): SuggestedProfile

    @GET("/projects/Matrimony/public/api/profile/{id}")
    suspend fun otherProfile(@Header("Authorization") token: String,
                             @Path("id") id:Int,): Profile

    @Multipart
    @POST("/projects/Matrimony/public/api/profile-picture")
    suspend fun uploadProfile(@Header("Authorization") token: String,
                              @Part filePart:MultipartBody.Part): ApiResponse

    @POST("/projects/Matrimony/public/api/send-message")
    suspend fun sendSms(@Header("Authorization") token: String,
                        @Body requestBody: Map<String, String>): ApiResponse

    @GET("/projects/Matrimony/public/api/get-message")
    suspend fun receiveMessage(@Header("Authorization") token: String,
                               @Query("receiver_id")id:Int): MessageResponse

    @POST("/projects/Matrimony/public/api/logout")
    suspend fun logOut(@Header("Authorization") token: String): ApiResponse

    @GET("/projects/Matrimony/public/api/profile-viewer-list")
    suspend fun profileViewList(@Header("Authorization") token: String): ProfileViewList

    @FormUrlEncoded
    @POST("/ussd/kuhu/subscription.php")
    suspend fun bdAppsSubscription(@Field("mobilenumber") mobileNumber: String): ApiResponse
}