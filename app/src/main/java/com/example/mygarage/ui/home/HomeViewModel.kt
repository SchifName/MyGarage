package com.example.mygarage.ui.home

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.mygarage.data.CarDao
import com.example.mygarage.model.Car
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class HomeViewModel(private val CarDao: CarDao) : ViewModel() {
    val allCars: LiveData<List<Car>> = CarDao.getCars().asLiveData()

    fun addCar(
        Brand: String,
        Model: String,
        YearOfProduction: Int,
        Power: Int,
        FuelType: String,
        Price: Double,
        Image: Bitmap?,
        Mileage: Double
    ) {
        val car = Car(
            brand = Brand,
            model = Model,
            yearOfProduction = YearOfProduction,
            power = Power,
            fuelType = FuelType,
            price = Price,
            image = Image?.toByteArray(),
            mileage = Mileage
        )

        viewModelScope.launch {
            CarDao.insert(car)
        }
    }
    fun updateCar(car: Car) {
        val updatedCar = Car(
            id = car.id,
            brand = car.brand,
            model = car.model,
            yearOfProduction = car.yearOfProduction,
            power = car.power,
            fuelType = car.fuelType,
            price = car.price,
            mileage = car.mileage,
            image = car.image,
        )
        updateCarDatabase(updatedCar)
    }

    fun updateCarDatabase(car: Car) {
        CoroutineScope(Dispatchers.IO).launch {
            CarDao.update(car)
        }
    }
    private fun Bitmap.toByteArray(quality: Int = 100): ByteArray {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, quality, stream)
        return stream.toByteArray()
    }
}

class HomeViewModelFactory(private val carDao: CarDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(carDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
