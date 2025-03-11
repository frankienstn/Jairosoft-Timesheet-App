package com.example.jairosofttimesheet

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jairosofttimesheet.ui.screens.*
import com.example.jairosofttimesheet.ui.theme.JairosoftTimesheetTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Request location permission if not granted
        requestLocationPermission()

        setContent {
            JairosoftTimesheetTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "StartUpScreen") {
                    composable("StartUpScreen") { StartUpScreen(navController) }
                    composable("LoginScreen") { LoginScreen(navController) }
                    composable("forgotPasswordScreen") { ForgotPasswordScreen(navController) }
                    composable("NavigationScreen") {
                        NavigationScreen(navController = navController) {
                            ProfileAnalyticsScreen(navController)
                        }
                    }
                    composable("ProfileAnalyticsScreen") {
                        NavigationScreen(navController = navController) {
                            ProfileAnalyticsScreen(navController)
                        }
                    }
                    composable("TimesheetScreen") {
                        NavigationScreen(navController = navController) {
                            TimesheetScreen(navController)
                        }
                    }
                    composable("AttendanceScreen") {
                        NavigationScreen(navController = navController) {
                            AttendanceScreen(navController)
                        }
                    }
                }
            }
        }
    }

    private fun requestLocationPermission() {
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
                if (!granted) {
                    // Handle permission denial if needed
                }
            }.launch(permission)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartUpScreenPreview() {
    JairosoftTimesheetTheme {
        val navController = rememberNavController()
        StartUpScreen(navController)
    }
}
