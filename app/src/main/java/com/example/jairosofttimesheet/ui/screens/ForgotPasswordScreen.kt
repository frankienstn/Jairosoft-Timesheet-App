package com.example.jairosofttimesheet.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jairosofttimesheet.R
import androidx.compose.ui.platform.LocalContext

@Composable
fun ForgotPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF10161F))
            .padding(horizontal = 32.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier
                    .size(32.dp)
                    .clickable { navController.popBackStack() },
                tint = Color.White
            )
        }

        Spacer(modifier = Modifier.height(200.dp))

        Text(
            text = "Forgot password?",
            fontSize = 24.sp,
            color = Color.White,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Enter your email to receive a confirmation email",
            fontSize = 14.sp,
            color = Color.White,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 12.dp),
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.ic_email), contentDescription = "Email Icon")
            },
            placeholder = { Text("Enter email") }
        )

        Spacer(modifier = Modifier.height(20.dp))


        Button(
            onClick = {
                Toast.makeText(context, "Email sent!", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Send password reset email")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    val navController = rememberNavController()
    ForgotPasswordScreen(navController)
}
