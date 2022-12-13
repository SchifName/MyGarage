package com.example.mygarage

import android.app.Application
import com.example.mygarage.data.CarDatabase

class BaseApplication : Application() {
    val database: CarDatabase by lazy { CarDatabase.getDatabase(this) }
    companion object {
        const val CHANNEL_ID = "car_reminder_id"
    }
}