package com.example.mygarage.ui.carDetail

import androidx.lifecycle.*
import com.example.mygarage.data.CarDao
import com.example.mygarage.model.Car
import com.example.mygarage.model.CarLogo
import com.example.mygarage.network.MyCarApiLogo
import com.example.mygarage.ui.home.LogoApiStatus
import kotlinx.coroutines.launch

class CarDetailViewModel(private val carDao: CarDao) : ViewModel() {
    var idCar: Long = 0
    var arg: Long? = null
    // Status Logo Api
    private val _statusLogApi = MutableLiveData<LogoApiStatus>()
    val statusLogApi: LiveData<LogoApiStatus> = _statusLogApi

    private val _logoDataApi = MutableLiveData<List<CarLogo>>()
    val logoDataApi: LiveData<List<CarLogo>> = _logoDataApi

    init {
        getLogo()
    }

    private fun getLogo() {
        viewModelScope.launch {
            _statusLogApi.value = LogoApiStatus.LOADING
            try {
                _logoDataApi.value = MyCarApiLogo.retrofitService.getMyCarLogo()
                _statusLogApi.value = LogoApiStatus.DONE
            } catch (e: java.lang.Exception) {
                _statusLogApi.value = LogoApiStatus.ERROR
                _logoDataApi.value = listOf()
            }
        }
    }

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