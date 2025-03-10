package com.example.jairosofttimesheet.data.repository

import com.example.jairosofttimesheet.data.local.dao.AttendanceDao
import com.example.jairosofttimesheet.data.local.dao.DaySummary
import com.example.jairosofttimesheet.data.local.entities.AttendanceEntity

class TimesheetRepository(private val attendanceDao: AttendanceDao) {
    suspend fun insertAttendance(attendance: AttendanceEntity) {
        attendanceDao.insertAttendance(attendance)
    }

    suspend fun getAttendanceByDate(date: String): AttendanceEntity? {
        return attendanceDao.getAttendanceByDate(date)
    }

    suspend fun getAllAttendance(): List<AttendanceEntity> {
        return attendanceDao.getAllAttendance()
    }

    suspend fun getWeeklySummary(): List<DaySummary> {
        return attendanceDao.getWeeklySummary()
    }
}
