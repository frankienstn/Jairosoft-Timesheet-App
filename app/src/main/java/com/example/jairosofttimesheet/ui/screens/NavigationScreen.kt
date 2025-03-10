package com.example.jairosofttimesheet.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.jairosofttimesheet.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NavigationScreen(
    navController: NavController,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = { TopBar(navController) },
        bottomBar = { BottomNavigationBar(navController) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            content()
        }
    }
}

@Composable
fun TopBar(navController: NavController) {
    val coroutineScope = rememberCoroutineScope()
    var isLogoutClicked by remember { mutableStateOf(false) }
    val logoutScale by animateFloatAsState(targetValue = if (isLogoutClicked) 1.1f else 1f, label = "LogoutScale")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.timesheetappbanner),
            contentDescription = "Jairosoft Logo Banner",
            modifier = Modifier.size(158.dp, 48.dp)
        )

        Image(
            painter = painterResource(id = R.drawable.logout),
            contentDescription = "Logout",
            modifier = Modifier
                .size(28.dp)
                .clickable {
                    if (!isLogoutClicked) {
                        isLogoutClicked = true
                        navController.navigate("LoginAndSignUpScreen") {
                            popUpTo("StartUpScreen") { inclusive = true }
                        }
                        coroutineScope.launch {
                            delay(300)
                            isLogoutClicked = false
                        }
                    }
                }
                .scale(logoutScale)
        )
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentRoute by navController.currentBackStackEntryAsState()
    val coroutineScope = rememberCoroutineScope()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF10161F))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavigationItem(
            navController = navController,
            iconRes = R.drawable.timesheet,
            destination = "TimesheetScreen",
            currentRoute = currentRoute?.destination?.route
        )
        NavigationItem(
            navController = navController,
            iconRes = R.drawable.home,
            destination = "ProfileAnalyticsScreen",
            currentRoute = currentRoute?.destination?.route
        )
        NavigationItem(
            navController = navController,
            iconRes = R.drawable.attendance,
            destination = "AttendanceScreen",
            currentRoute = currentRoute?.destination?.route
        )
    }
}

@Composable
fun NavigationItem(
    navController: NavController,
    iconRes: Int,
    destination: String,
    currentRoute: String?
) {
    val coroutineScope = rememberCoroutineScope()
    var isClicked by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(targetValue = if (isClicked) 1.1f else 1f, label = "NavigationScale")

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
            modifier = Modifier
                .clickable {
                    if (!isClicked && currentRoute != destination) {
                        isClicked = true
                        navController.navigate(destination)
                        coroutineScope.launch {
                            delay(300)
                            isClicked = false
                        }
                    }
                }
                .scale(scale)
        )

        if (currentRoute == destination) {
            Image(
                painter = painterResource(id = R.drawable.officialreddot),
                contentDescription = "Selected",
                modifier = Modifier.size(8.dp),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color(0XFFFF4A4A))
            )
        }
    }
}
