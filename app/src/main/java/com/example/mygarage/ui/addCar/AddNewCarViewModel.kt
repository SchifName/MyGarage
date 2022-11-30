package com.example.mygarage.ui.addCar

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.mygarage.model.CarInfo
import com.example.mygarage.data.CarDao
import com.example.mygarage.model.Car
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

class AddNewCarViewModel(private val carDao: CarDao) : ViewModel() {
    private val _carList = MutableLiveData<List<CarInfo>>()

    val playlist: LiveData<List<CarInfo>>
        get() = _carList

    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    /*val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown
    */

    fun checkInputEditTextNewCar(
        brand: String,
        year: Int,
        model: String,
        fuelType: String,
        power: TextInputEditText,
        price: TextInputEditText,
        mileage: Double
    ): Boolean {
        return brand.isNotBlank() && year != 0 && model.isNotBlank() && fuelType.isNotBlank() && power != 0 && power.toString().length <= 4 && price != 0.0 && mileage != 0.0 && mileage.toString().length < 10
    }

    fun addCar(
        Brand: String,
        Model: String,
        YearOfProduction: Int,
        Power: TextInputEditText,
        FuelType: String,
        Price: TextInputEditText,
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

    /*fun refreshDataFromNetwork() = viewModelScope.launch {
        try {
            _carList.value = VehicleApi.retrofitService.getCarInfo()
            _eventNetworkError.value = false
            _isNetworkErrorShown.value = false

        } catch (networkError: IOException) {
            _eventNetworkError.value = true
        }
    }*/

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