package com.kuhu.xosstech.data

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("id")
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("content")
    val content: String,
    @SerializedName("read")
    val read: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class NotificationsResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<Notification>
)