package com.example.jairosofttimesheet.data.remote

import retrofit2.http.*
import retrofit2.Response

interface ApiService {

    @GET("wf/users")
    suspend fun getUsers(
        @Header("Authorization") token: String
    ): Response<List<UserResponse>>

    @FormUrlEncoded
    @POST("wf/login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @GET("wf/logs")
    suspend fun getLogs(
        @Header("Authorization") token: String
    ): Response<List<LogResponse>>

    @FormUrlEncoded
    @POST("wf/time-in")
    suspend fun clockIn(
        @Header("Authorization") token: String,
        @Field("time-in") timeIn: String,
        @Field("time-out") timeOut: String,
        @Field("date") date: String,
        @Field("attendance_status") status: String
    ): Response<ClockInResponse>
}

// 📌 Response Models (Inside `ApiService.kt`)

data class UserResponse(
    val id: String,
    val email: String,
    val name: String
)

data class LoginResponse(
    val token: String,
    val userId: String
)

data class LogResponse(
    val id: String,
    val userId: String,
    val timeIn: String,
    val timeOut: String,
    val date: String
)

data class ClockInResponse(
    val status: String,
    val message: String
)
