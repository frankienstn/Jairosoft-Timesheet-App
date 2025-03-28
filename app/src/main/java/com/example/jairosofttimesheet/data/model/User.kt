package com.example.jairosofttimesheet.data.model

import com.squareup.moshi.Json

data class LoginRequest(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)

data class LoginResponse(
    val status: String,
    val response: LoginResponseData
)

data class LoginResponseData(
    val token: String,
    val user_id: String,
    val expires: Int
)

data class Attendance(
    val location: String,
    val date: String,
    val timeIn: String,
    val timeOut: String
)
