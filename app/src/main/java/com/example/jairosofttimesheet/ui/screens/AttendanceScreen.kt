package com.example.jairosofttimesheet.ui.screens

import android.content.ContentValues
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jairosofttimesheet.R
import com.example.jairosofttimesheet.viewmodel.AttendanceViewModel
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.viewmodel.compose.viewModel
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.createSavedStateHandle
import com.example.jairosofttimesheet.viewmodel.AttendanceViewModelFactory
import com.example.jairosofttimesheet.data.remote.RetrofitClient
import com.example.jairosofttimesheet.data.repository.Repository
import com.example.jairosofttimesheet.ui.theme.gradientDBlue
import java.io.OutputStream
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember



@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen() {

    val factory = AttendanceViewModelFactory(
        Repository(RetrofitClient.authApiService, RetrofitClient.attendanceApiService)
    )
    val viewModel: AttendanceViewModel = viewModel(factory = factory)

    val logs by viewModel.attendanceLogs.observeAsState(emptyList())
    val isClockedIn by viewModel.isClockedIn.collectAsState()


    // Font setup
    val afacad = FontFamily(Font(R.font.afacad, FontWeight.Normal))
    val poppins = FontFamily(Font(R.font.poppinsregular, FontWeight.Normal))

    //for download context
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.fetchAttendanceLogs()
    }

    Box(modifier = Modifier.fillMaxSize().background(gradientDBlue)) {

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
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .size(24.dp)
                    .padding(start = 4.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.download),
                contentDescription = "Download Attendance",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

                        saveAttendanceToDownloads(
                            logs.map {

                                Triple(
                                    dateFormat.format(Date((it.date ?: 0L) * 1000)),
                                    dateFormat.format(Date((it.timeIn ?: 0L) * 1000)),
                                    dateFormat.format(Date((it.timeOut ?: 0L) * 1000))
                                )
                            },
                            context
                        )
                    }
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
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val timeFormat = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(logs) { log ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = dateFormat.format(Date((log.date ?: 0L) * 1000L)),
                            fontFamily = afacad,
                            color = Color.White,
                            fontSize = 11.sp
                        )
                        Text(
                            text = timeFormat.format(Date((log.timeIn ?: 0L) * 1000L)),
                            fontFamily = afacad,
                            color = Color.White,
                            fontSize = 11.sp
                        )
                        Text(
                            text = timeFormat.format(Date((log.timeOut ?: 0L) * 1000L)),
                            fontFamily = afacad,
                            color = Color.White,
                            fontSize = 11.sp
                        )
                    }
                }
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
fun saveAttendanceToDownloads(attendanceList: List<Triple<String, String, String>>, context: Context) {
    val fileName = "Attendance_${System.currentTimeMillis()}.txt"
    val fileContents = buildString {
        append("Attendance Record\n\n")
        append("Date\t\tTime In\t\tTime Out\n")
        append("=================================\n")
        attendanceList.forEach { (date, timeIn, timeOut) ->
            append("$date\t$timeIn\t$timeOut\n")
        }
    }

    try {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "text/plain")
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            resolver.openOutputStream(it)?.use { outputStream: OutputStream ->
                outputStream.write(fileContents.toByteArray())
            }
            Toast.makeText(context, "File saved to Downloads", Toast.LENGTH_LONG).show()
        } ?: Toast.makeText(context, "Failed to save file", Toast.LENGTH_LONG).show()

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error saving file", Toast.LENGTH_LONG).show()
    }
}






