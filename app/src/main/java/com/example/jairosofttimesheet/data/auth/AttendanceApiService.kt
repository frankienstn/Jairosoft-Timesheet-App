package com.example.jairosofttimesheet.data.auth


import com.example.jairosofttimesheet.data.model.AttendanceResponse
import retrofit2.Call
import retrofit2.http.GET

// API Interface
interface AttendanceApiService {

    @GET("logs")
    fun getAttendanceLogs(): Call<AttendanceResponse>
}

