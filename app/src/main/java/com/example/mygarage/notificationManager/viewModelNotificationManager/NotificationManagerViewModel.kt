package com.example.mygarage.notificationManager.viewModelNotificationManager


import android.app.Application
import androidx.lifecycle.*
import androidx.work.*
import com.example.mygarage.notificationManager.dataNotification.NotificationDao
import com.example.mygarage.notificationManager.modelNotification.Notification
import com.example.mygarage.notificationManager.ReminderWorker
import com.example.mygarage.notificationManager.ReminderWorker.Companion.carId
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class NotificationManagerViewModel(
    val application: Application,
    private val notificationDao: NotificationDao
) : ViewModel() {
    val allNotification: LiveData<List<Notification>> =
        notificationDao.getNotifications().asLiveData()

    internal fun scheduleReminder(
        duration: Long,
        unit: TimeUnit,
        title: String,
        content: String,
        carId: Long,
        carBrand: String,
        carModel: String,
        carMileage: String
    ) {
        val dataToWorker: Data =
            workDataOf(
                ReminderWorker.title to title,
                ReminderWorker.content to content,
                ReminderWorker.carId to carId,
                ReminderWorker.carBrand to carBrand,
                ReminderWorker.carModel to carModel
            )

        val carRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInputData(dataToWorker)
            .setInitialDelay(duration, unit)
            .build()

        WorkManager
            .getInstance(application)
            .enqueueUniqueWork(carId.toString(), ExistingWorkPolicy.REPLACE, carRequest)

        //Adding notification to the database
        val notification =
            Notification(carId = carId, title = title, content = content, brand = carBrand, model = carModel, mileage = carMileage)
        viewModelScope.launch {
            notificationDao.insert(notification)
        }
    }

    fun deleteNotification(notification: Notification) {
        viewModelScope.launch {
            notificationDao.delete(notification)
        }
    }

    fun deleteNotificationByCarId(carId: Long) {
        viewModelScope.launch {
            notificationDao.deleteNotificationById(carId)
        }
    }

    fun getNotificationById(id: Long){
        viewModelScope.launch {
            notificationDao.getNotificationById(id)
        }
    }
}


class NotificationManagerViewModelFactory(
    private val application: Application,
    private val notificationDao: NotificationDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(NotificationManagerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            NotificationManagerViewModel(application, notificationDao) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

