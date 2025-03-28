package com.example.jairosofttimesheet.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.jairosofttimesheet.data.model.AttendanceLog
import com.example.jairosofttimesheet.data.repository.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class AttendanceViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _isClockedIn = MutableStateFlow(false)
    val isClockedIn: StateFlow<Boolean> = _isClockedIn

    val _attendanceLogs = MutableLiveData<List<AttendanceLog>>()
    val attendanceLogs: LiveData<List<AttendanceLog>> get() = _attendanceLogs

    fun fetchAttendanceLogs() {
        repository.getAttendanceLogs { success, logs, errorMessage ->
            if (success) {
                _attendanceLogs.postValue(logs ?: emptyList())
                Log.d("AttendanceViewModel", "Fetched logs: $logs")
            } else {
                Log.e("AttendanceViewModel", "Failed to fetch logs: $errorMessage")
            }
        }
    }

}
