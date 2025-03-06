package com.example.jairosofttimesheet.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jairosofttimesheet.R
import com.example.jairosofttimesheet.ui.theme.JairosoftTimesheetTheme
import kotlinx.coroutines.delay

@Composable
fun StartUpScreen(navController: NavController? = null, isPreview: Boolean = false) {
    var visible by remember { mutableStateOf(false) }

    if (isPreview) {
        visible = true // force visibility in preview mode
    } else {
        LaunchedEffect(Unit) { visible = true }

        LaunchedEffect(Unit) {
            delay(3000)
            navController?.navigate("LoginScreen") {
                popUpTo("StartUpScreen") { inclusive = true }
            }
        }
    }

    // UI for the loading screen
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF10161F)), // the dark blue-ish background
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(visible = visible, enter = fadeIn(animationSpec = tween(300))) { //fade in animation
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.timesheetapplogopng), // the transparent logo
                    contentDescription = "Jairosoft Logo",
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .size(250.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StartUpScreenPreview() {
    JairosoftTimesheetTheme {
        StartUpScreen(isPreview = true)
    }
}




