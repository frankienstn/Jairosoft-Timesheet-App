package com.example.jairosofttimesheet.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jairosofttimesheet.data.model.LoginRequest
import com.example.jairosofttimesheet.data.remote.AuthApiService
import com.example.jairosofttimesheet.data.remote.retrofit
import com.example.jairosofttimesheet.data.repository.Repository
import com.example.jairosofttimesheet.data.preferences.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val apiService = retrofit.create(AuthApiService::class.java)
    private val repository = Repository(apiService)

    private val _loginMessage = MutableStateFlow<String?>(null)
    val loginMessage: StateFlow<String?> = _loginMessage

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val _authToken = MutableStateFlow<String?>(null)
    val authToken: StateFlow<String?> = _authToken

    fun login(email: String, password: String, rememberMe: Boolean, context: Context) {
        val request = LoginRequest(email, password)
        viewModelScope.launch {
            repository.loginUser(request) { success, message, token ->
                _loginSuccess.value = success
                _loginMessage.value = message
                if (success) {
                    _authToken.value = token
                    // Save credentials if remember me is checked
                    if (rememberMe) {
                        val userPreferences = UserPreferences(context)
                        userPreferences.setRememberMe(true)
                        userPreferences.saveCredentials(email, password)
                        userPreferences.saveAuthToken(token ?: "")
                    }
                }
            }
        }
    }

    fun checkAutoLogin(context: Context): Boolean {
        val userPreferences = UserPreferences(context)
        if (userPreferences.isRememberMeEnabled()) {
            val email = userPreferences.getEmail()
            val password = userPreferences.getPassword()
            val token = userPreferences.getAuthToken()
            
            if (email != null && password != null && token != null) {
                _authToken.value = token
                return true
            }
        }
        return false
    }

    fun logout(context: Context) {
        val userPreferences = UserPreferences(context)
        userPreferences.clearCredentials()
        _authToken.value = null
        _loginSuccess.value = false
    }

    fun clearMessage() {
        _loginMessage.value = null
    }
}
