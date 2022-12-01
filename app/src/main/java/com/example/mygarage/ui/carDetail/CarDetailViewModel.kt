package com.example.mygarage.ui.carDetail

import androidx.lifecycle.*
import com.example.mygarage.data.CarDao
import com.example.mygarage.model.Car
import kotlinx.coroutines.launch

class CarDetailViewModel(private val carDao: CarDao) : ViewModel() {

    fun getCarById(id: Long): LiveData<Car> {
        return carDao.getCarById(id).asLiveData()
    }

    fun deleteCarById(id: Long) {
        viewModelScope.launch {
            carDao.deleteCarById(id)
        }
    }

}

class CarDetailViewModelFactory(private val carDao: CarDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CarDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CarDetailViewModel(carDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}