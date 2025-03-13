package com.example.jairosofttimesheet.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jairosofttimesheet.R
import com.example.jairosofttimesheet.viewmodel.AttendanceViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(viewModel: AttendanceViewModel = viewModel()) {

    val locationText = "Davao City" // Placeholder for location

    // Font setup
    val afacad = FontFamily(Font(R.font.afacad, FontWeight.Normal))
    val poppins = FontFamily(Font(R.font.poppinsregular, FontWeight.Normal))

    // Collect state from ViewModel
    val isClockedIn by viewModel.isClockedIn.collectAsState()

    // Fake attendance list with placeholder data
    val fakeAttendanceList = remember {
        mutableStateListOf(
            Triple("01/15/2025", "08:00 AM", "05:00 PM"),
            Triple("02/01/2025", "08:00 AM", "05:00 PM"),
            Triple("03/10/2025", "08:00 AM", "05:00 PM"),
            Triple("03/20/2025", "08:00 AM", "05:00 PM"),
            Triple("03/22/2025", "08:00 AM", "05:00 PM"),
            Triple("01/20/2025", "08:00 AM", "05:00 PM"),
            Triple("01/25/2025", "08:00 AM", "05:00 PM"),
            Triple("02/10/2025", "08:00 AM", "05:00 PM"),
            Triple("02/14/2025", "08:00 AM", "05:00 PM"),
            Triple("02/18/2025", "08:00 AM", "05:00 PM"),
            Triple("03/01/2025", "08:00 AM", "05:00 PM"),
            Triple("03/05/2025", "08:00 AM", "05:00 PM"),
            Triple("03/08/2025", "08:00 AM", "05:00 PM"),
            Triple("03/12/2025", "08:00 AM", "05:00 PM"),
            Triple("03/15/2025", "08:00 AM", "05:00 PM"),
            Triple("03/18/2025", "08:00 AM", "05:00 PM"),
            Triple("03/21/2025", "08:00 AM", "05:00 PM"),
            Triple("03/23/2025", "08:00 AM", "05:00 PM"),
            Triple("03/26/2025", "08:00 AM", "05:00 PM"),
            Triple("03/29/2025", "08:00 AM", "05:00 PM"),
            Triple("03/12/2025", "08:00 AM", "05:00 PM"),
            Triple("03/15/2025", "08:00 AM", "05:00 PM"),
            Triple("03/18/2025", "08:00 AM", "05:00 PM"),
            Triple("03/21/2025", "08:00 AM", "05:00 PM"),
            Triple("03/23/2025", "08:00 AM", "05:00 PM"),
            Triple("03/26/2025", "08:00 AM", "05:00 PM"),
            Triple("03/29/2025", "08:00 AM", "05:00 PM"),
        )
    }

    // For adding attendance and updating time out
    LaunchedEffect(isClockedIn) {
        if (isClockedIn) {
            val date = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Date()) // Placeholder date
            val timeIn = "08:00 AM" // Placeholder time in
            fakeAttendanceList.add(Triple(date, timeIn, "--")) // Add a new record with placeholder data
        } else if (fakeAttendanceList.isNotEmpty() && fakeAttendanceList.last().third == "--") {
            val timeOut = "05:00 PM" // Placeholder time out
            val lastIndex = fakeAttendanceList.lastIndex
            fakeAttendanceList[lastIndex] = fakeAttendanceList[lastIndex].copy(third = timeOut) // Update last record with time out
        }
    }

    Card(
        modifier = Modifier
            .width(669.dp)
            .height(800.dp)
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF203859))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Header Row
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

                // Date and Time Row
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
                        modifier = Modifier
                            .weight(1f) // Takes 1 fraction of the space
                    )

                    Row(
                        modifier = Modifier
                            .clickable { showDatePicker = true }
                            .weight(1f), // Takes 1 fraction of the space
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Date",
                            fontFamily = poppins,
                            fontSize = 11.sp,
                            color = Color.White
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown Arrow",
                            tint = Color.White,
                            modifier = Modifier
                                .padding(start = 4.dp)
                        )
                    }

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
                            color = Color.White,
                            modifier = Modifier.padding(end = 15.dp)
                        )
                    }
                }

                // Attendance List Scrollable Section
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .weight(1f)
                ) {
                    fakeAttendanceList.forEach { (date, timeIn, timeOut) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                                .height(IntrinsicSize.Min),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = locationText.take(10) + if (locationText.length > 10) "..." else "",
                                fontFamily = afacad,
                                color = Color.White,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 5.dp),
                                fontSize = 11.sp
                            )

                            Text(
                                text = date, // Placeholder for date
                                fontFamily = afacad,
                                color = Color.White,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 5.dp),
                                fontSize = 11.sp
                            )

                            Text(
                                text = timeIn, // Placeholder for time in
                                fontFamily = afacad,
                                color = Color.White,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 5.dp),
                                fontSize = 11.sp
                            )

                            Text(
                                text = if (timeOut == "--") " -- " else timeOut, // Placeholder for time out
                                fontFamily = afacad,
                                color = Color.White,
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 5.dp, end = 6.dp)
                                    .run {
                                        if (timeOut == "--") {
                                            this.then(Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp))
                                        } else {
                                            this
                                        }
                                    },
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
