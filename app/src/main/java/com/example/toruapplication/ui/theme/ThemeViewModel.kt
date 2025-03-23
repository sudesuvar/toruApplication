package com.example.toruapplication.ui.theme

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ThemeViewModel: ViewModel() {
    private var _isDarkTheme = mutableStateOf(false)
    val isDarkTheme: MutableState<Boolean> get() = _isDarkTheme // Değişiklik burada

    fun toggleTheme() {
        _isDarkTheme.value = !_isDarkTheme.value // Şu anda olan temayı tersine çeviriyoruz
        Log.i("Theme", "Toggled Theme: ${_isDarkTheme.value}") // Tema değişikliğini loglat
    }


}