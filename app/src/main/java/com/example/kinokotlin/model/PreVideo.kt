package com.example.kinokotlin.model

import com.google.gson.annotations.SerializedName

data class PreVideo (
    @SerializedName("id") val id: Int,
    @SerializedName("results") val results: List<Video>? = null
)