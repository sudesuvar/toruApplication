package com.example.toruapplication.Components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.toruapplication.R
import com.example.toruapplication.Routes
import com.example.toruapplication.viewmodel.AudioRecorderViewModel


@Composable
fun ActionBar(navController: NavController) {
    var isExpanded by remember { mutableStateOf(false) }
    var showNoteCreate by remember { mutableStateOf(false) }
    val context = LocalContext.current

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            AnimatedVisibility(
                visible = isExpanded,
                enter = androidx.compose.animation.expandVertically(),
                exit = androidx.compose.animation.shrinkVertically()
            ) {
                Column {
                    SmallFab(
                        icon = Icons.Default.Settings,
                        contentDescription = "Setting Page",
                        color = colorResource(R.color.Primary)
                    ) {
                        navController.navigate(Routes.SettingsPage)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    SmallFab(
                        icon = Icons.Default.Edit,
                        contentDescription = "Add voice note",
                        color = colorResource(R.color.Primary)
                    ) {
                        showNoteCreate = true
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            SmallFab(
                icon = Icons.Default.Add,
                contentDescription = "Add",
                color = colorResource(R.color.Primary)
            ) {
                isExpanded = !isExpanded
            }
        }

        if (showNoteCreate) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)) // Arkaplanı hafif karartalım
                    .clickable { showNoteCreate = false },
                contentAlignment = Alignment.Center
            ) {
                NoteCreate()
            }
        }
    }


@Composable
fun SmallFab(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.secondary,
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.size(56.dp),
        containerColor = color
    ) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}
