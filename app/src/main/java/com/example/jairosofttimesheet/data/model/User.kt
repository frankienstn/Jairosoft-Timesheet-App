package com.example.jairosofttimesheet.data.model

import com.squareup.moshi.Json

data class LoginRequest(
    @Json(name = "email") val email: String,
    @Json(name = "password") val password: String
)

data class LoginResponse(
    val status: String,
    val response: LoginResponseData
)

data class LoginResponseData(
    val token: String,
    val user_id: String,
    val expires: Int
)

data class AttendanceResponse(
    val status: String,
    val response: AttendanceLogs
)

data class AttendanceLogs(
    val Logs: List<AttendanceLog>
)

data class AttendanceLog(
    val toggle: String?,
    @Json(name = "Total Hours 100%") val totalHours: Int?,
    @Json(name = "Created By") val createdBy: String?,
    val _id: String?,
    val User_id: String?,
    @Json(name = "time-out") val timeOut: Long?,
    val Date: Long?,
    @Json(name = "Attendance Status") val attendanceStatus: String?,
    @Json(name = "Created Date") val createdDate: Long?,
    @Json(name = "Modified Date") val modifiedDate: Long?,
    @Json(name = "time-in") val timeIn: Long?
)

data class AttendanceRecord(
    val location: String,
    val date: String,
    val timeIn: String,
    val timeOut: String,
    val status: String
)

data class AttendanceLogUI(
    val date: Long,
    val timeIn: Long,
    val timeOut: Long,
    val status: String
)








