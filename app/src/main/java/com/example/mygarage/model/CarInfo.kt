package com.example.mygarage.model


import com.google.gson.annotations.SerializedName

data class CarInfo(
    @SerializedName("YEAR") val year: String,
    @SerializedName("MAKER") val maker: String,
    @SerializedName("ENGINE") val engine: String,
    @SerializedName("ENGINEMAKER") val engineMaker: String,
    @SerializedName("MODEL") val model: String,
    @SerializedName("FULLMODELNAME") val fullModelName: String,
)

data class CarLogo(
    @SerializedName("name") val name: String,
    @SerializedName("logo") val logo: String,
)