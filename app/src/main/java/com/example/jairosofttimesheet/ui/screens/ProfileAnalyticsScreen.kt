package com.example.jairosofttimesheet.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jairosofttimesheet.R
import com.example.jairosofttimesheet.ui.theme.JairosoftTimesheetTheme
import com.example.jairosofttimesheet.ui.theme.gradientDBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileAnalyticsScreen(navController: NavController) {
    var isClockedIn by remember { mutableStateOf(false) }
    var timeCounter by remember { mutableLongStateOf(0L) }
    val userName by remember { mutableStateOf("User") }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    // Attendance tracking
    val trackedHours = remember { mutableStateListOf<Pair<String, Long>>() }
    val attendanceList = remember { mutableStateListOf<Triple<String, String, String>>() }

    val formattedTime = String.format(
        "%02d:%02d:%02d",
        TimeUnit.SECONDS.toHours(timeCounter),
        TimeUnit.SECONDS.toMinutes(timeCounter) % 60,
        timeCounter % 60
    )

    LaunchedEffect(isClockedIn) {
        while (isClockedIn) {
            delay(1000)
            timeCounter++
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(gradientDBlue),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(text = "Hi, $userName 👋", color = Color(0xFF2E7BE1), fontSize = 30.sp)
                    Text(text = "Happy Working!", color = Color(0xFF888888), fontSize = 12.sp)
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = {
                            isClockedIn = !isClockedIn
                            if (isClockedIn) {
                                // Clock In: Add a new entry with the current date and start counting
                                val date = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Date())
                                val timeIn = SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(Date())
                                attendanceList.add(Triple(date, timeIn, "--"))
                            } else {
                                // Clock Out: Update the last entry with a time out
                                if (attendanceList.isNotEmpty()) {
                                    val timeOut = SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(Date())
                                    val lastIndex = attendanceList.lastIndex
                                    attendanceList[lastIndex] = attendanceList[lastIndex].copy(third = timeOut)

                                    // Calculate total worked hours
                                    val workedHours = timeCounter / 3600
                                    trackedHours.add(Pair(attendanceList[lastIndex].first, workedHours))
                                }
                            }
                            timeCounter = 0 // Reset time counter
                        },
                        modifier = Modifier.size(120.dp, 35.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isClockedIn) Color(0xFFDC3545) else Color(0xFF28A745)
                        )
                    ) {
                        Text(text = if (isClockedIn) "Clock Out" else "Clock In")
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Card(modifier = Modifier.size(120.dp, 35.dp)) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = formattedTime)
                        }
                    }
                }
            }

            Card(modifier = Modifier.size(366.dp, 50.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF203859))) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Today is ${SimpleDateFormat("MM/dd/yyyy, EEEE", Locale.getDefault()).format(Date())}",
                        color = Color.White,
                        fontSize = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tracked Hours
            Card(
                modifier = Modifier
                    .size(366.dp, 280.dp)
                    .clickable { navController.navigate("TimesheetScreen") },
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD9D9D9))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(text = "Tracked Hours", fontSize = 16.sp)
                    trackedHours.forEach { (day, hours) ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(text = day, modifier = Modifier.weight(1f))
                            Box(
                                modifier = Modifier
                                    .weight(2f)
                                    .height(20.dp)
                                    .background(Color.Green)
                            ) {
                                Text(text = "$hours h", color = Color.White, modifier = Modifier.align(Alignment.Center))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Attendance
            Card(modifier = Modifier.size(366.dp, 337.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF203859))) {
                Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
                    Text(text = "Attendance", color = Color.White, fontSize = 16.sp)

                    attendanceList.forEach { (date, timeIn, timeOut) ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(text = date, color = Color.White, modifier = Modifier.weight(1f))
                            Text(text = timeIn, color = Color.White, modifier = Modifier.weight(1f))
                            Text(text = timeOut, color = Color.White, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileAnalyticsScreenPreview() {
    JairosoftTimesheetTheme {
        val navController = rememberNavController()
        ProfileAnalyticsScreen(navController)
    }
}
