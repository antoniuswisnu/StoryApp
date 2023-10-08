package com.example.storyapp.data.repository

import com.example.storyapp.api.retrofit.ApiService
import com.example.storyapp.api.response.RegisterResponse

class RegisterRepository(private val apiService: ApiService) {
    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }
}