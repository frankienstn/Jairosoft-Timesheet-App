package com.example.jairosofttimesheet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.jairosofttimesheet.data.local.entities.AttendanceEntity

@Dao
interface AttendanceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(attendance: AttendanceEntity)

    @Query("SELECT * FROM attendance_table WHERE date = :date LIMIT 1")
    suspend fun getAttendanceByDate(date: String): AttendanceEntity?

    @Query("SELECT * FROM attendance_table ORDER BY date DESC")
    suspend fun getAllAttendance(): List<AttendanceEntity>

    @Query("SELECT dayOfWeek, SUM(totalHours) AS totalHours FROM attendance_table GROUP BY dayOfWeek")
    suspend fun getWeeklySummary(): List<DaySummary>
}

data class DaySummary(val dayOfWeek: String, val totalHours: Long?)
