package com.example.toruapplication.pages

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.toruapplication.Components.ActionBar
import com.example.toruapplication.R
import com.example.toruapplication.viewmodel.AudioNote
import com.example.toruapplication.viewmodel.AudioRecorderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(navController: NavController, viewModel: AudioRecorderViewModel) {
    val context = LocalContext.current
    var notes by remember { mutableStateOf<List<AudioNote>>(emptyList()) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.Primary),
                    titleContentColor = colorResource(R.color.BrokenWhite),
                ),
                title = {
                    Text(
                        "Toru", textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        color = colorResource(R.color.black),
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (notes.isEmpty()) {
                Text(
                    "No notes yet",
                    color = Color.Gray,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 4.dp)
                ) {
                   /* items(notes) { note ->
                        VoiceNoteItem(title = note.title, audioUrl = note.audioUrl, viewModel, onDelete = {
                            notes = notes - note
                        })
                    }*/


                }
            }
            Box(
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                ActionBar(navController = navController)
            }
        }
    }
}

/*@Composable
fun VoiceNoteItem(title: String, audioUrl: String, viewModel: AudioRecorderViewModel, onDelete: () -> Unit) {
    val context = LocalContext.current

    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .border(1.dp, color = Color.Black, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title)

        Row {
            IconButton(onClick = {
                viewModel.playAudio(audioUrl, context)
                Log.d("AudioURL", "Playing audio from URL: $audioUrl")

            }) {
                Icon(
                    painter = painterResource(id = R.drawable.play),
                    contentDescription = "Play",
                    tint = Color(0xFFC14A4A),
                    modifier = Modifier.size(24.dp)
                )
            }
            /*IconButton(onClick = {
                if (audioUrl.isEmpty()) {
                    Toast.makeText(context, "Ses kaydı bulunamadı!", Toast.LENGTH_SHORT).show()
                } else {
                    viewModel.deleteAudioNote(audioUrl, onSuccess = {
                        Toast.makeText(context, "Ses başarıyla silindi", Toast.LENGTH_SHORT).show()
                        onDelete()
                    }, onFailure = {
                        Toast.makeText(context, "Silme başarısız!", Toast.LENGTH_SHORT).show()
                    })
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = "Delete",
                    tint = Color(0xFF2F5731),
                    modifier = Modifier.size(24.dp)
                )
            }*/
        }
    }
}*/








