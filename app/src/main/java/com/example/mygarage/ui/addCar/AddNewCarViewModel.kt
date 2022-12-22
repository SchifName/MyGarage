package com.example.mygarage.ui.addCar

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.mygarage.data.CarDao
import com.example.mygarage.model.Car
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class AddNewCarViewModel(private val carDao: CarDao) : ViewModel() {

    fun getCarById(id: Long): LiveData<Car> {
        return carDao.getCarById(id).asLiveData()
    }

    fun checkInputEditTextNewCar(
        brand: String,
        year: Int,
        model: String,
        fuelType: String,
        power: Int,
        price: Double,
        mileage: Double
    ): Boolean {
        return brand.isNotBlank() && year != 0 && model.isNotBlank() && fuelType.isNotBlank() && power > 0 && power < 9999 && price > 0.0 && mileage > 0.0 && mileage < 1000000
    }

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
            carDao.insert(car)
        }
    }

    fun modCar(
        id: Long,
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
            id = id,
            brand = Brand,
            model = Model,
            yearOfProduction = YearOfProduction,
            power = Power,
            fuelType = FuelType,
            price = Price,
            image = Image?.toByteArray(),
            mileage = Mileage
        )

        CoroutineScope(Dispatchers.IO).launch {
            carDao.update(car)
        }
    }

    private fun Bitmap.toByteArray(quality: Int = 100): ByteArray {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, quality, stream)
        return stream.toByteArray()
    }

}

class AddNewCarViewModelFactory(private val carDao: CarDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddNewCarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddNewCarViewModel(carDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}