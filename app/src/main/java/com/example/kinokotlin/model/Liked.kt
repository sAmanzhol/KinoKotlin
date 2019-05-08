package com.example.kinokotlin.model

data class Liked(val id: String, val email: String){
    constructor(): this("", "")
}