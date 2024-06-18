package com.kuhu.xosstech.data

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("sender_id") val senderId: Int? = null,
    @SerializedName("receiver_id") val receiverId: Int? = null,
    @SerializedName("message_content") val messageContent: String,
    @SerializedName("message_status") val messageStatus: String? = null,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("updated_at") val updatedAt: String? = null
)

data class MessageResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: List<Message>
)
