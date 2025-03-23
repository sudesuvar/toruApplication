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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(navController: NavController, viewModel: AudioRecorderViewModel) {

    val context = LocalContext.current
    var notes by remember { mutableStateOf<List<AudioNote>>(emptyList()) }
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    LaunchedEffect(Unit) {
        userId?.let { uid ->
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("audio_notes")
                .get()
                .addOnSuccessListener { result ->
                    notes = result.documents.mapNotNull { it.toObject(AudioNote::class.java) }
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Veri alınamadı: ${e.message}")
                }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = {
                    Text(
                        "Toru", textAlign = TextAlign.Center,
                        modifier = Modifier.padding(start = 185.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
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
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                    items(notes) { note ->
                        VoiceNoteItem(title = note.title, audioUrl = note.audioUrl, viewModel)
                    }
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

@Composable
fun VoiceNoteItem(title: String, audioUrl: String, viewModel: AudioRecorderViewModel) {
    val context = LocalContext.current
    val mediaPlayer = remember { mutableStateOf<MediaPlayer?>(null) }

    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, color = MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(title, color = MaterialTheme.colorScheme.onSecondary)

        Row {
            IconButton(onClick = {
                mediaPlayer.value?.release() // Önceki çalmayı durdur
                mediaPlayer.value = MediaPlayer().apply {
                    setAudioAttributes(
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
                    )
                    setDataSource(audioUrl)
                    prepare()
                    start()
                }
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.play),
                    contentDescription = "Play",
                    tint = Color(0xFFC14A4A),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}








