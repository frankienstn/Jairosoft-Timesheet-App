package com.example.jairosofttimesheet.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.jairosofttimesheet.R

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") } // Email state
    var password by remember { mutableStateOf("") } // Password state
    var rememberMeChecked by remember { mutableStateOf(false) } // Checkbox state

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
            painter = painterResource(id = R.drawable.timesheetapplogopng), // Replace with actual image
            contentDescription = "Jairosoft Logo",
            modifier = Modifier
                .size(188.dp)
                .padding(bottom = 32.dp)
        )

        // Email Input
        Text(text = "Email", fontSize = 18.sp, fontFamily = afacadExtraBold, color = Color(0xFFFFFFFF), modifier = Modifier.align(Alignment.Start))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it }, // Update state on input
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 12.dp),
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.ic_email), contentDescription = "Email Icon")
            },
            placeholder = { Text("Enter email", fontFamily = afacad) }
        )

        // Password Input
        Text(text = "Password", fontSize = 18.sp, fontFamily = afacadExtraBold, color = Color(0xFFFFFFFF), modifier = Modifier.align(Alignment.Start))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it }, // Update state on input
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 12.dp),
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.ic_password), contentDescription = "Password Icon")
            },
            visualTransformation = PasswordVisualTransformation(),
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
                Text("Remember me", fontSize = 14.sp, fontFamily = afacad, color = Color(0xFFFFFFFF))
            }
            TextButton(onClick = { navController.navigate("forgotPasswordScreen") }) {
                Text("Forgot Password?", fontSize = 14.sp, fontFamily = afacad)
            }

        }

        Spacer(modifier = Modifier.height(12.dp))

        // Login Button
        Button(
            onClick = { navController.navigate("NavigationScreen") },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Login", fontSize = 16.sp, fontFamily = afacad)
        }

        Spacer(modifier = Modifier.height(150.dp))

        // Don't have an account? Login (INLINE)
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Don't have an account?",
                fontSize = 14.sp,
                color = Color(0xFFFFFFFF),
                fontFamily = afacad
            )
            Spacer(modifier = Modifier.width(4.dp))
            TextButton(onClick = { navController.navigate("SignUpScreen") }) {
                Text("Sign Up", fontSize = 14.sp, color = Color.Blue, fontFamily = afacad)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    LoginScreen(navController)
}
