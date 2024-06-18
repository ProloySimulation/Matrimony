package com.kuhu.xosstech.data

data class ApiResponse(val message: String,val data:Data,val status: String, val subscriptionStatus:String)

data class Data(
    val token: String,
    val user: User?
)

data class User(
    val id: Int
)