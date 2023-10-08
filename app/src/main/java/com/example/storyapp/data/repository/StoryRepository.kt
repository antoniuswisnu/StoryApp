package com.example.storyapp.data.repository

import com.example.storyapp.api.response.ListStoryItem
import com.example.storyapp.api.response.Story
import com.example.storyapp.api.retrofit.ApiService

class StoryRepository(private val apiService: ApiService) {
    companion object {
        @Volatile
        private var instance: StoryRepository? = null

        fun getInstance(apiService: ApiService): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService).also { instance = it }
            }
    }

    suspend fun getStories(): List<ListStoryItem> {
        return apiService.getStories().listStory
    }

    suspend fun getDetailStory(id: String) : Story? {
        return apiService.getDetailStories(id).story
    }
}

