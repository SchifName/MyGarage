package com.example.mygarage.notificationManager

import com.example.mygarage.BaseApplication
import com.example.mygarage.MainActivity
import com.example.mygarage.R
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters


class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters,
) : Worker(context, workerParams) {
    // Arbitrary id number
    private val notificationId = 1

    override fun doWork(): Result {

        //Get values in input from CarWorkerViewModel
        val carModel = inputData.getString(carModel)
        val carBrand = inputData.getString(carBrand)
        val carIdLong = inputData.getLong(carId, 0)
        val title = inputData.getString(title)
        val content = inputData.getString(content)
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = FLAG_ACTIVITY_CLEAR_TOP or FLAG_ACTIVITY_SINGLE_TOP
            putExtra("ID", carIdLong)
        }
        val pendingIntent: PendingIntent = PendingIntent
            .getActivity(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_MUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

        val builder = NotificationCompat.Builder(applicationContext, BaseApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_baseline_directions_car_filled_24)
            .setContentTitle("$title")
            .setContentText("$content")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationId, builder.build())
        }
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
