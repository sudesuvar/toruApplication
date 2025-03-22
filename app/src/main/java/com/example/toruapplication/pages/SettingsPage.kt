package com.example.toruapplication.pages

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.toruapplication.R
import com.example.toruapplication.Routes
import com.example.toruapplication.theme.ThemeViewModel
import com.example.toruapplication.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(navController: NavController, viewModel: AuthViewModel){
    val themeViewModel: ThemeViewModel = viewModel()

    val isDarkTheme by themeViewModel.isDarkTheme
    var checkedLanguage = remember { mutableStateOf(true) }

    Log.i("Theme", "Recomposition happened! isDarkTheme: $isDarkTheme")

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.Primary),
                    titleContentColor = colorResource(R.color.BrokenWhite),
                ),
                navigationIcon = {
                    IconButton(onClick = {  navController.navigate(Routes.MainPage)}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description",
                            tint = colorResource(R.color.black)
                        )
                    }
                },
                title = {
                    Text("Toru", textAlign = TextAlign.Center,
                        modifier = Modifier.padding(start = 145.dp),
                        color = colorResource(R.color.black),
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .height(640.dp)
                .padding(innerPadding),
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Column {
                SettingOption(
                    text = "Theme",
                    checked = isDarkTheme,
                    onCheckedChange = { themeViewModel.toggleTheme() }
                )
                Log.i("Theme", "Current Theme: $isDarkTheme")
                SettingOption(
                    text = "Language",
                    checked = checkedLanguage.value,
                    onCheckedChange = { checkedLanguage.value = it }
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(16.dp))
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .padding(vertical = 12.dp, horizontal = 16.dp)
                        .clickable { viewModel.signout() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Log Out",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(32.dp))
                    Image(
                        painter = painterResource(id = R.drawable.logout),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )

                }
            }
        }
    }
}

@Composable
fun SettingOption(text: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(16.dp))
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            thumbContent = if (checked) {
                {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                    )
                }
            } else {
                null
            }
        )
    }
}


