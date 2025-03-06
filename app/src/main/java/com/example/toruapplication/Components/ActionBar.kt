package com.example.toruapplication.Components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.toruapplication.R

@Preview
@Composable
fun ActionBar() {
    var isExpanded by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                AnimatedVisibility(visible = isExpanded, enter = androidx.compose.animation.expandVertically(), exit = androidx.compose.animation.shrinkVertically()) {
                    Column {
                        SmallFab(icon = Icons.Default.Settings, contentDescription = "Setting Page", modifier = Modifier, color = colorResource(R.color.Primary)) {
                            println("Settings clicked")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        SmallFab(icon = Icons.Default.Edit, contentDescription = "Add Note", modifier = Modifier, color = colorResource(R.color.Primary)) {
                            println("Edit clicked")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        SmallFab(icon = Icons.Default.Call, contentDescription = "Add voice note", modifier = Modifier, color = colorResource(R.color.Primary)) {
                            println("Call clicked")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                SmallFab(icon = Icons.Default.Add, contentDescription = "Add", modifier = Modifier, color = colorResource(R.color.Primary)) {
                    isExpanded = !isExpanded
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            // Ekranın ana içeriği buraya
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
