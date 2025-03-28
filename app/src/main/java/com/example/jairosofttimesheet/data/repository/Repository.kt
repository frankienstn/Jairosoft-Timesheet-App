package com.example.jairosofttimesheet.data.repository

import com.example.jairosofttimesheet.data.model.LoginRequest
import com.example.jairosofttimesheet.data.model.LoginResponse
import com.example.jairosofttimesheet.data.remote.AuthApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(private val apiService: AuthApiService) {

    fun loginUser(
        loginRequest: LoginRequest,
        onResult: (success: Boolean, message: String?, token: String?) -> Unit
    ) {
        val call = apiService.loginUser(loginRequest)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val token = response.body()?.response?.token
                    onResult(true, "Login successful", token)
                } else {
                    onResult(false, "Invalid email or password", null)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onResult(false, "Network error: ${t.localizedMessage}", null)
            }
        })
    }
}
