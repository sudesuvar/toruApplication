package com.example.toruapplication.Components


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.toruapplication.R
import com.example.toruapplication.viewmodel.AndroidAudioPlayer
import com.example.toruapplication.viewmodel.AudioRecorderViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import java.io.File



@Composable
fun NoteCreate(onDismiss: () -> Unit) { // onDismiss ekledik
    var title by remember { mutableStateOf("") }
    val context = LocalContext.current
    var audioFile: File? = null
    val recorder by lazy {
        AudioRecorderViewModel(context)
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background, shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
            .width(300.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("New Note",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 24.sp,
                modifier = Modifier.padding(top = 8.dp),
            )

            IconButton(onClick = { onDismiss() }) {
                Icon(
                    painter = painterResource(id = R.drawable.cross),
                    contentDescription = "Exit",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Give your audio recording a title first",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 8.sp,
                modifier = Modifier
                    .padding(top = 8.dp)
            )
            /*Text(
                text = "Recording Time: $recordTime s",
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 8.dp)
            )*/


        }


        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title", color = MaterialTheme.colorScheme.onSurface) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.onPrimary,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
            ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color(0xFFEFF1F7), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    painter = painterResource(id = R.drawable.sound),
                    contentDescription = "create",
                    tint = colorResource(R.color.Primary),
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = {
                recorder.stop()
            }) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(50))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.stop_button),
                        contentDescription = "create",
                        tint = Color(0xFFC14A4A),
                        modifier = Modifier.align(Alignment.Center)
                            .size(56.dp)
                    )
                }
            }

            IconButton(onClick = {
                val audioFile = File(context.cacheDir, "audio.mp3").also {
                    recorder.start(it, title)
                    audioFile = it
                }
            }) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(MaterialTheme.colorScheme.background, RoundedCornerShape(50))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.microphone),
                        contentDescription = "create",
                        tint = Color(0xFF2F5731),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
