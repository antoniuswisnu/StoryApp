package com.example.storyapp.di

import android.content.Context
import com.example.storyapp.api.retrofit.ApiConfig
import com.example.storyapp.data.pref.UserPreference
import com.example.storyapp.data.repository.UserRepository
import com.example.storyapp.data.pref.dataStore
import com.example.storyapp.data.repository.StoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {

    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val userPreference = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { userPreference.getUser().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return StoryRepository.getInstance(apiService)
    }
}