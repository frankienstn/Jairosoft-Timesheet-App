package com.example.jairosofttimesheet.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jairosofttimesheet.R
import android.widget.Toast
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jairosofttimesheet.viewmodel.LoginViewModel

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberMeChecked by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val viewModel: LoginViewModel = viewModel()
    val loginSuccess by viewModel.loginSuccess.collectAsState()
    val loginMessage by viewModel.loginMessage.collectAsState()

    // Check for auto-login when the screen is created
    LaunchedEffect(Unit) {
        if (viewModel.checkAutoLogin(context)) {
            navController.navigate("NavigationScreen") {
                popUpTo("loginScreen") { inclusive = true }
            }
        }
    }

    // Handle login success
    LaunchedEffect(loginSuccess) {
        if (loginSuccess) {
            navController.navigate("NavigationScreen") {
                popUpTo("loginScreen") { inclusive = true }
            }
        }
    }

    // font
    val afacad = FontFamily(
        Font(R.font.afacad, FontWeight.Normal),
    )

    val afacadExtraBold = FontFamily(
        Font(R.font.afacad, FontWeight.ExtraBold)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF10161F))
            .padding(horizontal = 32.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Logo
        Image(
            painter = painterResource(id = R.drawable.timesheetapplogopng),
            contentDescription = "Jairosoft Logo",
            modifier = Modifier
                .size(188.dp)
                .padding(bottom = 32.dp)
        )

        // Email Input
        Text(
            text = "Email",
            fontSize = 18.sp,
            fontFamily = afacadExtraBold,
            color = Color(0xFFFFFFFF),
            modifier = Modifier.align(Alignment.Start)
        )
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 12.dp),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_email),
                    contentDescription = "Email Icon"
                )
            },
            placeholder = { Text("Enter email", fontFamily = afacad) }
        )

        // Password Input
        Text(
            text = "Password",
            fontSize = 18.sp,
            fontFamily = afacadExtraBold,
            color = Color(0xFFFFFFFF),
            modifier = Modifier.align(Alignment.Start)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 12.dp),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_password),
                    contentDescription = "Password Icon"
                )
            },
            trailingIcon = {
                val icon = if (passwordVisible) R.drawable.ic_eye else R.drawable.ic_eye_off
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(painter = painterResource(id = icon), contentDescription = "Toggle Password")
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            placeholder = { Text("Enter account password", fontFamily = afacad) }
        )

        // Remember Me & Forgot Password Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = rememberMeChecked,
                    onCheckedChange = { rememberMeChecked = it }
                )
                Text(
                    "Remember me",
                    fontSize = 14.sp,
                    fontFamily = afacad,
                    color = Color(0xFFFFFFFF)
                )
            }
            TextButton(onClick = { navController.navigate("forgotPasswordScreen") }) {
                Text("Forgot Password?", fontSize = 14.sp, fontFamily = afacad)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Login Button
        Button(
            onClick = {
                // Comment out API-related code
                // viewModel.login(email, password, rememberMeChecked, context)
                navController.navigate("ProfileAnalyticsScreen")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Login", fontSize = 16.sp, fontFamily = afacad)
        }

        Spacer(modifier = Modifier.height(150.dp))

        loginMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    LoginScreen(navController)
}