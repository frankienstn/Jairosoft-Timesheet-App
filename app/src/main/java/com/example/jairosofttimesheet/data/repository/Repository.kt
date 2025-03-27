package com.example.jairosofttimesheet.data.repository

import android.util.Log
import com.example.jairosofttimesheet.data.model.LoginRequest
import com.example.jairosofttimesheet.data.model.LoginResponseWrapper
import com.example.jairosofttimesheet.data.model.LoginUser
import com.example.jairosofttimesheet.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(private val apiService: ApiService) {

    private fun bearerToken(token: String): String = "Bearer $token"

    fun loginUser(
        loginRequest: LoginRequest,
        onResult: (success: Boolean, message: String?) -> Unit
    ) {
        apiService.loginUser(loginRequest).enqueue(object : Callback<LoginResponseWrapper> {
            override fun onResponse(
                call: Call<LoginResponseWrapper>,
                response: Response<LoginResponseWrapper>
            ) {
                if (response.isSuccessful) {
                    val token = response.body()?.response?.token
                    Log.d("LoginResponse", "Success: Token = $token")
                    onResult(true, "Login successful")
                } else {
                    Log.e("LoginResponse", "API Error: ${response.errorBody()?.string()}")
                    onResult(false, "Invalid email or password")
                }
            }

            override fun onFailure(call: Call<LoginResponseWrapper>, t: Throwable) {
                Log.e("LoginResponse", "Network Failure: ${t.localizedMessage}")
                onResult(false, "Network error: ${t.localizedMessage}")
            }
        })
    }
}
