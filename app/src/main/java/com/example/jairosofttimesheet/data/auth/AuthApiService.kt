package com.example.jairosofttimesheet.data.remote


import retrofit2.http.POST
import com.example.jairosofttimesheet.data.model.LoginRequest
import com.example.jairosofttimesheet.data.model.LoginResponse
import retrofit2.Call
import retrofit2.http.Body

// API Interface
interface AuthApiService {

    @POST("login")
    fun loginUser(@Body request: LoginRequest): Call<LoginResponse>
}