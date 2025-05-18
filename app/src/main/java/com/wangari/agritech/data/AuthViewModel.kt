package com.wangari.agritech.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wangari.agritech.models.User
import com.wangari.agritech.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class AuthViewModel : ViewModel() {
    private val userRepository = UserRepository()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val firebaseUser = userRepository.getCurrentUser()

        firebaseUser?.let { user ->
            viewModelScope.launch {
                _isLoading.value = true

                userRepository.getUserProfile(user.uid)
                    .onSuccess { userProfile ->
                        _currentUser.value = userProfile
                    }
                    .onFailure { exception ->
                        _error.value = exception.message
                    }

                _isLoading.value = false
            }
        }
    }

    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            userRepository.signIn(email, password)
                .onSuccess { user ->
                    _currentUser.value = user
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }

    fun signUp(
        email: String,
        password: String,
        name: String,
        phone: String,
        location: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            userRepository.signUp(email, password, name, phone, location)
                .onSuccess { user ->
                    _currentUser.value = user
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }

            _isLoading.value = false
        }
    }

    fun signOut() {
        userRepository.signOut()
        _currentUser.value = null
    }

    fun clearError() {
        _error.value = null
    }
}