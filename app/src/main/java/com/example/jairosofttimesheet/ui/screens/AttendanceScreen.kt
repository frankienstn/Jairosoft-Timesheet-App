package com.example.jairosofttimesheet.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jairosofttimesheet.R
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen() {
    val locationText = "Unknown Location"

    var isClockedIn by remember { mutableStateOf(false) }

    //font
    val afacad = FontFamily(
        Font(R.font.afacad, FontWeight.Normal),
    )

    val poppins = FontFamily(
        Font(R.font.poppinsregular, FontWeight.Normal)
    )

    Card(
        modifier = Modifier
            .size(366.dp, 300.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF203859)
        )
    ) {
        Box {
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

                Spacer(modifier = Modifier.weight(1f))

                Image(
                    painter = painterResource(id = R.drawable.download),
                    contentDescription = "Download",
                    modifier = Modifier
                        .size(24.dp)
                        .padding(start = 8.dp)
                        .clickable {

                        }
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
        }
    }
}

