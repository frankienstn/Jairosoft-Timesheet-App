package com.example.jairosofttimesheet.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jairosofttimesheet.ui.theme.JairosoftTimesheetTheme
import com.example.jairosofttimesheet.ui.theme.gradientDBlue
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileAnalyticsScreen(navController: NavController) {
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var isClockedIn by remember { mutableStateOf(false) }
    var timeCounter by remember { mutableLongStateOf(0L) }
    val userName by remember { mutableStateOf("User") }
    val scrollState = rememberScrollState()

    // Attendance and Tracked Hours Data
    val trackedHours = remember { mutableStateListOf<Pair<String, Long>>() }
    val attendanceList = remember { mutableStateListOf<Triple<String, String, String>>() }

    var showClockInDialog by remember { mutableStateOf(false) }
    var currentLocation by remember { mutableStateOf("Fetching location...") }

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

    fun fetchLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    val addressList = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                    if (!addressList.isNullOrEmpty()) {
                        currentLocation = addressList[0].getAddressLine(0)
                    } else {
                        currentLocation = "Location not found"
                    }
                } ?: run {
                    currentLocation = "Unable to get location"
                }
            }
            .addOnFailureListener {
                Log.e("LocationError", "Failed to fetch location", it)
                currentLocation = "Error fetching location"
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
            // Header Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
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
                            if (!isClockedIn) {
                                fetchLocation()
                                showClockInDialog = true
                            } else {
                                isClockedIn = false
                                val timeOut = SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(Date())
                                if (attendanceList.isNotEmpty()) {
                                    val lastIndex = attendanceList.lastIndex
                                    attendanceList[lastIndex] = attendanceList[lastIndex].copy(third = timeOut)
                                    val workedHours = timeCounter / 3600
                                    trackedHours.add(Pair(attendanceList[lastIndex].first, workedHours))
                                }
                                timeCounter = 0
                            }
                        },
                        modifier = Modifier.size(120.dp, 35.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isClockedIn) Color(0xFFDC3545) else Color(0xFF28A745)
                        )
                    ) {
                        Text(text = if (isClockedIn) "Clock Out" else "Clock In", color = Color.White)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier.size(120.dp, 35.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = formattedTime)
                        }
                    }
                }
            }

            // Date Display
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF203859))
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Today is ${SimpleDateFormat("MM/dd/yyyy, EEEE", Locale.getDefault()).format(Date())}",
                        color = Color.White,
                        fontSize = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))



            // On-Going Projects
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF89BAFA))
            ) {
                Box(modifier = Modifier.padding(16.dp), contentAlignment = Alignment.CenterStart) {
                    Text(text = "On-Going Projects:", fontSize = 16.sp, color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tracked Hours
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD9D9D9))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(text = "Tracked Hours", fontSize = 16.sp)
                    trackedHours.forEach { (day, hours) ->
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Text(text = day, modifier = Modifier.weight(1f))
                            Box(
                                modifier = Modifier
                                    .weight(3f)
                                    .height(20.dp)
                                    .background(Color.Green)
                            ) {
                                Text(text = "$hours h", color = Color.Black, modifier = Modifier.align(Alignment.Center))
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Attendance
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF203859))
            ) {
                Column(modifier = Modifier.fillMaxSize().padding(10.dp)) {
                    Text(text = "Attendance", color = Color.White, fontSize = 16.sp)
                    attendanceList.forEach { (date, timeIn, timeOut) ->
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                            Text(text = date, color = Color.White, modifier = Modifier.weight(1f))
                            Text(text = timeIn, color = Color.White, modifier = Modifier.weight(1f))
                            Text(text = timeOut, color = Color.White, modifier = Modifier.weight(1f))
                        }
                    }
                }
                // Show Attendance Button (Retained)
                Button(
                    onClick = { navController.navigate("AttendanceScreen") },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text(text = "Show Attendance", color = Color.Black, fontSize = 14.sp)
                }
            }

            // Show Clock-In Confirmation Popup
            if (showClockInDialog) {
                AlertDialog(
                    onDismissRequest = { showClockInDialog = false },
                    confirmButton = {
                        TextButton(onClick = {
                            isClockedIn = true
                            val date = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Date())
                            val timeIn = SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(Date())
                            attendanceList.add(Triple(date, timeIn, "--"))
                            showClockInDialog = false
                        }) { Text("Yes, clock me in!", color = Color.Blue) }
                    },
                    dismissButton = { TextButton(onClick = { showClockInDialog = false }) { Text("No", color = Color.Red) } },
                    title = { Text("Clock In") },
                    text = { Text("Confirm Clock In Location:\n$currentLocation") }
                )
            }


            Spacer(modifier = Modifier.height(16.dp))
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
