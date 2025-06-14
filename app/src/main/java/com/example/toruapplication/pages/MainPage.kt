package com.example.toruapplication.pages

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.toruapplication.Components.ActionBar
import com.example.toruapplication.R
import com.example.toruapplication.viewmodel.AudioNote
import com.example.toruapplication.viewmodel.AudioRecorderViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.accompanist.swiperefresh.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPage(navController: NavController, viewModel: AudioRecorderViewModel) {
    val context = LocalContext.current
    var notes by remember { mutableStateOf<List<AudioNote>>(emptyList()) }
    val userId = FirebaseAuth.getInstance().currentUser?.uid
    var isRefreshing by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }


    fun refreshNotes() {
        isRefreshing = true
        userId?.let { uid ->
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("audio_notes")
                .get()
                .addOnSuccessListener { result ->
                    notes = result.documents.mapNotNull { it.toObject(AudioNote::class.java) }
                    isRefreshing = false
                    isLoading = false
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Veri alınamadı: ${e.message}")
                    isRefreshing = false
                    isLoading = false
                }
        }
    }
    fun getSortedNotes(): List<AudioNote> {
        return notes.sortedByDescending { it.isPinned } // Sabitlenenler en başa gelir
    }


    LaunchedEffect(Unit) {
        refreshNotes()
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
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                isLoading -> { // Yüklenme sırasında gösterilecek indicator
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                notes.isEmpty() -> {
                    Text(
                        "No notes yet",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 4.dp)
                    ) {
                        items(getSortedNotes()) { note -> // Use sorted list
                            VoiceNoteItem(
                                title = note.title,
                                audioUrl = note.audioUrl,
                                timestamp = note.timestamp,
                                isPinned = note.isPinned,
                                onPinClick = {
                                    viewModel.pinAudioNote(note.audioUrl){refreshNotes()
                                    }
                                    refreshNotes() // Refresh notes after pinning
                                },
                                viewModel = viewModel
                            )
                        }
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
fun VoiceNoteItem(title: String, audioUrl: String,timestamp: String,isPinned: Boolean, onPinClick: () -> Unit, viewModel: AudioRecorderViewModel) {
    val context = LocalContext.current
    val mediaPlayer = remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

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
        IconButton(
            onClick = onPinClick,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.thumbtacks),
                contentDescription = "Pin",
                tint = if (isPinned) Color(0xFFFFA500) else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
        Column(verticalArrangement = Arrangement.Center) {

            Text(
                title,
                color = MaterialTheme.colorScheme.onSecondary,
                fontWeight = FontWeight.Bold
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = "Timestamp",
                    tint = Color.LightGray,
                    modifier = Modifier.size(16.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    timestamp,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }


        }


        Row {
            IconButton(onClick = {
                if (isPlaying) {
                    mediaPlayer.value?.pause()
                    isPlaying = false
                } else {
                    mediaPlayer.value?.release() // Önceki oynatmayı temizle
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
                        isPlaying = true
                        setOnCompletionListener {
                            isPlaying = false
                        }
                    }
                }
            }) {
                Icon(
                    painter = painterResource(id = if (isPlaying) R.drawable.pause else R.drawable.play),
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = if (isPlaying) Color(0xFFC14A4A) else Color(0xFF4CAF50),
                    modifier = Modifier.size(24.dp)
                )
            }
            IconButton(onClick = {
                viewModel.deleteAudioNote(audioUrl)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = "Delete",
                    tint = Color(0xFFC14A4A),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}
