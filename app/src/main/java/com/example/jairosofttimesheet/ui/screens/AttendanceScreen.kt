package com.example.jairosofttimesheet.ui.screens

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jairosofttimesheet.R
import com.example.jairosofttimesheet.data.model.Attendance
import com.example.jairosofttimesheet.ui.theme.gradientDBlue
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun AttendanceScreen() {
   // val repository = remember { AttendanceRepository() }
    //val factory = remember { AttendanceViewModelFactory(repository) }
    //val viewModel: AttendanceViewModel = viewModel(factory = factory)


    //val attendanceList by viewModel.attendanceList.collectAsState()
    //val isClockedIn by viewModel.isClockedIn.collectAsState()
    val context = LocalContext.current
    val afacad = FontFamily(Font(R.font.afacad, FontWeight.Normal))
    val poppins = FontFamily(Font(R.font.poppinsregular, FontWeight.Normal))

    var showDatePicker by remember { mutableStateOf(false) }

//    LaunchedEffect(Unit) {
//        val token = "bus|1741329443584x643073812085500400|1742539400610x157152606711386880"
//        viewModel.fetchAttendanceLogs(token)
//    }


    Box(modifier = Modifier.fillMaxSize().background(gradientDBlue)) {
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 10.dp, top = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Attendance", fontFamily = afacad, color = Color.White)
            Image(
                painter = painterResource(id = R.drawable.calendar),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier.size(24.dp).padding(start = 4.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.download),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier.size(24.dp).clickable {
                    saveAttendanceToDownloads(attendanceList, context)
                }
            )
        }

        Column(modifier = Modifier.fillMaxSize().padding(top = 40.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF666666))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Attendance Status", fontFamily = poppins, fontSize = 11.sp, color = Color.White, modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.clickable { showDatePicker = true }.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Date", fontFamily = poppins, fontSize = 11.sp, color = Color.White)
                    Icon(Icons.Default.ArrowDropDown, null, tint = Color.White, modifier = Modifier.padding(start = 4.dp))
                }
                Row(modifier = Modifier.weight(2f), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Time In", fontFamily = poppins, fontSize = 11.sp, color = Color.White)
                    Text("Time Out", fontFamily = poppins, fontSize = 11.sp, color = Color.White, modifier = Modifier.padding(end = 15.dp))
                }
            }

            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()).weight(1f)
            ) {
                attendanceList.forEach { attendance ->
                    val formattedDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(attendance.date))
                    val formattedTimeIn = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(attendance.timeIn))
                    val formattedTimeOut = if (attendance.timeOut == 0L) "--"
                    else SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(attendance.timeOut))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                            .height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(attendance.attendanceStatus, fontFamily = afacad, color = Color.White, modifier = Modifier.weight(1f).padding(end = 5.dp), fontSize = 11.sp)
                        Text(formattedDate, fontFamily = afacad, color = Color.White, modifier = Modifier.weight(1f).padding(end = 5.dp), fontSize = 11.sp)
                        Text(formattedTimeIn, fontFamily = afacad, color = Color.White, modifier = Modifier.weight(1f).padding(end = 5.dp), fontSize = 11.sp)
                        Text(formattedTimeOut, fontFamily = afacad, color = Color.White, modifier = Modifier.weight(1f).padding(end = 5.dp), fontSize = 11.sp)
                    }
                }
            }
        }

        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = { TextButton(onClick = { showDatePicker = false }) { Text("OK") } },
                dismissButton = { TextButton(onClick = { showDatePicker = false }) { Text("Cancel") } }
            ) {
                DatePicker(state = rememberDatePickerState())
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.Q)
fun saveAttendanceToDownloads(attendanceList: List<Attendance>, context: Context) {
    val fileContents = buildString {
        append("Attendance Record\n\n")
        append("Date\t\tTime In\t\tTime Out\t\tAttendance Status\n")
        append("=================================\n")
        attendanceList.forEach { record ->
            append("${record.date}\t${record.timeIn}\t${record.timeOut}\t${record.attendanceStatus}\n")
        }
    }

    try {
        val fileName = "AttendanceRecord_${System.currentTimeMillis()}.txt"
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

