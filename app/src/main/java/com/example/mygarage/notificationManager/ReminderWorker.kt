package com.example.mygarage.notificationManager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters


class ReminderWorker(val context: Context, params: WorkerParameters) : Worker(context, params){
    override fun doWork(): Result {
        NotificationHelper(context).createNotification(
            title = inputData.getString(title).toString(),
            message = inputData.getString(content).toString()
        )

        return Result.success()
    }

    companion object {
        const val carBrand = "BRAND"
        const val carId = "ID"
        const val carModel = "MODEL"
        const val title = "TITLE"
        const val content = "CONTENT"
    }

}
