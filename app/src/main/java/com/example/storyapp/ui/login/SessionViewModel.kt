package com.example.storyapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.repository.UserRepository
import com.example.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

class SessionViewModel (private val repository: UserRepository) : ViewModel(){
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun getSession() = repository.getSession()

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}