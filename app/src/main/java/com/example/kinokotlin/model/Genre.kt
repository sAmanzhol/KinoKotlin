package com.example.kinokotlin.model

import com.google.gson.annotations.SerializedName

data class Genre (
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String

){
    override fun toString(): String = name
}