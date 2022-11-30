package com.example.mygarage.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Car")
data class Car(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo val brand: String,
    @ColumnInfo val model: String,
    @ColumnInfo(name="fuel_type") val fuelType: String,
    @ColumnInfo val price: Double,
    @ColumnInfo(name="power") val power: Int,
    @ColumnInfo val seats: Int,
    @ColumnInfo val mileage: Double,
    @ColumnInfo(name="year_start_production") val yearStartProduction: Int,
    @ColumnInfo(name="year_end_production") val yearEndProduction: Int,
)

enum class fuelType{
    Petrol,
    Diesel,
    Electric,
    Gas,
    Other
}