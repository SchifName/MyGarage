package com.example.mygarage.notificationManager.viewModelNotificationManager


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.example.mygarage.notificationManager.ReminderWorker
import java.util.concurrent.TimeUnit


class NotificationManagerViewModel(val application: Application) : ViewModel() {
    internal fun scheduleReminder(
        duration: Long,
        unit: TimeUnit,
        title: String,
        content: String,
        carId: Long,
        carBrand: String,
        carModel: String,
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
    }
}


class NotificationManagerViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(NotificationManagerViewModel::class.java)) {
            NotificationManagerViewModel(application) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}

