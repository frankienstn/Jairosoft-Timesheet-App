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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.jairosofttimesheet.data.remote.RetrofitClient
import com.example.jairosofttimesheet.data.repository.Repository
import com.example.jairosofttimesheet.ui.theme.gradientDBlue
import com.example.jairosofttimesheet.viewmodel.AttendanceViewModel
import com.example.jairosofttimesheet.viewmodel.AttendanceViewModelFactory
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import com.example.jairosofttimesheet.data.model.AttendanceRecord
import com.example.jairosofttimesheet.data.model.AttendanceLogUI

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen() {

    val factory = AttendanceViewModelFactory(
        Repository(RetrofitClient.authApiService, RetrofitClient.attendanceApiService)
    )
    val viewModel: AttendanceViewModel = viewModel(factory = factory)

    // Comment out API-related code
    // val logs by viewModel.attendanceLogs.observeAsState(emptyList())
    val isClockedIn by viewModel.isClockedIn.collectAsState()

    // Font setup
    val afacad = FontFamily(Font(R.font.afacad, FontWeight.Normal))
    val poppins = FontFamily(Font(R.font.poppinsregular, FontWeight.Normal))

    //for download context
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        // Comment out API-related code
        // viewModel.fetchAttendanceLogs()
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
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        val timeFormat = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())

                        // Create placeholder data for download
                        val placeholderRecords = (1..30).map { day ->
                            AttendanceRecord(
                                location = "Davao City",
                                date = dateFormat.format(Calendar.getInstance().apply {
                                    set(2025, Calendar.MARCH, 11 + day - 1)
                                }.time),
                                timeIn = "8:00:00 AM",
                                timeOut = "5:00:00 PM",
                                status = "WholeDay"
                            )
                        }

                        saveAttendanceToDownloads(placeholderRecords, context)
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
                    modifier = Modifier.weight(0.8f)
                )

                Row(
                    modifier = Modifier
                        .weight(0.8f)
                        .clickable { showDatePicker = true },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = "Date",
                        fontFamily = poppins,
                        fontSize = 11.sp,
                        color = Color.White
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Select Date",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = "Time In",
                    fontFamily = poppins,
                    fontSize = 11.sp,
                    color = Color.White,
                    modifier = Modifier.weight(0.8f)
                )

                Text(
                    text = "Time Out",
                    fontFamily = poppins,
                    fontSize = 11.sp,
                    color = Color.White,
                    modifier = Modifier.weight(0.8f)
                )

                Text(
                    text = "Status",
                    fontFamily = poppins,
                    fontSize = 11.sp,
                    color = Color.White,
                    modifier = Modifier
                        .weight(0.8f)
                        .padding(end = 8.dp)
                )
            }

            if (showDatePicker) {
                val datePickerState = rememberDatePickerState()
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            showDatePicker = false
                            datePickerState.selectedDateMillis?.let { selectedDate ->
                                // viewModel.filterLogsByDate(selectedDate)
                            }
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(
                        state = datePickerState
                    )
                }
            }

            // Attendance List
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val timeFormat = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())

            // Create placeholder data
            val placeholderLogs = (1..30).map { day ->
                AttendanceLogUI(
                    date = Calendar.getInstance().apply {
                        set(2025, Calendar.MARCH, 11 + day - 1)
                    }.timeInMillis,
                    timeIn = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, 8)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                    }.timeInMillis,
                    timeOut = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, 17)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                    }.timeInMillis,
                    status = "WholeDay"
                )
            }

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(placeholderLogs) { log ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .height(IntrinsicSize.Min),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Location column
                        Text(
                            text = "Davao City",
                            fontFamily = afacad,
                            color = Color.White,
                            fontSize = 11.sp,
                            modifier = Modifier.weight(0.8f)
                        )

                        // Date column
                        Text(
                            text = dateFormat.format(Date(log.date)),
                            fontFamily = afacad,
                            color = Color.White,
                            fontSize = 11.sp,
                            modifier = Modifier.weight(0.8f)
                        )

                        // Time In column
                        Text(
                            text = timeFormat.format(Date(log.timeIn)),
                            fontFamily = afacad,
                            color = Color.White,
                            fontSize = 11.sp,
                            modifier = Modifier.weight(0.8f)
                        )

                        // Time Out column
                        Text(
                            text = timeFormat.format(Date(log.timeOut)),
                            fontFamily = afacad,
                            color = Color.White,
                            fontSize = 11.sp,
                            modifier = Modifier.weight(0.8f)
                        )

                        // Status column
                        Text(
                            text = log.status,
                            fontFamily = afacad,
                            color = Color.White,
                            fontSize = 11.sp,
                            modifier = Modifier
                                .weight(0.8f)
                                .padding(end = 8.dp)
                        )
                    }
                }
            }

        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
fun saveAttendanceToDownloads(attendanceList: List<AttendanceRecord>, context: Context) {
    try {
        // Create PDF document in memory first
        val outputStream = ByteArrayOutputStream()
        val pdfWriter = PdfWriter(outputStream)
        val pdfDocument = PdfDocument(pdfWriter)
        val document = Document(pdfDocument)

        // Add title
        val title = Paragraph("Attendance Record")
            .setFontSize(20f)
            .setTextAlignment(TextAlignment.CENTER)
            .setBold()
        document.add(title)
        document.add(Paragraph("\n")) // Add some space

        // Create table
        val table = Table(UnitValue.createPercentArray(floatArrayOf(20f, 20f, 20f, 20f, 20f)))
            .useAllAvailableWidth()

        // Add headers with styling
        val headerColor = DeviceRgb(16, 22, 31) // Dark blue color
        val headerTextColor = DeviceRgb(255, 255, 255) // White color

        arrayOf("Location", "Date", "Time In", "Time Out", "Status").forEach { headerText ->
            table.addHeaderCell(
                Cell().add(Paragraph(headerText))
                    .setBackgroundColor(headerColor)
                    .setFontColor(headerTextColor)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setBold()
            )
        }

        // Add data rows using placeholder data
        attendanceList.forEach { record ->
            arrayOf(record.location, record.date, record.timeIn, record.timeOut, record.status).forEach { text ->
                table.addCell(
                    Cell().add(Paragraph(text))
                        .setTextAlignment(TextAlignment.CENTER)
                )
            }
        }

        document.add(table)
        document.close()

        // Save the PDF file
        val fileName = "Attendance_${System.currentTimeMillis()}.pdf"
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            resolver.openOutputStream(it)?.use { stream ->
                stream.write(outputStream.toByteArray())
            }
            Toast.makeText(context, "PDF saved to Downloads", Toast.LENGTH_LONG).show()
        } ?: Toast.makeText(context, "Failed to save PDF", Toast.LENGTH_LONG).show()

    } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(context, "Error saving PDF: ${e.message}", Toast.LENGTH_LONG).show()
    }
}






