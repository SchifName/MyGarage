package com.example.mygarage.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.NumberFormat
import java.util.*

@Entity(tableName = "Car")
data class Car(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo val brand: String,
    @ColumnInfo val model: String,
    @ColumnInfo(name="fuel_type") val fuelType: String,
    @ColumnInfo val price: Double,
    @ColumnInfo(name="power") val power: Int,
    @ColumnInfo val mileage: Double,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) val image: ByteArray?,
    @ColumnInfo(name="year_production") val yearOfProduction: Int,
)

fun carPowerWithUnitString(kw: Int): String {
    return "$kw kW"
}

fun carMileageWithUnitString(mileage: Double): String {
    return "$mileage Km"
}

fun formatCurrency(price:Double): String{
    val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
    return format.format(price)
}

