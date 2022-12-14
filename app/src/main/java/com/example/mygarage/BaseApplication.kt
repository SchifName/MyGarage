package com.example.mygarage

import android.app.Application
import com.example.mygarage.data.CarDatabase
import com.example.mygarage.notificationManager.dataNotification.NotificationDatabase

class BaseApplication : Application() {
    val database: CarDatabase by lazy { CarDatabase.getDatabase(this) }
    val notDatabase : NotificationDatabase by lazy { NotificationDatabase.getDatabase(this) }
    companion object {
        const val CHANNEL_ID = "car_reminder_id"
    }
}