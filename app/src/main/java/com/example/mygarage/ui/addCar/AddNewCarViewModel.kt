package com.example.mygarage.ui.addCar

import android.graphics.Bitmap
import android.view.View
import androidx.lifecycle.*
import com.example.mygarage.data.CarDao
import com.example.mygarage.model.Car
import com.example.mygarage.model.CarInfo
import com.example.mygarage.network.MyCarApi
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException

class AddNewCarViewModel(private val carDao: CarDao) : ViewModel() {

    fun getCarById(id: Long): LiveData<Car> {
        return carDao.getCarById(id).asLiveData()
    }

    var checkedItemBrand = -1
    var checkedItemModel = -1

    private val _brand = MutableLiveData<List<CarInfo>>()

    fun checkInputEditTextNewCar(
        brand: String,
        year: Int,
        model: String,
        fuelType: String,
        power: Int,
        price: Double,
        mileage: Double
    ): Boolean {
        return brand.isNotBlank() && year > 1800 && model.isNotBlank() && fuelType.isNotBlank() && power > 0 && power < 3000 && price > 0.0 && mileage > 0.0 && mileage < 1000000
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

    /**
     * Function for capturing car data via API
     */
    fun brandAcquisition(view: View) = CoroutineScope(Dispatchers.Main).launch {
        try {
            if (_brand.value == null) {
                //_brand.value = MyCarApi.retrofitService.getMyCarInfo()
                _brand.postValue(MyCarApi.retrofitService.getMyCarInfo())
            }
        } catch (networkError: IOException) {
            Snackbar.make(view, "Connection error", Snackbar.LENGTH_SHORT).show()
        }
    }

    /**
     * Function for brand acquisition via API
     */
    fun getBrand(): List<String> {
        return _brand.value!!.map { e -> e.maker }.distinct()
    }

    /**
     * Function for capturing patterns by brand via API
     */
    fun getModel(maker: String): List<String> {
        val makerList = _brand.value!!.filter { e -> e.maker == maker }
        return makerList.map { e -> e.model }.distinct().sorted()
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