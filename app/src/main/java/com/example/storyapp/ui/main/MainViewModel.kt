package com.example.storyapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.repository.UserRepository
import kotlinx.coroutines.launch

class MainViewModel (private val repository: UserRepository) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
}