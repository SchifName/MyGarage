package com.example.mygarage

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.mygarage.data.CarDao
import com.example.mygarage.data.CarDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class CarDatabaseTest {
    private lateinit var dao: CarDao
    private lateinit var db: CarDatabase
    val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setUp(){
        db = Room.inMemoryDatabaseBuilder(context, CarDatabase::class.java).build()
        dao = db.CarDao()
    }

    @After
    fun closeDb(){
        db.close()
    }

    @Test
    fun test_if_an_addedCar_return_theItem_inFlow() = runBlocking {
        val car = CarListDao[1]
        dao.insert(car)
        dao.getCars().test {
            val list = awaitItem()
            Log.d("test", list.toString())
            assert(list.contains(car))
            cancel()
        }
    }

    @Test
    fun test_if_a_deletedCar_isNot_inTheFlow() = runBlocking {
        val car = CarListDao[0]
        dao.insert(car)
        dao.delete(car)
        dao.getCars().test {
            val list = awaitItem()
            Log.d("test", list.toString())
            assert(!list.contains(car))
            cancel()
        }
    }

    @Test
    fun test_if_aCar_taken_with_getCarById_is_inFlow() = runBlocking {
        val car = CarListDao[0]
        val car1 = CarListDao[1]
        dao.insert(car)
        dao.insert(car1)
        dao.getCarById(car.id).test {
            val list = awaitItem()
            assert(list == car)
            cancel()
        }
    }

    @Test
    fun test_if_a_deletedCarById_isNot_inTheFlow() = runBlocking{
        val car = CarListDao[0]
        val car1 = CarListDao[1]
        dao.insert(car)
        dao.insert(car1)
        dao.deleteCarById(car.id)
        dao.getCars().test {
            val list = awaitItem()
            assert(!list.contains(car))
        }
    }
}