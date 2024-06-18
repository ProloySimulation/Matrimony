package com.kuhu.xosstech.data

import com.google.gson.annotations.SerializedName

data class Profile(
    val status: String,
    val message: String,
    @SerializedName("data") val userProfile: UserProfile
)
