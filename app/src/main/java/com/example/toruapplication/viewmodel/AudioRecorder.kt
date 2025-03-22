package com.example.toruapplication.viewmodel

import java.io.File

interface AudioRecorder {
    fun start(outputFile: File)
    fun stop()
}