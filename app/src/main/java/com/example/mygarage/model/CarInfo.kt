package com.example.mygarage.model


import com.google.gson.annotations.SerializedName

data class CarInfo(
    @SerializedName("MAKER") val maker: String,
    @SerializedName("MODEL") val model: String
)
