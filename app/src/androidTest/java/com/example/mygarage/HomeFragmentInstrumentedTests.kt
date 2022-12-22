package com.example.mygarage

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.mygarage.data.CarDao
import com.example.mygarage.data.CarDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeFragmentInstrumentedTests {
    @get:Rule()
    val activity = ActivityScenarioRule(MainActivity::class.java)
    private lateinit var db: CarDatabase
    private lateinit var dao: CarDao
    val context = ApplicationProvider.getApplicationContext<Context>()
    val car = CarList.random()

    @Before
    fun setUp() {
        // initialize the db and dao variable
        db = Room.inMemoryDatabaseBuilder(context, CarDatabase::class.java).build()
        dao = db.CarDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun click_card_and_delete_car() {
        test_if_add_car_fragment_is_showedUp_and_if_works()
        Thread.sleep(2000)
        click_on_card(0)
        Thread.sleep(3000)
        click_delete_car_fab()
        Thread.sleep(2000)
        click_negative_for_delete_alert_dialog()
    }

    @Test
    fun test_if_add_car_fragment_is_showedUp_and_if_works() = runBlocking {
        click_add_new_car_fab()
        Thread.sleep(1000)
        add_new_car_text_input(car)
        Thread.sleep(4000)
        click_on_id(R.id.button_add_new_car)
    }
}