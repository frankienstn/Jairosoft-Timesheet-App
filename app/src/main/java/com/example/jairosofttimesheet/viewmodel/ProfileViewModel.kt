package com.example.jairosofttimesheet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jairosofttimesheet.data.repository.TimesheetRepository
import com.example.jairosofttimesheet.data.remote.responses.LoginResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val repository: TimesheetRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<Result<LoginResponse>?>(null)
    val loginState: StateFlow<Result<LoginResponse>?> = _loginState

    private val _usersState = MutableStateFlow<Result<List<UserResponse>>?>(null)
    val usersState: StateFlow<Result<List<UserResponse>>?> = _usersState

    private val _logsState = MutableStateFlow<Result<List<LogResponse>>?>(null)
    val logsState: StateFlow<Result<List<LogResponse>>?> = _logsState

    // 🔹 Perform Login
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = repository.login(email, password)
        }
    }

    // 🔹 Get Users
    fun fetchUsers(token: String) {
        viewModelScope.launch {
            _usersState.value = repository.getUsers(token)
        }
    }

    // 🔹 Get Logs
    fun fetchLogs(token: String) {
        viewModelScope.launch {
            _logsState.value = repository.getLogs(token)
        }
    }
}
