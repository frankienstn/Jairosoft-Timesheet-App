package com.example.jairosofttimesheet.data.repository

import android.util.Log
import com.example.jairosofttimesheet.data.model.AttendanceLog
import com.example.jairosofttimesheet.data.model.LoginRequest
import com.example.jairosofttimesheet.data.model.LoginResponse
import com.example.jairosofttimesheet.data.remote.AuthApiService
import com.example.jairosofttimesheet.data.auth.AttendanceApiService
import com.example.jairosofttimesheet.data.model.AttendanceResponse
import com.example.jairosofttimesheet.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(
    private val apiService: AuthApiService,
    private val attendanceApiService: AttendanceApiService = RetrofitClient.attendanceApiService
) {

    // Login function
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

    // Get Attendance Logs function
    fun getAttendanceLogs(callback: (Boolean, List<AttendanceLog>?, String?) -> Unit) {
        attendanceApiService.getAttendanceLogs().enqueue(object : Callback<AttendanceResponse> {
            override fun onResponse(call: Call<AttendanceResponse>, response: Response<AttendanceResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("AttendanceApi", "Response: $body")
                    if (body?.status == "success") {
                        callback(true, body.response.Logs, null)
                    } else {
                        callback(false, null, "API Error: ${body?.status}")
                    }
                } else {
                    callback(false, null, "API Error: ${response.errorBody()?.string()}")
                }
            }


            override fun onFailure(call: Call<AttendanceResponse>, t: Throwable) {
                // Network error or failure
                callback(false, null, "Network Error: ${t.message}")
            }
        })
    }
}
