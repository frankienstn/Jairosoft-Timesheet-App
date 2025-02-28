package com.example.jairosofttimesheet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jairosofttimesheet.ui.screens.ForgotPasswordScreen
import com.example.jairosofttimesheet.ui.screens.LoginAndSignUpScreen
import com.example.jairosofttimesheet.ui.screens.SignUpScreen
import com.example.jairosofttimesheet.ui.theme.JairosoftTimesheetTheme
import com.example.jairosofttimesheet.ui.screens.LoginScreen
import com.example.jairosofttimesheet.ui.screens.ProfileAnalyticsScreen
import com.example.jairosofttimesheet.ui.screens.StartUpScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JairosoftTimesheetTheme {
                val navController = rememberNavController()
                NavHost(navController, startDestination = "StartUpScreen") {
                    composable ("StartUpScreen") { StartUpScreen(navController) }
                    composable("LoginAndSignUpScreen") { LoginAndSignUpScreen(navController) }
                    composable("LoginScreen") { LoginScreen(navController) }
                    composable("signUpScreen") { SignUpScreen(navController) }
                    composable("forgotPasswordScreen") { ForgotPasswordScreen(navController) }
                    composable("ProfileAnalyticsScreen") { ProfileAnalyticsScreen(navController) }
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
