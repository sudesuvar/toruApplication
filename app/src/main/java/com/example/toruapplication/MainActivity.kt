package com.example.toruapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.toruapplication.pages.LoginPage
import com.example.toruapplication.pages.WelcomePage
import com.example.toruapplication.ui.theme.ToruApplicationTheme
import com.example.toruapplication.pages.SignupPage


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToruApplicationTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Routes.WelcomePage, builder = {
                    composable(Routes.LoginPage,) {
                        LoginPage(navController, )
                    }
                    composable(Routes.WelcomePage,) {
                       WelcomePage(navController,)
                    }
                    composable(Routes.SignupPage,) {
                        SignupPage(navController,)
                    }
                })
            }
        }
    }
}

