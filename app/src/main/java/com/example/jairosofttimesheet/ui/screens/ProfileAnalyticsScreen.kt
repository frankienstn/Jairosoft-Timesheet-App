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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.jairosofttimesheet.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.text.style.TextAlign
import com.example.jairosofttimesheet.ui.theme.JairosoftTimesheetTheme
import com.example.jairosofttimesheet.ui.theme.gradientDBlue
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import android.os.Looper
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
import androidx.navigation.NavHostController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jairosofttimesheet.viewmodel.ProfileViewModel
import java.io.IOException
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ProfileAnalyticsScreen(navController: NavController) {

    var isClockedIn by remember { mutableStateOf(false) }
    var timeCounter by remember { mutableLongStateOf(0L) }
    val userName by remember { mutableStateOf("User") }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    //location
    var showLocationDialog by remember { mutableStateOf(false) }

    // animation states
    var isLogoutClicked by remember { mutableStateOf(false) }
    var isButtonClicked by remember { mutableStateOf(false) }

    val trackedHours by remember { mutableIntStateOf(0) }

    val logoutScale by animateFloatAsState(
        if (isLogoutClicked) 1.1f else 1f,
        label = "LogoutScale"
    )
    val buttonScale by animateFloatAsState(
        if (isButtonClicked) 1.1f else 1f,
        label = "ButtonScale"
    )

    // Location permissions
    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    // Request permissions if not granted
    if (permissionState.status != PermissionStatus.Granted) {
        LaunchedEffect(Unit) {
            permissionState.launchPermissionRequest()
        }
    }

    // Location request and callback
    val locationRequest = LocationRequest.create().apply {
        priority = Priority.PRIORITY_HIGH_ACCURACY
        interval = 5000
    }

    var userLocation by remember { mutableStateOf(LatLng(0.0, 0.0)) }
    var locationText by remember { mutableStateOf("Fetching location...") } // Separate text variable

    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                userLocation = LatLng(location.latitude, location.longitude)
                val geocoder = Geocoder(context, Locale.getDefault())
                try {
                    val addressList = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    if (!addressList.isNullOrEmpty()) {
                        val address = addressList[0]
                        locationText = address.locality ?: address.subAdminArea ?: "Unknown Location"
                    }
                } catch (e: IOException) {
                    locationText = "Unable to get location name"
                }
            }
        }
    }

    LaunchedEffect(permissionState.status) {
        if (permissionState.status == PermissionStatus.Granted) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            )
        } else {
            locationText = "Permission denied"
        }
    }



    LaunchedEffect(permissionState.status) {
        if (permissionState.status == PermissionStatus.Granted) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            )
        } else {
            locationText = "Permission denied"
        }
    }

    //font
    val afacad = FontFamily(
        Font(R.font.afacad, FontWeight.Normal),
    )
    val afacadmedium = FontFamily(
        Font(R.font.afacadmedium),
    )
    val montserratextrabold = FontFamily(
        Font(R.font.montserratextrabold)
    )
    val inter = FontFamily(
        Font(R.font.inter, FontWeight.Normal)
    )

    val poppins = FontFamily(
        Font(R.font.poppinsregular, FontWeight.Normal)
    )

    val poppinsextrabold = FontFamily(
        Font(R.font.poppinsextrabold, FontWeight.Normal)
    )

    LaunchedEffect(isClockedIn) {
        while (isClockedIn) {
            delay(1000)
            timeCounter++
        }
    }

    var formattedTime = String.format(
        "%02d:%02d:%02d",
        TimeUnit.SECONDS.toHours(timeCounter),
        TimeUnit.SECONDS.toMinutes(timeCounter) % 60,
        timeCounter % 60
    )

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
                        text = "Hi, User",
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
                    Button(
                        onClick = {
                            if (isClockedIn) {
                                showLocationDialog = true
                                isButtonClicked = true
                            } else {
                                showLocationDialog = true
                                isButtonClicked = true
                            }
                            coroutineScope.launch {
                                delay(300)
                                isButtonClicked = false
                            }
                        },
                        modifier = Modifier
                            .size(120.dp, 35.dp)
                            .scale(buttonScale),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
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

                // Determine dialog content before showing the dialog
                val dialogTitle = if (isClockedIn) "Confirm Clock-Out" else "Confirm Clock-In"
                val dialogText =
                    if (isClockedIn) "Are you sure you want to clock out?" else "Your location has been detected. Do you want to proceed with clocking in?"

                if (showLocationDialog) {
                    AlertDialog(
                        onDismissRequest = { showLocationDialog = false },
                        confirmButton = {
                            TextButton(onClick = {
                                showLocationDialog = false
                                if (isClockedIn) {
                                    isClockedIn = false
                                    timeCounter = 0L
                                } else {
                                    isClockedIn = true
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
                                            userLocation =
                                                LatLng(location.latitude, location.longitude)
                                            locationText =
                                                "Lat: ${location.latitude}, Lng: ${location.longitude}"
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
                modifier = Modifier.size(366.dp, 50.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(
                        0xFF203859
                    )
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Today is ${
                            SimpleDateFormat(
                                "MM/dd/yyyy, EEEE",
                                Locale.getDefault()
                            ).format(Date())
                        }",
                        color = Color.White,
                        fontSize = 24.sp,
                        fontFamily = afacadmedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // On Going Projects
            Card(
                modifier = Modifier
                    .size(366.dp, 63.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(
                        0xFF89BAFA
                    )
                )
            ) {
                Box(
                    modifier = Modifier
                        .size(366.dp, 63.dp)
                        .padding(start = 10.dp, top = 3.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "On Going Projects:",
                        color = Color.Black,
                        fontFamily = afacad,
                        modifier = Modifier
                            .align(Alignment.TopStart)
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

            //Tracked Hours
            Card(
                modifier = Modifier
                    .size(366.dp, 280.dp)
                    .clickable {
                        navController.navigate("TimesheetScreen")
                    },
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFD9D9D9)
                )
            ) {
                Box {
                    Row(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 10.dp, top = 3.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tracked Hours",
                            fontFamily = afacad,
                            color = Color.Black
                        )
                        Image(
                            painter = painterResource(id = R.drawable.clock),
                            contentDescription = "Clock Icon",
                            colorFilter = ColorFilter.tint(
                                Color(
                                    0xFF10161F
                                )
                            ),
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

                        Column {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                hours.forEach { hour ->
                                    Text(
                                        text = "$hour",
                                        fontFamily = afacad,
                                        fontSize = 11.sp,
                                        color = Color.Black
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .size(20.dp, 110.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxHeight(),
                                    verticalArrangement = Arrangement.SpaceEvenly,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    days.forEach { day ->
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .width(24.dp)
                                                    .height((trackedHours * 30).dp)
                                                    .background(Color.Blue)
                                            )
                                            Text(
                                                text = day,
                                                fontFamily = afacad,
                                                fontSize = 11.sp,
                                                color = Color.Black
                                            )
                                        }
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
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                        ) {

                            //change this so that when the user clicks the Clock In button it will register as bars for Hours Worked
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "00h and 00m",
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

                            //change this so that when the user clicks the Clock In button it will register as bars for Overtime
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "00h and 00m",
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
                    .size(366.dp, 337.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF203859)
                )
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
                            colorFilter = ColorFilter.tint(
                                Color(0xFFFFFFFF)
                            ),
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
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Date",
                                fontFamily = poppins,
                                fontSize = 11.sp,
                                color = Color.White,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { showDatePicker = true }
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Dropdown Arrow",
                                tint = Color.White,
                                modifier = Modifier.clickable { showDatePicker = true }
                            )

                            if (showDatePicker) {
                                DatePickerDialog(
                                    onDismissRequest = { showDatePicker = false },
                                    confirmButton = {
                                        TextButton(onClick = { showDatePicker = false }) {
                                            Text("OK")
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(onClick = { showDatePicker = false }) {
                                            Text("Cancel")
                                        }
                                    }
                                ) {
                                    DatePicker(state = rememberDatePickerState())
                                }
                            }

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
                                    color = Color.White
                                )
                            }
                        }

                        val attendanceList =
                            remember { mutableStateListOf<Triple<String, String, String>>() }

                        LaunchedEffect(isClockedIn) {
                            if (isClockedIn) {
                                val date = SimpleDateFormat(
                                    "MM/dd/yyyy",
                                    Locale.getDefault()
                                ).format(Date())
                                val timeIn = SimpleDateFormat(
                                    "hh:mm:ss a",
                                    Locale.getDefault()
                                ).format(Date())
                                attendanceList.add(Triple(date, timeIn, "--"))
                            } else if (attendanceList.isNotEmpty() && attendanceList.last().third == "--") {
                                val timeOut = SimpleDateFormat(
                                    "hh:mm:ss a",
                                    Locale.getDefault()
                                ).format(Date())
                                val lastIndex = attendanceList.lastIndex
                                attendanceList[lastIndex] =
                                    attendanceList[lastIndex].copy(third = timeOut)
                            }
                        }

                        Column(
                            modifier = Modifier.verticalScroll(rememberScrollState())
                                .weight(1f)
                        ) {
                            attendanceList.forEach { (date, timeIn, timeOut) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Location at the start of the row with 10 character limit
                                    Text(
                                        text = locationText.take(10) + if (locationText.length > 10) "..." else "",
                                        fontFamily = afacad,
                                        color = Color.White,
                                        modifier = Modifier.weight(1f),
                                        fontSize = 11.sp // Uniform font size
                                    )
                                    Row(
                                        modifier = Modifier.weight(2f),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = date,
                                            fontFamily = afacad,
                                            color = Color.White,
                                            fontSize = 11.sp // Uniform font size
                                        )
                                        Text(
                                            text = timeIn,
                                            fontFamily = afacad,
                                            color = Color.White,
                                            fontSize = 11.sp // Uniform font size
                                        )
                                        Text(
                                            text = if (timeOut == "--") {
                                                " -- " // Leave empty or show something special
                                            } else {
                                                timeOut
                                            },
                                            fontFamily = afacad,
                                            color = Color.White,
                                            fontSize = 11.sp // Uniform font size
                                        )
                                    }
                                }
                            }
                        }
                    }

                    FloatingActionButton(
                        onClick = {
                            navController.navigate("AttendanceScreen")
                        },
                        containerColor = Color(0xFFFFFFFFF),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .size(332.dp, 41.dp)
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



