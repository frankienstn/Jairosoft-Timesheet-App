//package com.example.jairosofttimesheet.viewmodel
//
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.jairosofttimesheet.data.repository.AttendanceRepository
//
//class AttendanceVieModelFactory(private val repository: AttendanceRepository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(AttendanceViewModel::class.java)) {
//            return AttendanceViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//}
//
//
//
//
