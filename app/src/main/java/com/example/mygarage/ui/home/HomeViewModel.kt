package com.example.mygarage.ui.home

import android.graphics.Bitmap
import androidx.lifecycle.*
import com.example.mygarage.data.CarDao
import com.example.mygarage.model.Car
import com.example.mygarage.model.CarLogo
import com.example.mygarage.network.MyCarApiLogo
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

enum class LogoApiStatus {
    LOADING,
    ERROR,
    DONE
}

class HomeViewModel(private val CarDao: CarDao) : ViewModel() {
    val allCars: LiveData<List<Car>> = CarDao.getCars().asLiveData()

    // Status Logo Api
    private val _statusLogApi = MutableLiveData<LogoApiStatus>()
    val statusLogApi: LiveData<LogoApiStatus> = _statusLogApi

    private val _logoDataApi = MutableLiveData<List<CarLogo>>()
    val logoDataApi: LiveData<List<CarLogo>> = _logoDataApi

    init {
        getLogo()
    }

    /**
     * Gets Logo information from the Vehicle API Retrofit service and updates the
     * [_logoDataApi] [List] [LiveData].
     */
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
