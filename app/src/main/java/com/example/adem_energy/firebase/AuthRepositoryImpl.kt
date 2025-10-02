package com.example.adem_energy.firebase

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AuthRepositoryImpl (
    private val authdb: FirebaseAuth,context: Context
) : AuthRepository {

    private lateinit var onVerificationCode: String
    override fun createUser(auth: AuthUser): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        if (auth.email!!.isBlank() || auth.password!!.isBlank()) {
            trySend(ResultState.Error(Exception("Email and Password cannot be empty")))
            close()
            return@callbackFlow
        }

        authdb.createUserWithEmailAndPassword(auth.email!!, auth.password!!)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(ResultState.Success("User Created Successfully"))
                    Log.d("Auth", "User ID: ${authdb.currentUser?.uid}")
                } else {
                    trySend(ResultState.Error(Exception("Failed to create user")))
                }
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it))
            }

        awaitClose { close() }
    }


    override fun loginUser(auth: AuthUser): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)

        authdb.signInWithEmailAndPassword(
            auth.email!!,
            auth.password!!
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    trySend(ResultState.Success("Login Successful"))
                } else {
                    trySend(ResultState.Error(Exception("Login Failed")))
                }
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it))
            }

        awaitClose { close() }
    }

    override fun createUserWithPhone(phone: String, activity: Activity): Flow<ResultState<String>> =
        callbackFlow {
            val onVerificationCallBack =
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                    }

                    override fun onVerificationFailed(p0: FirebaseException) {
                        trySend(ResultState.Error(p0))
                    }

                    override fun onCodeSent(
                        verificationCode: String,
                        p1: PhoneAuthProvider.ForceResendingToken
                    ) {
                        super.onCodeSent(verificationCode, p1)
                        trySend(ResultState.Success("OTP Sent Successful"))
                        onVerificationCode = verificationCode
                    }

                }



            trySend(ResultState.Loading)

            val options = PhoneAuthOptions.newBuilder(authdb)
                .setPhoneNumber("+91$phone")
                .setTimeout(60L, java.util.concurrent.TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(onVerificationCallBack)
                .build()
            PhoneAuthProvider.verifyPhoneNumber(options)
            awaitClose {
                close()
            }

        }

    override fun signWithCredential(otp: String): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        val credential = PhoneAuthProvider.getCredential(onVerificationCode, otp)
        authdb.signInWithCredential(credential)
            .addOnCompleteListener {
                if (it.isSuccessful)
                    trySend(ResultState.Success("otp verified"))
            }
            .addOnFailureListener {
                trySend(ResultState.Error(it))
            }
        awaitClose {
            close()
        }
    }


    private val sharedPref = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    override fun logoutUser(): Flow<ResultState<String>> = callbackFlow {
        trySend(ResultState.Loading)
        try {
            authdb.signOut()


            sharedPref.edit().putBoolean("isUserLoggedIn", false).apply()

            trySend(ResultState.Success("Logout Successful"))
            close()
        } catch (e: Exception) {
            trySend(ResultState.Error(e))
            close()
        }

        awaitClose { }
    }

    override fun isUserLoggedIn(): Boolean {
        return sharedPref.getBoolean("isUserLoggedIn", authdb.currentUser != null)
    }
    fun setUserLoggedIn(loggedIn: Boolean) {
        sharedPref.edit().putBoolean("isUserLoggedIn", loggedIn).apply()
    }

}