package com.example.adem_energy.firebase

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: AuthRepository) : ViewModel() {

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

    fun createUser(authUser: AuthUser) = repo.createUser(authUser)

    fun loginUser(authUser: AuthUser) = repo.loginUser(authUser)

    fun createUserWithPhone(mobile: String, activity: Activity) =
        repo.createUserWithPhone(mobile, activity)

    fun signInWithCredential(code: String) = repo.signWithCredential(code)

    fun logoutUser() {
        viewModelScope.launch {
            repo.logoutUser().collect { result ->
                when (result) {
                    is ResultState.Success -> _isUserLoggedIn.value = false
                    is ResultState.Error -> _isUserLoggedIn.value = true
                    is ResultState.Loading -> {}
                }
            }
        }
    }

    // ✅ Ab sahi login status check karega
    fun checkUserLoginStatus() {
        _isUserLoggedIn.value = repo.isUserLoggedIn()
    }
}


class AuthViewModelFactory(
    private val repo: AuthRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
