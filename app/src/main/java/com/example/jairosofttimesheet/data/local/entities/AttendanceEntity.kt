package com.example.jairosofttimesheet.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendance_table")
data class AttendanceEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,  // YYYY-MM-DD
    val dayOfWeek: String,  // e.g., Monday, Tuesday
    val timeIn: String,  // HH:mm:ss
    val timeOut: String?,  // HH:mm:ss (nullable)
    val totalHours: Long?  // In minutes
)
