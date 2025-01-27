package com.example.reportyourproblem


//import com.example.myapp.firebase.FirebaseAuthManager
import com.example.reportyourproblem.firebase.FirebaseAuthManager


import android.app.Activity
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel : ViewModel() {
    private val firebaseAuthManager = FirebaseAuthManager()

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun sendOtp(phoneNumber: String, activity: Activity) {
        _uiState.value = LoginUiState.Loading
        firebaseAuthManager.sendOtp(
            phoneNumber = phoneNumber,
            activity = activity,
            onCodeSent = { verificationId ->
                _uiState.value = LoginUiState.OtpSent(verificationId)
            },
            onError = { exception ->
                _uiState.value = LoginUiState.Error(exception.message ?: "Unknown error")
            }
        )
    }

    fun verifyOtp(verificationId: String, otp: String) {
        _uiState.value = LoginUiState.Loading
        firebaseAuthManager.verifyOtp(
            verificationId = verificationId,
            otp = otp,
            onSuccess = {
                _uiState.value = LoginUiState.Success
            },
            onFailure = { exception ->
                _uiState.value = LoginUiState.Error(exception.message ?: "Unknown error")
            }
        )
    }
}

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class OtpSent(val verificationId: String) : LoginUiState()
    object Success : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}
