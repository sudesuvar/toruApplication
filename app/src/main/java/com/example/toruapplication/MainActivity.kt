package com.example.toruapplication

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.toruapplication.pages.LoginPage
import com.example.toruapplication.pages.MainPage
import com.example.toruapplication.pages.SettingsPage
import com.example.toruapplication.pages.WelcomePage
import com.example.toruapplication.pages.SignupPage
import com.example.toruapplication.theme.ThemeViewModel
import com.example.toruapplication.theme.ToruTheme
import com.example.toruapplication.viewmodel.AuthState
import com.example.toruapplication.viewmodel.AuthViewModel
import com.google.firebase.FirebaseApp


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            val isDarkTheme by themeViewModel.isDarkTheme
            ToruTheme(isDarkTheme = isDarkTheme) {
                ToruApp()
                Log.i("TORUTHEME", "Recomposition happened! isDarkTheme: $isDarkTheme")
            }
        }
    }
}

@Composable
fun ToruApp(){
    val navController = rememberNavController()
    val authviewModel = AuthViewModel()
    val authState = authviewModel.authState.observeAsState(AuthState.UnAuthenticated)
    var isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(authState.value) {
        if (authState.value is AuthState.Authenticated) {
            isLoading.value = false
        } else if (authState.value is AuthState.UnAuthenticated) {
            isLoading.value = false
        } else {
            isLoading.value = true
        }
    }

    if (isLoading.value) {
        LoadingScreen()
    } else {
        val startDestination = when (authState.value) {
            is AuthState.Authenticated -> Routes.MainPage
            else -> Routes.WelcomePage
        }
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable(Routes.LoginPage) {
                LoginPage(navController, viewModel = authviewModel)
            }
            composable(Routes.WelcomePage) {
                WelcomePage(navController, viewModel= authviewModel)
            }
            composable(Routes.SignupPage) {
                SignupPage(navController, viewModel= authviewModel)
            }
            composable(Routes.MainPage) {
                MainPage(navController)
            }
            composable(Routes.SettingsPage) {
                SettingsPage(navController, viewModel= authviewModel)
            }
        }
    }

}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


