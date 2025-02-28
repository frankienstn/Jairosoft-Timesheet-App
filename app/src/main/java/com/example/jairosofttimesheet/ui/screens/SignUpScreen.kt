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
fun SignUpScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    //font
    val afacad = FontFamily(
        Font(R.font.afacad, FontWeight.Normal)
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
        Text(text = "Email", color = Color(0xFFFFFFFF), fontSize = 18.sp, fontFamily = afacadExtraBold, modifier = Modifier
            .align(Alignment.Start))
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
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
            onValueChange = { password = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 12.dp),
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.ic_password), contentDescription = "Password Icon")
            },
            visualTransformation = PasswordVisualTransformation(),
            placeholder = { Text("Enter account password", fontFamily = afacad) }
        )

        // Confirm Password Input
        Text(text = "Confirm Password", fontSize = 18.sp, fontFamily = afacadExtraBold, color = Color(0xFFFFFFFF), modifier = Modifier.align(Alignment.Start))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, bottom = 12.dp),
            leadingIcon = {
                Icon(painter = painterResource(id = R.drawable.ic_password), contentDescription = "Password Icon")
            },
            visualTransformation = PasswordVisualTransformation(),
            placeholder = { Text("Enter account password", fontFamily = afacad) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Sign Up Button
        Button(
            onClick = { navController.navigate("ProfileAnalyticsScreen") },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Sign Up", fontSize = 16.sp, fontFamily = afacad)
        }

        Spacer(modifier = Modifier.height(100.dp))

        // Already have an account? Login (INLINE)
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Already have an account?",
                fontSize = 14.sp,
                color = Color(0xFFFFFFFF),
                fontFamily = afacad
            )
            Spacer(modifier = Modifier.width(4.dp)) // Adjust spacing between text and button
            TextButton(onClick = { navController.navigate("LoginScreen") }) {
                Text("Login", fontSize = 14.sp, fontFamily = afacad, color = Color.Blue)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    val navController = rememberNavController()
    SignUpScreen(navController)
}
