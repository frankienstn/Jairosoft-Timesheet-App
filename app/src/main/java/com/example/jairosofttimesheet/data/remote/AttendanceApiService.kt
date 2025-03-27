//package com.example.jairosofttimesheet.data.remote
//
//import com.example.jairosofttimesheet.data.model.AttendanceLogsResponseWrapper
//import com.example.jairosofttimesheet.network.RetrofitClient
//import retrofit2.http.GET
//import retrofit2.http.Header
//
//interface AttendanceApiService {
//    @GET("logs")
//    suspend fun getAttendanceLogs(
//        @Header("Authorization") token: String
//    ): AttendanceLogsResponseWrapper
//
//    companion object {
//        fun create(): AttendanceApiService {
//            return RetrofitClient.retrofit.create(AttendanceApiService::class.java)
//        }
//    }
//}
