package com.example.reportyourproblem


import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapp.viewmodel.LoginUiState
import com.example.myapp.viewmodel.LoginViewModel

@Composable
fun LoginScreen(activity: Activity, viewModel: LoginViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    var phoneNumber by remember { mutableStateOf("") }
    var otp by remember { mutableStateOf("") }
    var verificationId by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (uiState) {
            is LoginUiState.Idle -> {
                TextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.sendOtp(phoneNumber, activity) }) {
                    Text("Send OTP")
                }
            }
            is LoginUiState.OtpSent -> {
                verificationId = (uiState as LoginUiState.OtpSent).verificationId
                TextField(
                    value = otp,
                    onValueChange = { otp = it },
                    label = { Text("Enter OTP") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { viewModel.verifyOtp(verificationId, otp) }) {
                    Text("Verify OTP")
                }
            }
            is LoginUiState.Loading -> {
                CircularProgressIndicator()
            }
            is LoginUiState.Success -> {
                Text("Login Successful!")
            }
            is LoginUiState.Error -> {
                Text("Error: ${(uiState as LoginUiState.Error).message}")
            }
        }
    }
}
