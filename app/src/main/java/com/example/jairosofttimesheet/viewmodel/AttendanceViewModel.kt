//package com.example.jairosofttimesheet.viewmodel
//
//import android.util.Log
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.jairosofttimesheet.data.model.Attendance
//import com.example.jairosofttimesheet.data.repository.AttendanceRepository
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//
//class AttendanceViewModel @Inject constructor(
//    private val repository: AttendanceRepository
//) : ViewModel() {
//
//    private val _attendanceList = MutableStateFlow<List<Attendance>>(emptyList())
//    val attendanceList: StateFlow<List<Attendance>> = _attendanceList.asStateFlow()
//
//    private val _isClockedIn = MutableStateFlow(false)
//    val isClockedIn: StateFlow<Boolean> = _isClockedIn.asStateFlow()
//
////    fun fetchAttendanceLogs(token: String) {
////        viewModelScope.launch {
////            try {
////                val logs = repository.getAttendanceLogs(token)
////                _attendanceList.value = logs
////            } catch (e: Exception) {
////                Log.e("AttendanceViewModel", "Error fetching logs", e)
////            }
////        }
////    }
//
//    fun addAttendance(attendanceStatus: String, date: Long, timeIn: Long) {
//        val newAttendance = Attendance(attendanceStatus, date, timeIn, 0L)
//        _attendanceList.value = _attendanceList.value + newAttendance
//        _isClockedIn.value = true
//    }
//
//    fun updateTimeOut() {
//        val updatedList = _attendanceList.value.toMutableList()
//        if (updatedList.isNotEmpty() && updatedList.last().timeOut == 0L) {
//            updatedList[updatedList.lastIndex] = updatedList.last().copy(timeOut = System.currentTimeMillis())
//            _attendanceList.value = updatedList
//            _isClockedIn.value = false
//        }
//    }
//
//    init {
//        Log.d("AttendanceViewModel", "ViewModel initialized")
//    }
//}
