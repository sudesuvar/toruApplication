package com.example.toruapplication.viewmodel

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.File
import java.io.FileOutputStream

class AudioRecorderViewModel(private val context: Context) : AudioRecorder {
    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null
    private var currentTitle: String = "" // 🔥 Kaydedilen başlık
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

    override fun start(outputFile: File, title: String) {
        this.outputFile = outputFile
        this.currentTitle = title
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
            uploadAudioToFirebase(file, currentTitle)
        }
    }

    private fun uploadAudioToFirebase(file: File, title: String) {
        val userId = auth.currentUser?.uid ?: return
        val fileName = "audio_notes/$userId/${System.currentTimeMillis()}.mp3"
        val audioRef = storageRef.child(fileName)

        audioRef.putFile(file.toUri())
            .addOnSuccessListener {
                audioRef.downloadUrl.addOnSuccessListener { uri ->
                    saveAudioMetadata(userId, uri.toString(), title) // 🔥 Başlık da kaydediliyor!
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Ses yükleme başarısız: ${e.message}")
                Toast.makeText(context, "Ses yükleme başarısız", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveAudioMetadata(userId: String, audioUrl: String, title: String) {
        val audioNote = AudioNote(title = title, audioUrl = audioUrl) // 🔥 Başlığı da ekledik!
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
    fun deleteAudioNote(audioUrl: String) {
        val userId = auth.currentUser?.uid ?: return

        firestoreRef.collection("users")
            .document(userId)
            .collection("audio_notes")
            .whereEqualTo("audioUrl", audioUrl)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    firestoreRef.collection("users")
                        .document(userId)
                        .collection("audio_notes")
                        .document(document.id)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Failed to delete: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Silme hatası: ${e.message}")
                Toast.makeText(context, "Failed to delete: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}


data class AudioNote(
    val title: String = "",
    val audioUrl: String = ""
) {
    // Firestore'un deserialize edebilmesi için boş bir constructor gerekli
    constructor() : this("", "")
}
