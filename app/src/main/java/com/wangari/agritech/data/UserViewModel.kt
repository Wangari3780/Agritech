package com.wangari.agritech.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wangari.agritech.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            _user.value = User(
                uid ="" ,
                name = "",
                location = "",
                email = ""
            )
        }
    }
}