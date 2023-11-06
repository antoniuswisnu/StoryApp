package com.example.storyapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.api.response.ListStoryItem
import com.example.storyapp.api.response.StoryResponse
import com.example.storyapp.api.retrofit.ApiConfig
import com.example.storyapp.api.retrofit.ApiService
import com.example.storyapp.data.paging.StoryPagingSource
import com.example.storyapp.data.pref.LoginPreferences
import com.example.storyapp.di.ResultState


class StoryPagingRepository constructor(application: Application, private val userPreferences: LoginPreferences) {

    private val token = userPreferences.getUser().token.toString()
    private val apiService: ApiService = ApiConfig.getApiService(token)

    fun getStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(userPreferences, apiService)
            }
        ).liveData
    }

    fun getStoriesLocation(): LiveData<ResultState<StoryResponse>> = liveData {
        emit(ResultState.Loading)
        try {
            val response = apiService.getStoriesLocation(
                page = 1,
                size = 100,
                location = 1
            )
            if (response.error) {
                emit(ResultState.Error(response.message))
            } else {
                emit(ResultState.Success(response))
            }
        } catch (e: Exception) {
            emit(ResultState.Error(e.message.toString()))
        }
    }
}
