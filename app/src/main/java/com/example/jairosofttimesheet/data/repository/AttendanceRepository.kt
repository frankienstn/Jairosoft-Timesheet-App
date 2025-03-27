//package com.example.jairosofttimesheet.data.repository
//
//import com.example.jairosofttimesheet.data.model.Attendance
//import com.example.jairosofttimesheet.network.RetrofitClient
//
//class AttendanceRepository {
//    private val apiService = RetrofitClient.attendanceService
//
//    suspend fun getAttendanceLogs(token: String): List<Attendance> {
//        val bearerToken = "Bearer $token"
//        val response = apiService.getAttendanceLogs(bearerToken)
//        return response.response.logs.map {
//            Attendance(
//                attendanceStatus = it.attendanceStatus,
//                date = it.date,
//                timeIn = it.timeIn,
//                timeOut = it.timeOut
//            )
//        }
//    }
//}
