package com.example.jairosofttimesheet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jairosofttimesheet.data.model.LoginRequest
import com.example.jairosofttimesheet.data.repository.Repository
import com.example.jairosofttimesheet.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val apiService = RetrofitClient.apiService
    private val repository = Repository(apiService)

    private val _loginMessage = MutableStateFlow<String?>(null)
    val loginMessage: StateFlow<String?> = _loginMessage

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess


    fun login(email: String, password: String) {
        val request = LoginRequest(email, password)
        viewModelScope.launch {
            repository.loginUser(request) { success, message ->
                _loginSuccess.value = success
                _loginMessage.value = message
            }
        }
    }

    fun clearMessage() {
        _loginMessage.value = null
    }
}
