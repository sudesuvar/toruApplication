package com.example.toruapplication.viewmodel

import java.io.File

interface AudioPlayer {
    fun playFile(file: File)
    fun stop()
}