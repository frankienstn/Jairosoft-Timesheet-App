package com.example.jairosofttimesheet.network

import com.example.jairosofttimesheet.data.model.LoginRequest
import com.example.jairosofttimesheet.data.model.LoginResponseWrapper
import com.example.jairosofttimesheet.network.RetrofitClient
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    fun loginUser(@Body loginRequest: LoginRequest): Call<LoginResponseWrapper>
}



