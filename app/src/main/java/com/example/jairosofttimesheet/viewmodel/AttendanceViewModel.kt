package com.example.jairosofttimesheet.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.jairosofttimesheet.data.model.AttendanceLog
import com.example.jairosofttimesheet.data.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*


class AttendanceViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _isClockedIn = MutableStateFlow(false)
    val isClockedIn: StateFlow<Boolean> = _isClockedIn

    private val _allLogs = MutableLiveData<List<AttendanceLog>>()
    val _attendanceLogs = MutableLiveData<List<AttendanceLog>>()
    val attendanceLogs: LiveData<List<AttendanceLog>> get() = _attendanceLogs

    fun fetchAttendanceLogs() {
        repository.getAttendanceLogs { success, logs, errorMessage ->
            if (success) {
                _allLogs.postValue(logs ?: emptyList())
                _attendanceLogs.postValue(logs ?: emptyList())
                Log.d("AttendanceViewModel", "Fetched logs: $logs")
            } else {
                Log.e("AttendanceViewModel", "Failed to fetch logs: $errorMessage")
            }
        }
    }

    fun filterLogsByDate(selectedDateMillis: Long) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = selectedDateMillis
        // Set time to start of day
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val startOfDay = calendar.timeInMillis

        // Set time to end of day
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        val endOfDay = calendar.timeInMillis

        _allLogs.value?.let { logs ->
            val filteredLogs = logs.filter { log ->
                val logDate = log.Date ?: 0L
                logDate in startOfDay..endOfDay
            }
            _attendanceLogs.postValue(filteredLogs)
        }
    }
}
