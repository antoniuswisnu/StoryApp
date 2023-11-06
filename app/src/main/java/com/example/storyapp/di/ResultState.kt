package com.example.storyapp.di

sealed class ResultState<out R> private constructor() {
    data class Success<out T>(val data: T) : ResultState<T>()
    data class Error(val data: String) : ResultState<Nothing>()
    object Loading : ResultState<Nothing>()
}