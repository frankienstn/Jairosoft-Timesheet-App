package com.example.jairosofttimesheet.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun TimesheetScreen (navController: NavController) {
    }


@Preview(showBackground = true)
@Composable
fun TimesheetScreenPreview() {
    val navController = rememberNavController()
    TimesheetScreen(navController)
}
