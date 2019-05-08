package com.example.kinokotlin.model

import com.google.gson.annotations.SerializedName

data class PreGenre (
    @SerializedName("genres") val genres: List<Genre>? = null
)