package com.example.mygarage.model


import com.google.gson.annotations.SerializedName

data class CarInfo(
    @SerializedName("YEAR") val year: String,
    @SerializedName("ENGINE") val engine: String,
    @SerializedName("PRICE") val price: String,
    @SerializedName("MODEL") val model: String,
    @SerializedName("DOOR") val door: String,
    @SerializedName("SEATS") val seats: String,
)
