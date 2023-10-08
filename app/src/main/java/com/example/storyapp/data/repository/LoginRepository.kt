package com.example.storyapp.data.repository

import com.example.storyapp.api.retrofit.ApiService
import com.example.storyapp.api.response.LoginResponse

class LoginRepository(private val apiService: ApiService) {
    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }
}