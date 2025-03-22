package com.example.toruapplication.pages

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.toruapplication.R
import com.example.toruapplication.Routes
import com.example.toruapplication.viewmodel.AuthState
import com.example.toruapplication.viewmodel.AuthViewModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient


@Composable
fun LoginPage(navController: NavController, viewModel: AuthViewModel) {
    val authState = viewModel.authState.observeAsState()
    val focusManager = LocalFocusManager.current // Klavye yönetimi için focusManager
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordVisible = remember { mutableStateOf(false) }
    val isBottomSheetOpen = remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (val currentState = authState.value) {
            is AuthState.Authenticated -> {
                navController.navigate(Routes.MainPage)
            }
            is AuthState.Error -> {
                Toast.makeText(context, currentState.message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }
    val oneTapClient: SignInClient = Identity.getSignInClient(context)

    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId("482122919504-jshbi5miq5gk34an7c2cukqsrgbvo0b2.apps.googleusercontent.com")
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
            val idToken = credential.googleIdToken
            if (idToken != null) {
                viewModel.googleSignup(idToken) { success ->
                    if (success) {
                        Toast.makeText(context, "Giriş başarılı!", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(context, "Giriş başarısız!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Login",
                color =MaterialTheme.colorScheme.onSurface,
                fontSize = 28.sp,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Email", color = Color.Black) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedLabelColor = Color.Gray,
                    focusedLabelColor = Color.Black,
                    cursorColor = Color.Black,
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Email
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text("Password", color = Color.Black) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedLabelColor = Color.Gray,
                    focusedLabelColor = Color.Black,
                    cursorColor = Color.Black,
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = Color.Black
                ),
                visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val icon = if (passwordVisible.value) "Hide" else "Show"
                    TextButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                        Text(icon, color = Color.Black)
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Password
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.login(email.value, password.value)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.Primary))
            ) {
                Text("Login", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Forgot Password ?",
                color = Color.Black,
                modifier = Modifier.clickable { isBottomSheetOpen.value = true },
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Don't have an account ? Signup ?",
                color = Color.Black,
                modifier = Modifier.clickable { navController.navigate("signup") },
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter =  painterResource(id = R.drawable.google) ,
                    contentDescription = "Google Image",
                    modifier = Modifier
                        .size(48.dp)
                        .clickable {
                            oneTapClient.beginSignIn(signInRequest)
                                .addOnSuccessListener { result ->
                                    val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                                    launcher.launch(intentSenderRequest)
                                    navController.navigate("main")
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(context, "Google Sign-In başlatılamadı: ${e.message}", Toast.LENGTH_SHORT).show()
                                    Log.e("GoogleSignIn", "Google Sign-In başlatılırken hata oluştu", e)
                                }


                        }
                )
            }
        }

        if (isBottomSheetOpen.value) {
            ForgotPasswordBottomSheet(
                onDismiss = { isBottomSheetOpen.value = false },
                viewModel = viewModel
            )
        }

    }
}

@Composable
fun ForgotPasswordBottomSheet(onDismiss: () -> Unit, viewModel: AuthViewModel) {
    val emailPass = remember { mutableStateOf("") }
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x80000000)), // Semi-transparent background
        contentAlignment = Alignment.BottomCenter
    ) {
        Surface(
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Reset Password",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Enter your email to reset your password.",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(16.dp))


                OutlinedTextField(
                    value = emailPass.value,
                    onValueChange = {emailPass.value = it},
                    label = { Text("Email", color = Color.Black) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedLabelColor = Color.Gray,
                        focusedLabelColor = Color.Black,
                        cursorColor = Color.Black,
                        unfocusedBorderColor = Color.Gray,
                        focusedBorderColor = Color.Black
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {  viewModel.forgotPassword(emailPass.value, context) {
                        Toast.makeText(context, "Şifre sıfırlama maili gönderildi!", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.Primary))
                ) {
                    Text("Reset Password", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Cancel",
                    color = Color.Red,
                    modifier = Modifier.clickable { onDismiss() },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}