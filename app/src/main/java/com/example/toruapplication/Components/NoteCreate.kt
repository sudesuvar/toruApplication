package com.example.toruapplication.Components


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
import java.io.File



@Preview
@Composable
fun NoteCreate() {
    var title by remember { mutableStateOf("") }
    val context = LocalContext.current
    var audioFile: File? = null
    val recorder by lazy {
        AudioRecorderViewModel(context)
    }
    val player by lazy {
        AndroidAudioPlayer(context)
    }


    Column(
        modifier = Modifier
            .background(Color(0xFFF6F8FB), shape = RoundedCornerShape(16.dp))
            .padding(16.dp)
            .width(300.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("New Note", color = Color.Black, fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
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
                //Text(if (isRecording) "Recording..." else "Click the button to register")
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
                        .background(color = colorResource(R.color.white), RoundedCornerShape(50))
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
                    recorder.start(it)
                    audioFile = it
                }

            }) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(Color(0xFFEAF6EA), RoundedCornerShape(50))
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

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            /*val minutes = recordingTime / 60
            val seconds = recordingTime % 60
            Text(String.format("%02d:%02d", minutes, seconds), fontSize = 14.sp)
            Text(if (isRecording) "Recording in progress" else "Recording has not started", fontSize = 14.sp)*/
        }

        Spacer(modifier = Modifier.height(24.dp))

        /*Button(
            onClick = {
                //player.playFile(audioFile ?: return@Button) oynatmak için
            },
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.Primary)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Save", color = Color.White, fontWeight = FontWeight.Bold)
        }*/
    }
}
