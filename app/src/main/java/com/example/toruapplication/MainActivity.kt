package com.example.toruapplication

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.toruapplication.pages.LoginPage
import com.example.toruapplication.pages.MainPage
import com.example.toruapplication.pages.SettingsPage
import com.example.toruapplication.pages.WelcomePage
import com.example.toruapplication.pages.SignupPage
import com.example.toruapplication.ui.theme.ToruTheme
import com.example.toruapplication.viewmodel.AuthState
import com.example.toruapplication.viewmodel.AuthViewModel
import com.google.firebase.FirebaseApp
import android.Manifest
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.setValue
import com.example.toruapplication.viewmodel.AudioRecorderViewModel
import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        val firebaseAppCheck = FirebaseAppCheck.getInstance()
        firebaseAppCheck.installAppCheckProviderFactory(
            PlayIntegrityAppCheckProviderFactory.getInstance()
        )
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            0
        )
        enableEdgeToEdge()
        setContent {
            //var darkTheme by remember { mutableStateOf(false) }
            val isSystemDark = isSystemInDarkTheme()
            var darkTheme by remember { mutableStateOf(isSystemDark) }
            ToruTheme(darkTheme = darkTheme) {
                ToruApp(darkTheme, onThemeUpdated = { darkTheme = !darkTheme })
            }
        }
    }
}

@Composable
fun ToruApp(darkTheme: Boolean, onThemeUpdated: () -> Unit){
    val navController = rememberNavController()
    val authviewModel = AuthViewModel()
    val authState = authviewModel.authState.observeAsState(AuthState.UnAuthenticated)
    var isLoading = remember { mutableStateOf(true) }
    val context = LocalContext.current

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
                SignupPage(navController, viewModel= authviewModel, )
            }
            composable(Routes.MainPage) {
                MainPage(navController, viewModel= AudioRecorderViewModel(context))
                val context = LocalContext.current
                val activity = context as? Activity

                LaunchedEffect(Unit) {
                    activity?.let {
                        ActivityCompat.requestPermissions(
                            it,
                            arrayOf(Manifest.permission.RECORD_AUDIO),
                            101
                        )
                    }
                }
            }
            composable(Routes.SettingsPage) {
                SettingsPage(navController, viewModel= authviewModel, darkTheme = darkTheme,
                    onThemeUpdated = onThemeUpdated)
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


