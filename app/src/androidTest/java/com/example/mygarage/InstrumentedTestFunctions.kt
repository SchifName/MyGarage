package com.example.mygarage

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.assertion.ViewAssertions.matches

fun go_to_home_fragment(){
    onView(withId(R.id.navigation_home))
        .perform()
}

fun go_to_notification_fragment(){
    onView(withId(R.id.navigation_notifications))
        .perform()
}

fun click_add_new_car_fab(){
    onView(withId(R.id.add_fab))
        .perform()
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

fun click_text_input_and_type(idTextInput: Int, string: String){
    Thread.sleep(3000)
    click_on_id(idTextInput)
    Thread.sleep(2000)
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