package com.example.mygarage.data

import androidx.room.*
import com.example.mygarage.model.Car
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {
    @Query("SELECT * FROM Car")
    fun getCars(): Flow<List<Car>>

    @Query("SELECT * FROM Car WHERE id = :id")
    fun getCarById(id: Long): Flow<Car>

    @Query("DELETE FROM Car WHERE id = :id")
    suspend fun deleteCarById(id: Long)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(car: Car)

    @Update
    fun update(car: Car)

    @Delete
    suspend fun delete(car: Car)
}