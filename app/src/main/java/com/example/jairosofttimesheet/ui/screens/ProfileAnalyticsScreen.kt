package com.example.jairosofttimesheet.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jairosofttimesheet.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.jairosofttimesheet.ui.theme.gradientDBlue
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.os.Looper
import androidx.compose.animation.core.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.*
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.jairosofttimesheet.viewmodel.AttendanceViewModel
import java.io.IOException
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.graphicsLayer
import com.example.jairosofttimesheet.viewmodel.AttendanceViewModelFactory
import com.example.jairosofttimesheet.data.remote.RetrofitClient
import com.example.jairosofttimesheet.data.repository.Repository
import com.example.jairosofttimesheet.ui.theme.gradientDate
import com.example.jairosofttimesheet.ui.theme.gradientOnGoing
import com.example.jairosofttimesheet.ui.theme.gradientTrackedHours
import com.example.jairosofttimesheet.viewmodel.ProfileViewModel
import java.util.Calendar
import androidx.compose.ui.tooling.preview.Preview
import com.example.jairosofttimesheet.ui.theme.JairosoftTimesheetTheme
import androidx.navigation.compose.rememberNavController
import com.example.jairosofttimesheet.data.preferences.UserPreferences

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ProfileAnalyticsScreen(navController: NavController, attendanceViewModel: AttendanceViewModel = viewModel(factory = AttendanceViewModelFactory(
    Repository(RetrofitClient.authApiService, RetrofitClient.attendanceApiService)
)), profileViewModel: ProfileViewModel = viewModel()) {
    val isClockedIn by attendanceViewModel.isClockedIn.collectAsState()
    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }
    val userEmail = remember { userPreferences.getEmail() }
    val username = remember { 
        userEmail?.let { email ->
            // Extract username from email (part before @)
            val usernamePart = email.substringBefore('@')
            // Remove numbers and capitalize first letter
            usernamePart.replace(Regex("[0-9]"), "")
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        } ?: "User"
    }

    var timeCounter by remember { mutableLongStateOf(0L) }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    // Updated for tracked hours
    val trackedHours by profileViewModel.trackedHours.collectAsState()
    val currentDay = getCurrentDay()
    val workedHours = trackedHours[getCurrentDay()] ?: 0f
    val totalMinutes = (workedHours * 60).toInt()
    val hoursPart = totalMinutes / 60
    val minutesPart = totalMinutes % 60
    val formattedWorkedHours = "${hoursPart}h and ${"%02d".format(minutesPart)}m"
    val overtime = if (workedHours > 8) workedHours - 8 else 0f
    val totalOvertimeMinutes = (overtime * 60).toInt()
    val overtimeHoursPart = totalOvertimeMinutes / 60
    val overtimeMinutesPart = totalOvertimeMinutes % 60
    val formattedOvertime = "${overtimeHoursPart}h and ${"%02d".format(overtimeMinutesPart)}m"

    // location state
    var showLocationDialog by remember { mutableStateOf(false) }
    var userLocation by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var locationText by remember { mutableStateOf("Fetching location...") }

    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    // Request permissions if not granted
    LaunchedEffect(Unit) {
        if (permissionState.status != PermissionStatus.Granted) {
            permissionState.launchPermissionRequest()
        }
    }

    // Location permission granted, request location updates
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val locationRequest = LocationRequest.create().apply {
        priority = Priority.PRIORITY_HIGH_ACCURACY
        interval = 5000
    }

    val locationCallback = rememberUpdatedState(
        object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    userLocation = LatLng(location.latitude, location.longitude)
                    val geocoder = Geocoder(context, Locale.getDefault())
                    try {
                        val addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        if (addressList.isNullOrEmpty()) {
                            locationText = "Unable to get location name"
                        } else {
                            locationText = addressList[0].locality ?: addressList[0].subAdminArea ?: "Unknown Location"
                        }
                    } catch (e: IOException) {
                        locationText = "Unable to get location name"
                    }
                }
            }
        }
    )

    //request location updates when permission is granted
    LaunchedEffect(permissionState.status) {
        if (permissionState.status == PermissionStatus.Granted) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest, locationCallback.value, Looper.getMainLooper()
            )
        }
    }

    LaunchedEffect(isClockedIn) {
        while (isClockedIn) {
            delay(1000)
            timeCounter++
        }
    }

    val runningTime by profileViewModel.runningTime.collectAsState()

    val formattedTime = String.format(
        "%02d:%02d:%02d",
        runningTime / 3600,
        (runningTime % 3600) / 60,
        runningTime % 60
    )

    // font
    val afacad = FontFamily(Font(R.font.afacad, FontWeight.Normal),)
    val afacadmedium = FontFamily(Font(R.font.afacadmedium),)
    val montserratextrabold = FontFamily(Font(R.font.montserratextrabold))
    val inter = FontFamily(Font(R.font.inter, FontWeight.Normal))
    val poppins = FontFamily(Font(R.font.poppinsregular, FontWeight.Normal))
    val poppinsextrabold = FontFamily(Font(R.font.poppinsextrabold, FontWeight.Normal))

    // Animation states
    val alpha1 = remember { Animatable(0f) }
    val alpha2 = remember { Animatable(0f) }
    val alpha3 = remember { Animatable(0f) }
    val alpha4 = remember { Animatable(0f) }
    val scale1 = remember { Animatable(0.8f) }
    val scale2 = remember { Animatable(0.8f) }
    val scale3 = remember { Animatable(0.8f) }
    val scale4 = remember { Animatable(0.8f) }

    // Smooth spring animation
    val springSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )

    // Smooth easing for fade
    val fadeEasing = FastOutSlowInEasing

    LaunchedEffect(Unit) {
        // Staggered animations with smoother timing
        delay(50) // Initial delay before starting animations
        launch {
            alpha1.animateTo(1f, animationSpec = tween(300, easing = fadeEasing))
            scale1.animateTo(1f, animationSpec = springSpec)
        }
        delay(100) // Increased delay between animations
        launch {
            alpha2.animateTo(1f, animationSpec = tween(300, easing = fadeEasing))
            scale2.animateTo(1f, animationSpec = springSpec)
        }
        delay(150)
        launch {
            alpha3.animateTo(1f, animationSpec = tween(300, easing = fadeEasing))
            scale3.animateTo(1f, animationSpec = springSpec)
        }
        delay(200)
        launch {
            alpha4.animateTo(1f, animationSpec = tween(300, easing = fadeEasing))
            scale4.animateTo(1f, animationSpec = springSpec)
        }
    }

    var isButtonClicked by remember { mutableStateOf(false) }
    val buttonScale by animateFloatAsState(
        if (isButtonClicked) 1.1f else 1f,
        label = "ButtonScale"
    )

    //START OF THE CODE SCREENS
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(gradientDBlue),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .width(393.dp)
                    .height(79.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Hi, $username!",
                        color = Color(0xFF2E7BE1),
                        fontSize = 30.sp,
                        fontFamily = montserratextrabold
                    )
                    Text(
                        text = "Happy Working!",
                        color = Color(0xFF888888),
                        fontSize = 12.sp,
                        fontFamily = inter
                    )
                }

                // Clock-In / Clock-Out
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                   val isClockedIn by profileViewModel.isClockedIn.collectAsState()

                    Button(
                        onClick = {
                            showLocationDialog = true
                            isButtonClicked = true

                            coroutineScope.launch {
                                delay(300)
                                isButtonClicked = false
                            }
                        },
                        modifier = Modifier
                            .size(120.dp, 35.dp)
                            .scale(buttonScale),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isClockedIn) Color(0xFFDC3545) else Color(
                                0xFF28A745
                            )
                        )
                    ) {
                        Text(
                            text = if (isClockedIn) "Clock Out" else "Clock In",
                            fontFamily = afacad
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Running Time (Formatted as: hh:mm:ss)
                    Card(modifier = Modifier.size(120.dp, 35.dp)) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = formattedTime,
                                fontFamily = afacad
                            )
                        }
                    }
                }

                //determine dialog content before showing the dialog
                val dialogTitle = if (isClockedIn) "Confirm Clock-Out" else "Confirm Clock-In"
                val dialogText =
                    if (isClockedIn) "Are you sure you want to clock out?" else "Your location has been detected. Do you want to proceed with clocking in?"

                //location dialog
                if (showLocationDialog) {
                    AlertDialog(
                        onDismissRequest = { showLocationDialog = false },
                        confirmButton = {
                            TextButton(onClick = {
                                showLocationDialog = false
                                val day = getCurrentDay()
                                if (profileViewModel.isClockedIn.value) {
                                    profileViewModel.clockOut(day)
                                    timeCounter = 0L
                                } else {
                                    profileViewModel.clockIn(day)
                                }
                            }) {
                                Text("Yes")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showLocationDialog = false }) {
                                Text("No")
                            }
                        },
                        title = { Text(dialogTitle) },
                        text = {
                            Column {
                                Text(dialogText)

                                val context = LocalContext.current
                                val fusedLocationClient = remember {
                                    LocationServices.getFusedLocationProviderClient(context)
                                }
                                val locationRequest = LocationRequest.create().apply {
                                    priority = Priority.PRIORITY_HIGH_ACCURACY
                                    interval = 5000 // Update every 5 seconds
                                }
                                var userLocation by remember { mutableStateOf(LatLng(0.0, 0.0)) }
                                var locationText by remember { mutableStateOf("Fetching location...") }

                                val locationCallback = object : LocationCallback() {
                                    override fun onLocationResult(locationResult: LocationResult) {
                                        locationResult.lastLocation?.let { location ->
                                            userLocation = LatLng(location.latitude, location.longitude)

                                            val geocoder = Geocoder(context, Locale.getDefault())
                                            locationText = try {
                                                val addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                                                if (!addressList.isNullOrEmpty()) {
                                                    addressList[0].locality ?: addressList[0].subAdminArea ?: "Unknown Location"
                                                } else {
                                                    "Unknown Location"
                                                }
                                            } catch (e: IOException) {
                                                "Error fetching location"
                                            }
                                        }
                                    }
                                }


                                // Start location updates
                                LaunchedEffect(Unit) {
                                    if (ActivityCompat.checkSelfPermission(
                                            context, Manifest.permission.ACCESS_FINE_LOCATION
                                        ) == PackageManager.PERMISSION_GRANTED
                                    ) {
                                        fusedLocationClient.requestLocationUpdates(
                                            locationRequest,
                                            locationCallback,
                                            Looper.getMainLooper()
                                        )
                                    } else {
                                        locationText = "Permission denied"
                                    }
                                }

                                // **Live Location TextField**
                                OutlinedTextField(
                                    value = locationText,
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { Text("Current Location") }
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // **Live Map**
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                ) {
                                    GoogleMap(
                                        modifier = Modifier.fillMaxSize(),
                                        cameraPositionState = rememberCameraPositionState {
                                            position =
                                                CameraPosition.fromLatLngZoom(userLocation, 15f)
                                        }
                                    ) {
                                        Marker(
                                            state = MarkerState(position = userLocation),
                                            title = "Your Location"
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            }

            // Date Today
            Card(
                modifier = Modifier
                    .width(320.dp)
                    .height(50.dp)
                    .alpha(alpha1.value)
                    .scale(scale1.value)
                    .background(brush = gradientDate, shape = RoundedCornerShape(8.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Today is ${
                            SimpleDateFormat("MM/dd/yyyy, EEEE", Locale.getDefault()).format(Date())
                        }",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontFamily = afacadmedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // On Going Projects
            Card(
                modifier = Modifier
                    .size(320.dp, 63.dp)
                    .alpha(alpha2.value)
                    .scale(scale2.value)
                    .background(brush = gradientOnGoing, shape = RoundedCornerShape(8.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .size(320.dp, 63.dp)
                        .padding(start = 10.dp, top = 3.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "On Going Projects:",
                        color = Color.Black,
                        fontFamily = afacad,
                        modifier = Modifier.align(Alignment.TopStart)
                    )
                    Text(
                        text = " _ _      _ _     _ _     _ _ ",
                        color = Color.Black,
                        fontFamily = afacad,
                        fontSize = 10.sp,
                        modifier = Modifier
                            .padding(bottom = 10.dp)
                            .align(Alignment.BottomStart)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tracked Hours
            Card(
                modifier = Modifier
                    .size(320.dp, 350.dp)
                    .clickable { navController.navigate("TimesheetScreen") }
                    .alpha(alpha3.value)
                    .scale(scale3.value)
                    .background(brush = gradientTrackedHours, shape = RoundedCornerShape(8.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box {
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 10.dp, top = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Tracked Hours", fontFamily = afacad, color = Color.Black)
                        Image(
                            painter = painterResource(id = R.drawable.clock),
                            contentDescription = "Clock Icon",
                            colorFilter = ColorFilter.tint(Color(0xFF10161F)),
                            modifier = Modifier
                                .size(24.dp)
                                .padding(start = 4.dp)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .padding(start = 10.dp, top = 30.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        val hours = listOf(0, 2, 4, 6, 8)
                        val days = listOf("M", "T", "W", "Th", "F")
                        val totalSeconds = 60f

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            hours.forEach { hour ->
                                Text(text = "$hour", fontFamily = afacad, fontSize = 11.sp, color = Color.Black)
                            }
                        }

                        Column {
                            days.forEach { day ->
                                val workedSeconds = (trackedHours[day] ?: 0f) * totalSeconds
                                val progressFraction = workedSeconds / totalSeconds
                                val barWidth = (progressFraction * 220f).dp

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = day,
                                        fontFamily = afacad,
                                        fontSize = 11.sp,
                                        color = if (day == currentDay) Color.Red else Color.Black,
                                        fontWeight = if (day == currentDay) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.width(24.dp)
                                    )

                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(12.dp)
                                            .background(Color.LightGray)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .width(if (day == currentDay) barWidth else 0.dp)
                                                .fillMaxHeight()
                                                .background(Color.Blue)
                                        )
                                    }
                                }
                            }
                        }

                        Text(
                            text = "________________________________________",
                            fontFamily = afacad,
                            color = Color.LightGray,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = formattedWorkedHours,
                                    fontFamily = poppinsextrabold,
                                    fontSize = 11.sp,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Hours Worked",
                                    fontFamily = poppins,
                                    fontSize = 8.sp,
                                    color = Color.Black
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = formattedOvertime,
                                    fontFamily = poppinsextrabold,
                                    fontSize = 11.sp,
                                    color = Color.Black
                                )
                                Text(
                                    text = "Overtime",
                                    fontFamily = poppins,
                                    fontSize = 8.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Attendance
            Card(
                modifier = Modifier
                    .size(320.dp, 337.dp)
                    .alpha(alpha4.value)
                    .scale(scale4.value)
                    .background(Color.Transparent),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 10.dp, top = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Attendance", fontFamily = afacad, color = Color.White)
                        Image(
                            painter = painterResource(id = R.drawable.calendar),
                            contentDescription = "Calendar Icon",
                            colorFilter = ColorFilter.tint(Color(0xFFFFFFFF)),
                            modifier = Modifier
                                .size(24.dp)
                                .padding(start = 4.dp)
                        )
                    }

                    Column(modifier = Modifier.fillMaxSize().padding(top = 40.dp)) {
                        var showDatePicker by remember { mutableStateOf(false) }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF666666))
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Location",
                                fontFamily = poppins,
                                fontSize = 11.sp,
                                color = Color.White,
                                modifier = Modifier.weight(1f)
                            )

                            Text(
                                text = "Date",
                                fontFamily = poppins,
                                fontSize = 11.sp,
                                color = Color.White,
                                modifier = Modifier.weight(1f)
                            )

                            Row(
                                modifier = Modifier.weight(2f),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Time In",
                                    fontFamily = poppins,
                                    fontSize = 11.sp,
                                    color = Color.White
                                )
                                Text(
                                    text = "Time Out",
                                    fontFamily = poppins,
                                    fontSize = 11.sp,
                                    color = Color.White,
                                    modifier = Modifier.padding(end = 15.dp)
                                )
                            }
                        }

                        // Comment out API-related code
                        // val attendanceLogs by profileViewModel.attendanceLogs.collectAsState()
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val timeFormat = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())

                        // Create placeholder data
                        val placeholderLogs = (1..30).map { day ->
                            Triple(
                                "Davao City",
                                dateFormat.format(Calendar.getInstance().apply {
                                    set(2025, Calendar.MARCH, 11 + day - 1)
                                }.time),
                                Triple("8:00:00 AM", "5:00:00 PM", "WholeDay")
                            )
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            // Take only the last 5 logs from placeholders
                            placeholderLogs.takeLast(5).forEach { log ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = log.first, // Location
                                        fontFamily = afacad,
                                        color = Color.White,
                                        modifier = Modifier.weight(1f),
                                        fontSize = 11.sp
                                    )

                                    Text(
                                        text = log.second, // Date
                                        fontFamily = afacad,
                                        color = Color.White,
                                        modifier = Modifier.weight(1f),
                                        fontSize = 11.sp
                                    )

                                    Text(
                                        text = log.third.first, // Time In
                                        fontFamily = afacad,
                                        color = Color.White,
                                        modifier = Modifier.weight(1f),
                                        fontSize = 11.sp
                                    )

                                    Text(
                                        text = log.third.second, // Time Out
                                        fontFamily = afacad,
                                        color = Color.White,
                                        modifier = Modifier.weight(1f),
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }

                    FloatingActionButton(
                        onClick = { navController.navigate("AttendanceScreen") },
                        containerColor = Color(0xFFFFFFFFF),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .size(320.dp, 41.dp)
                    ) {
                        Text(
                            text = "Show Attendance",
                            color = Color.Black,
                            fontFamily = afacad,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

fun getCurrentDay(): String {
    val calendar = Calendar.getInstance()
    return when (calendar.get(Calendar.DAY_OF_WEEK)) {
        Calendar.MONDAY -> "M"
        Calendar.TUESDAY -> "T"
        Calendar.WEDNESDAY -> "W"
        Calendar.THURSDAY -> "Th"
        Calendar.FRIDAY -> "F"
        else -> ""
    }
}









