package com.example.mygarage.modelNotification

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Notification")
data class Notification(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo val title: String,
    @ColumnInfo val content: String,
    @ColumnInfo val brand: String,
    @ColumnInfo val model: String
    )