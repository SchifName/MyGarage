package com.example.mygarage

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.mygarage.model.Car

fun go_to_home_fragment(){
    onView(withId(R.id.navigation_home))
        .perform(click())
}

fun go_to_notification_fragment(){
    onView(withId(R.id.navigation_notifications))
        .perform(click())
}

fun click_add_new_car_fab(){
    onView(withId(R.id.add_fab))
        .perform(click())
}

fun navigate_to_home_to_add_new_car(){
    go_to_home_fragment()
    click_add_new_car_fab()
}

fun click_on_card(idCard: Int) {
    onView(withId(R.id.recycler_view)).perform(
        RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(idCard, click())
    )
}

fun click_on_id(idTextInput: Int){
    onView(withId(idTextInput))
        .perform(click())
}

fun write_on_text_input(idTextInput: Int, string :String){
    onView(withId(idTextInput))
        .perform(typeText(string))
}

fun add_new_car_text_input(car: Car){
    click_text_input_and_type(R.id.car_brand_add_text,car.brand)
    hide_keyboard()
    click_text_input_and_type(R.id.car_year_add_text,car.yearOfProduction.toString())
    hide_keyboard()
    click_text_input_and_type(R.id.car_model_add_text,car.model)
    hide_keyboard()
    click_text_input_and_type(R.id.car_fuel_type_add_text,car.fuelType)
    hide_keyboard()
    click_text_input_and_type(R.id.car_power_add_text,car.power.toString())
    hide_keyboard()
    click_text_input_and_type(R.id.car_price_add_text,car.price.toString())
    hide_keyboard()
    click_text_input_and_type(R.id.car_mileage_add_text,car.mileage.toString())
}

fun click_text_input_and_type(idTextInput: Int, string: String){
    Thread.sleep(500)
    click_on_id(idTextInput)
    Thread.sleep(500)
    write_on_text_input(idTextInput, string)
}

fun clear_text_input(idTextInput: Int){
    onView(withId(idTextInput))
        .perform(clearText())
}

fun click_modify_car_fab(){
    onView(withId(R.id.modify_fab))
        .perform(click())
}

fun click_button(idButton: Int){
    onView(withId(idButton))
        .perform(click())
}

fun click_delete_car_fab(){
    onView(withId(R.id.delete_fab))
        .perform(click())
}

fun click_positive_for_delete_alert_dialog(){
    onView(withId((android.R.id.button1)))
        .perform(click())
}

fun click_negative_for_delete_alert_dialog(){
    onView(withId(android.R.id.button2))
        .perform(click())
}

fun hide_keyboard(){
    onView((ViewMatchers.isRoot())).perform(closeSoftKeyboard())
}

fun scroll_to(id :Int){
    onView(withId(id))
        .perform(scrollTo())
        .check(matches(isDisplayed()))
}