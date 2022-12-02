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
