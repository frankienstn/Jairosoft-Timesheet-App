package com.example.jairosofttimesheet.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jairosofttimesheet.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

@Composable
fun ProfileAnalyticsScreen(navController: NavController) {
    var isClockedIn by remember { mutableStateOf(false) }
    var timeCounter by remember { mutableLongStateOf(0L) }
    var userName by remember { mutableStateOf("User") }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    // animation states
    var isLogoutClicked by remember { mutableStateOf(false) }
    var isButtonClicked by remember { mutableStateOf(false) }
    var isHomeClicked by remember { mutableStateOf(false) }
    //
    val logoutScale by animateFloatAsState(if (isLogoutClicked) 1.1f else 1f, label = "LogoutScale")
    val buttonScale by animateFloatAsState(if (isButtonClicked) 1.1f else 1f, label = "ButtonScale")
    val homeScale by animateFloatAsState(if (isHomeClicked) 1.1f else 1f, label = "HomeScale")

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

    LaunchedEffect(isClockedIn) {
        if (isClockedIn) {
            while (true) {
                delay(1000)
                timeCounter++
            }
        } else {
            timeCounter = 0L
        }
    }

    val formattedTime = String.format(
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
                .background(Color(0xFF10161F)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .width(488.dp)
                    .height(48.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.timesheetappbanner),
                    contentDescription = "Jairosoft Logo Banner",
                    modifier = Modifier.size(158.dp, 48.dp),
                    contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(id = R.drawable.logout),
                    contentDescription = "Logout",
                    modifier = Modifier
                        .size(48.dp, 49.dp)
                        .padding(8.dp)
                        .clickable {
                            isLogoutClicked = true
                            navController.navigate("LoginAndSignUpScreen")
                            coroutineScope.launch {
                                delay(300)
                                isLogoutClicked = false
                            }
                        }
                        .scale(logoutScale)
                )
            }

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
                        text = "Hi, $userName \uD83D\uDC4B",
                        color = Color(0xFF2E7BE1),
                        fontSize = 30.sp,
                        fontFamily = montserratextrabold
                    )
                    Text(text = "Happy Working!",
                        color = Color(0xFF888888),
                        fontSize = 12.sp,
                        fontFamily = inter
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            isButtonClicked = true
                            isClockedIn = !isClockedIn
                            coroutineScope.launch {
                                delay(300)
                                isButtonClicked = false
                            }
                        },
                        modifier = Modifier
                            .size(120.dp, 35.dp)
                            .scale(buttonScale),
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = if (isClockedIn) Color(0xFFDC3545) else Color(0xFF28A745)
                        )
                    ) {
                        Text(text = if (isClockedIn) "Clock Out" else "Clock In",
                            fontFamily = afacad)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    //Running Time (Formatted as: hh:mm:ss)
                    Card(modifier = Modifier.size(120.dp, 35.dp)) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = formattedTime,
                                fontFamily = afacad)
                        }
                    }
                }
            }

            // Date Today
            Card(
                modifier = Modifier.size(366.dp, 63.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = Color(
                        0xFF203859
                    )
                )
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center) {
                    Text(
                        text = "Today is ${
                            java.text.SimpleDateFormat(
                                "MM/dd/yyyy, EEEE",
                                java.util.Locale.getDefault()
                            ).format(java.util.Date())
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
                    .fillMaxSize()
                    .size(366.dp, 62.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = Color(
                        0xFF89BAFA
                    )
                )
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "On Going Projects: N/A",
                        color = Color.Black,
                        fontFamily = afacad)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Tracked Hours
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .size(366.dp, 156.dp),
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = Color(
                        0xFFD9D9D9
                    )
                )
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "Tracked Hours",
                        fontFamily = afacad)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //Attendance
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .size(500.dp, 337.dp), //it should be 365dp i changed it to 500 for testing
                colors = androidx.compose.material3.CardDefaults.cardColors(
                    containerColor = Color(
                        0xFF203859
                    )
                )
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "Attendance Log",
                        fontFamily = afacad)
                }
            }
        }

        // Navigations
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color(0xFF10161F))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Timesheet
            Image(
                painter = painterResource(id = R.drawable.timesheet),
                contentDescription = "Timesheet"
            )

            // Home with Scroll to Top
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = "Home",
                    modifier = Modifier
                        .clickable {
                            isHomeClicked = true
                            coroutineScope.launch {
                                scrollState.animateScrollTo(0)
                                delay(300)
                                isHomeClicked = false
                            }
                        }
                        .scale(homeScale)
                )
                Image(
                    painter = painterResource(id = R.drawable.officialreddot),
                    contentDescription = "Selected",
                    modifier = Modifier.size(8.dp),
                    colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color(0XFFFF4A4A))
                )
            }


            // Attendance
            Image(
                painter = painterResource(id = R.drawable.attendance),
                contentDescription = "Attendance"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileAnalyticsScreenPreview() {
    val navController = rememberNavController()
    ProfileAnalyticsScreen(navController)
}