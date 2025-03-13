package com.example.jairosofttimesheet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val _trackedHours = MutableStateFlow<Map<String, Float>>(emptyMap())
    val trackedHours: StateFlow<Map<String, Float>> = _trackedHours

    private val _isClockedIn = MutableStateFlow(false)
    val isClockedIn: StateFlow<Boolean> = _isClockedIn

    private val _runningTime = MutableStateFlow(0L)
    val runningTime: StateFlow<Long> = _runningTime

    private var clockInTimes = mutableMapOf<String, Long>()
    private var job: Job? = null

    fun clockIn(day: String) {
        if (!_isClockedIn.value) {
            _isClockedIn.value = true
            clockInTimes[day] = System.currentTimeMillis()
            startTimer(day)
        }
    }

    fun clockOut(day: String) {
        clockInTimes[day]?.let { startTime ->
            val elapsedMillis = System.currentTimeMillis() - startTime
            val elapsedHours = elapsedMillis / (1000 * 60 * 60).toFloat()

            _trackedHours.value = _trackedHours.value.toMutableMap().apply {
                this[day] = (this[day] ?: 0f) + elapsedHours
            }
        }

        _isClockedIn.value = false
        clockInTimes.remove(day)
        stopTimer()
    }

    private fun startTimer(day: String) {
        job?.cancel()
        job = viewModelScope.launch {
            while (_isClockedIn.value) {
                val elapsedMillis = System.currentTimeMillis() - (clockInTimes[day] ?: 0L)
                _runningTime.value = elapsedMillis / 1000


                val elapsedHours = elapsedMillis / (1000 * 60 * 60).toFloat()
                _trackedHours.value = _trackedHours.value.toMutableMap().apply {
                    this[day] = elapsedHours
                }

                delay(1000)
            }
        }
    }

    private fun stopTimer() {
        job?.cancel()
        _runningTime.value = 0
    }
}
