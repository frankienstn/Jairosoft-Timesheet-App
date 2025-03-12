package com.example.jairosofttimesheet.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    var locationText by mutableStateOf("Fetching location...")
        private set

    fun updateLocationText(newLocation: String) {
        locationText = newLocation
    }
}
