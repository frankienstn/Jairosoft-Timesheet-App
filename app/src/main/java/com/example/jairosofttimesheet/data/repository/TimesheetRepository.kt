package com.example.jairosofttimesheet.data.repository

import com.example.jairosofttimesheet.data.remote.ApiService
import com.example.jairosofttimesheet.data.remote.UserResponse
import com.example.jairosofttimesheet.data.remote.LoginResponse
import com.example.jairosofttimesheet.data.remote.LogResponse
import com.example.jairosofttimesheet.data.remote.ClockInResponse
import javax.inject.Inject

class TimesheetRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getUsers(token: String): Result<List<UserResponse>> {
        return try {
            val response = apiService.getUsers(token)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to fetch users"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(email, password)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getLogs(token: String): Result<List<LogResponse>> {
        return try {
            val response = apiService.getLogs(token)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to fetch logs"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun clockIn(token: String, timeIn: String, timeOut: String, date: String, status: String): Result<ClockInResponse> {
        return try {
            val response = apiService.clockIn(token, timeIn, timeOut, date, status)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Clock-in failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
