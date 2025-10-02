package com.example.adem_energy.firebase

import android.app.Activity
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun createUser(
        auth: AuthUser
    ): Flow<ResultState<String>>

    fun loginUser(
        auth: AuthUser
    ): Flow<ResultState<String>>

    fun createUserWithPhone(
        phone: String,
        activity: Activity
    ): Flow<ResultState<String>>

    fun signWithCredential(
        otp: String
    ): Flow<ResultState<String>>

    fun logoutUser(): Flow<ResultState<String>>
    fun isUserLoggedIn(): Boolean
}