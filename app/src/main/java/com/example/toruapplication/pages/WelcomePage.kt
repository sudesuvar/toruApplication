package com.example.toruapplication.pages

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.toruapplication.R
import com.example.toruapplication.Routes


@Composable
fun WelcomePage(navController: NavController){
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(200.dp) // Logo boyutu ayarlandı
                    .padding(bottom = 32.dp)
            )



            Text(
                text = "Welcome to Toro",
                color = Color.Black,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Login Butonu
            Button(
                onClick = {navController.navigate(Routes.LoginPage)  },
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.Primary)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "Login",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(24.dp)) // Butonlar arası boşluk artırıldı

            // Sign Up Butonu
            Button(
                onClick = {navController.navigate(Routes.SignupPage)},
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.Primary)),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(50.dp)
            ) {
                Text(
                    text = "Sign Up",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

}