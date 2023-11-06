package com.example.storyapp.ui.main

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.repository.StoryPagingRepository

class StoryViewModel(private val storyRepository: StoryPagingRepository) : ViewModel() {
    fun getStory() = storyRepository.getStories()
}