package com.example.jairosofttimesheet.data.repository

import com.example.jairosofttimesheet.data.model.LoginRequest
import com.example.jairosofttimesheet.data.model.LoginResponseWrapper
import com.example.jairosofttimesheet.data.model.LoginUser
import com.example.jairosofttimesheet.data.remote.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(private val apiService: ApiService) {


    fun loginUser(
        loginRequest: LoginRequest,
        onResult: (success: Boolean, message: String?) -> Unit
    ) {
        val call = apiService.loginUser(loginRequest)
        call.enqueue(object : Callback<LoginResponseWrapper> {
            override fun onResponse(call: Call<LoginResponseWrapper>, response: Response<LoginResponseWrapper>) {
                if (response.isSuccessful) {
                    val token = response.body()?.response?.token
                    android.util.Log.d("LoginResponse", "Success: Token = $token")
                    onResult(true, "Login successful")
                } else {
                    android.util.Log.e("LoginResponse", "API Error: ${response.errorBody()?.string()}")
                    onResult(false, "Invalid email or password")
                }
            }

            override fun onFailure(call: Call<LoginResponseWrapper>, t: Throwable) {
                android.util.Log.e("LoginResponse", "Network Failure: ${t.localizedMessage}")
                onResult(false, "Network error: ${t.localizedMessage}")
            }
        })
    }


    fun getAllUsers(
        token: String,
        onResult: (users: List<LoginUser>?, error: String?) -> Unit
    ) {
        val call = apiService.getAllUsers("Bearer $token")
        call.enqueue(object : Callback<List<LoginUser>> {
            override fun onResponse(call: Call<List<LoginUser>>, response: Response<List<LoginUser>>) {
                if (response.isSuccessful) {
                    onResult(response.body(), null)
                } else {
                    onResult(null, "Failed to fetch users")
                }
            }

            override fun onFailure(call: Call<List<LoginUser>>, t: Throwable) {
                onResult(null, "Network error: ${t.localizedMessage}")
            }
        })
    }
}
