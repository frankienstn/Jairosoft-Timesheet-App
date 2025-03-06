package com.example.jairosofttimesheet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jairosofttimesheet.ui.screens.*
import com.example.jairosofttimesheet.ui.theme.JairosoftTimesheetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}

@Preview(showBackground = true)
@Composable
fun StartUpScreenPreview() {
    JairosoftTimesheetTheme {
        val navController = rememberNavController()
        StartUpScreen(navController)
    }
}
