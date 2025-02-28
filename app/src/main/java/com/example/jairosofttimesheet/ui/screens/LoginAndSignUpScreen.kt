package com.example.jairosofttimesheet.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.jairosofttimesheet.R


@Composable
fun LoginAndSignUpScreen(navController: NavHostController) {

    //font
    val afacad = FontFamily(
        Font(R.font.afacad, FontWeight.SemiBold)
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF10161F))
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.timesheetapplogopng), // Replace with actual image
            contentDescription = "Jairosoft Logo",
            modifier = Modifier
                .size(188.dp)
                .padding(bottom = 32.dp)
                .height(1000.dp)
                .width(1000.dp),
            contentScale = ContentScale.Crop
        )
        // Login Button
        Button(
            onClick = { navController.navigate("LoginScreen") }, // Navigate to login
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Login", fontSize = 16.sp, fontFamily = afacad)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign Up Button
        OutlinedButton(
            onClick = { navController.navigate("SignUpScreen")},
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Sign Up", fontSize = 16.sp, fontFamily = afacad)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginAndSignUpScreenScreenPreview() {
    val navController = rememberNavController()
    LoginAndSignUpScreen(navController)
}
