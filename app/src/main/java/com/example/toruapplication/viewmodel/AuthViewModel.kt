package com.example.toruapplication.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore


class AuthViewModel {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    private val _user = mutableStateOf<FirebaseUser?>(null)
    val user: State<FirebaseUser?> = _user

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            _user.value = currentUser
            _authState.value = AuthState.Authenticated
        } else {
            _authState.value = AuthState.UnAuthenticated
        }
    }


    fun login(email: String, password: String) {

        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email ve Şifre boş olamaz")
            return
        }

        _authState.value = AuthState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Authenticated
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Giriş Başarısız")
                }
            }
    }

    fun signup(email: String, password: String, name: String) {

        if (email.isEmpty() || password.isEmpty()) {
            _authState.value = AuthState.Error("Email and password cannot be empty")
            return
        }
        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    currentUser?.let {
                        val user = mapOf(
                            "uid" to it.uid,
                            "email" to email,
                            "name" to name
                        )

                        firestore.collection("users").document(it.uid).set(user)
                            .addOnSuccessListener {
                                _authState.value = AuthState.Authenticated
                            }
                            .addOnFailureListener { exception ->
                                _authState.value = AuthState.Error("Failed to save user: ${exception.message}")
                            }
                    }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Signup failed")
                }
            }


    }
    fun googleSignup(idToken: String, onResult: (Boolean) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _user.value = auth.currentUser
                    _authState.value = AuthState.Authenticated
                    onResult(true)
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Google Sign-In failed")
                    onResult(false)
                }
            }
    }

    fun forgotPassword(email: String, context: Context, onSuccess: () -> Unit) {
        if (email.isEmpty()) {
            Toast.makeText(context, "Email field cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Failed to send password reset email: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }




    fun signout(){
        auth.signOut()
        _authState.value = AuthState.UnAuthenticated
    }
}

sealed class  AuthState{
    object Authenticated : AuthState()
    object UnAuthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String) : AuthState()
}