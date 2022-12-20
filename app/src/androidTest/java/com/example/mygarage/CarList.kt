package com.example.mygarage

import com.example.mygarage.model.Car

val CarList = listOf<Car>(
    Car(
        id = 1,
        brand = "Ferrari",
        model = "F40",
        yearOfProduction = 1987,
        mileage = 20000.0,
        image = null,
        power = 200,
        price = 3000000.0,
        fuelType = "Gasoline"
    ),
    Car(
        id = 2,
        brand = "Fiat",
        model = "Punto",
        yearOfProduction = 2012,
        mileage = 120000.0,
        image = null,
        power = 50,
        price = 12000.0,
        fuelType = "Diesel"
    ),
    Car(
        id = 3,
        brand = "Peugeot",
        model = "308",
        yearOfProduction = 2020,
        mileage = 70000.0,
        image = null,
        power = 100,
        price = 30000.0,
        fuelType = "Diesel"
    )
)

val CarListDao = listOf<Car>(
    Car(
        id = 1,
        brand = "Ferrari",
        model = "F40",
        yearOfProduction = 1987,
        mileage = 20000.0,
        image = null,
        power = 200,
        price = 3000000.0,
        fuelType = "Gasoline"
    ),
    Car(
        id = 2,
        brand = "Fiat",
        model = "500",
        yearOfProduction = 2020,
        mileage = 10000.0,
        image = null,
        power = 70,
        price = 20000.0,
        fuelType = "Electric"
    ),
    Car(
        id = 3,
        brand = "Citroen",
        model = "C3",
        yearOfProduction = 2018,
        mileage = 120000.0,
        image = null,
        power = 70,
        price = 16000.0,
        fuelType = "Gasoline"
    ),
    Car(
        id = 4,
        brand = "Ferrari",
        model = "Enzo",
        yearOfProduction = 2002,
        mileage = 20000.0,
        image = null,
        power = 300,
        price = 3000000.0,
        fuelType = "Gasoline"
    )
)