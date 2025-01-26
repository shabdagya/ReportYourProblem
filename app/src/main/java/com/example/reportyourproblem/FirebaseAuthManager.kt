package com.example.reportyourproblem

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class FirebaseAuthManager {
    private val auth = FirebaseAuth.getInstance()

    // Send OTP to phone number
    fun sendOtp(
        phoneNumber: String,
        activity: Activity,
        onCodeSent: (verificationId: String) -> Unit,
        onError: (exception: FirebaseException) -> Unit
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {}

                override fun onVerificationFailed(e: FirebaseException) {
                    onError(e) // Call onError when verification fails
                }

                override fun onCodeSent(
                    verificationId: String,
                    token: PhoneAuthProvider.ForceResendingToken
                ) {
                    onCodeSent(verificationId) // Pass verificationId
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    // Verify OTP entered by the user
    fun verifyOtp(
        verificationId: String,
        otp: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                } else {
                    task.exception?.let { onFailure(it) }
                }
            }
    }
}
