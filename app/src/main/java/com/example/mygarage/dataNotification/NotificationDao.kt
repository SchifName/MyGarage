package com.example.mygarage.dataNotification

import androidx.room.*
import com.example.mygarage.modelNotification.Notification
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    @Query("SELECT * FROM Notification")
    fun getNotifications(): Flow<List<Notification>>

    @Query("SELECT * FROM Notification WHERE id = :id")
    fun getNotificationById(id : Long): Flow<Notification>

    @Query("DELETE FROM NOTIFICATION WHERE id = :id")
    suspend fun deleteNotificationById(id: Long)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(notification: Notification)

    @Delete
    suspend fun delete(notification: Notification)
}