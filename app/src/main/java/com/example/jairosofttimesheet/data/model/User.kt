package com.example.jairosofttimesheet.data.model

import com.squareup.moshi.Json

//login
data class LoginRequest(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)

data class LoginResponseWrapper(
    val status: String,
    val response: LoginResponseData
)

data class LoginResponseData(
    val token: String,
    val user_id: String,
    val expires: Long
)

data class LoginUser(
    @Json(name = "email") val email: String,
    @Json(name = "name") val name: String
)

data class Attendance(
    @Json(name = "Attendance Status") val attendanceStatus: String,
    @Json(name = "Date") val date: Long,
    @Json(name = "time-in") val timeIn: Long,
    @Json(name = "time-out") val timeOut: Long
)

data class AttendanceLogsResponseWrapper(
    val status: String,
    val response: AttendanceResponse
)

data class AttendanceResponse(
    val logs: List<AttendanceRecord>
)

data class AttendanceRecord(
    @Json(name = "Attendance Status") val attendanceStatus: String,
    @Json(name = "Date") val date: Long,
    @Json(name = "time-in") val timeIn: Long,
    @Json(name = "time-out") val timeOut: Long
)








