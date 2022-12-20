package com.example.mygarage

import android.content.Context
import androidx.navigation.NavController
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mygarage.data.CarDao
import com.example.mygarage.data.CarDatabase
import com.example.mygarage.ui.addCar.AddNewCarFragmentDirections
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class AddNewCarFragmentInstrumentedTests {
    private lateinit var dao: CarDao
    private lateinit var db: CarDatabase
    val context = ApplicationProvider.getApplicationContext<Context>()
    @get:Rule()
    val activity = ActivityScenarioRule(MainActivity::class.java)
    val car = CarList.random()

    @Before
    fun setUp(){
        db = Room.inMemoryDatabaseBuilder(context, CarDatabase::class.java).build()
        dao = db.CarDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun test_if_add_car_fragment_is_showedUp_and_if_works() = runBlocking {
        click_add_new_car_fab()
        Thread.sleep(2000)
        add_new_car_text_input(car)
        Thread.sleep(2000)
        hide_keyboard()
        Thread.sleep(2000)
        click_on_id(R.id.button_add_new_car)
        //startAddNewCarFragment()
        /*verify(mockNavController).navigate(
            AddNewCarFragmentDirections.actionAddNewCarFragmentToNavigationHome()
        )*/
    }
}
