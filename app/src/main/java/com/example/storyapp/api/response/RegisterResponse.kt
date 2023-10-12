package com.example.storyapp.api.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse (
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
)

data class ErrorResponse(
    @field:SerializedName("error")
    val error: Boolean? = null,
    @field:SerializedName("message")
    val message: String? = null
)

