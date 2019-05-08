package com.example.kinokotlin.model

import com.google.gson.annotations.SerializedName

data class PreCast(
    @SerializedName("id") val id: Int,
    @SerializedName("cast") val cast: List<Cast>? = null,
    @SerializedName("crew") val crew: List<Crew>? = null
)