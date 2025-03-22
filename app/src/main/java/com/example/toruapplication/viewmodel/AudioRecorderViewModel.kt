package com.example.toruapplication.viewmodel

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileOutputStream

class AudioRecorderViewModel( private val context: Context) :  AudioRecorder {

    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null
    private val storageRef = FirebaseStorage.getInstance().reference
    private val firestoreRef = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private fun createRecorder(): MediaRecorder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            MediaRecorder()
        }
    }

    override fun start(outputFile: File) {
        this.outputFile = outputFile
        createRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(FileOutputStream(outputFile).fd)

            prepare()
            start()

            recorder = this
        }
    }

    override fun stop() {
        recorder?.stop()
        recorder?.reset()
        recorder = null

        outputFile?.let { file ->
            uploadAudioToFirebase(file)
        }
    }

    private fun uploadAudioToFirebase(file: File) {
        val userId = auth.currentUser?.uid ?: return
        val fileName = "audio_notes/$userId/${System.currentTimeMillis()}.mp3"
        val audioRef = storageRef.child(fileName)

        audioRef.putFile(file.toUri())
            .addOnSuccessListener {
                audioRef.downloadUrl.addOnSuccessListener { uri ->
                    saveAudioMetadata(userId, uri.toString())
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Ses yükleme başarısız: ${e.message}")
                Toast.makeText(context, "Ses yükleme başarısız", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveAudioMetadata(userId: String, audioUrl: String) {
        val audioNote = AudioNote(title = "Ses Notu", audioUrl = audioUrl)
        firestoreRef.collection("users").document(userId)
            .collection("audio_notes")
            .add(audioNote)
            .addOnSuccessListener {
                Toast.makeText(context, "Ses kaydedildi!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Veri eklenemedi: ${e.message}")
            }
    }
}

data class AudioNote(
    val title: String,
    val audioUrl: String
)

