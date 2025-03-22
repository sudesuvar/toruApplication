package com.example.toruapplication.viewmodel

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import java.io.File

class AndroidAudioPlayer(
    private val context: Context
): AudioPlayer,  ViewModel() {

    private var player: MediaPlayer? = null

    override fun playFile(file: File) {
        MediaPlayer.create(context, file.toUri()).apply {
            player = this
            start()
            Log.d("AudioPlayer", "Playing file: ${file.absolutePath}")
        }
    }

    override fun stop() {
        player?.stop()
        player?.release()
        player = null
    }
}