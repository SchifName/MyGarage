package com.example.mygarage

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.mygarage.ui.addCar.AddNewCarFragment
import com.example.mygarage.ui.notifications.NotificationsFragment
import com.example.mygarage.ui.home.HomeFragment
import org.mockito.Mockito

val mockNavController = Mockito.mock(NavController::class.java)
val themeApplication = R.style.Theme_MyGarage

fun startHomeFragment():NavController{
    launchFragmentInContainer<HomeFragment>(themeResId = themeApplication) {
        HomeFragment().also { fragment ->
            fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(fragment.requireView(), mockNavController)
                }
            }
        }
    }
    return mockNavController
}

fun startNotificationFragment():NavController{
    launchFragmentInContainer<NotificationsFragment>(themeResId = themeApplication) {
        NotificationsFragment().also { fragment ->
            fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(fragment.requireView(), mockNavController)
                }
            }
        }
    }
    return mockNavController
}

fun startAddNewCarFragment():NavController{
    launchFragmentInContainer<AddNewCarFragment>(themeResId = themeApplication) {
        AddNewCarFragment().also { fragment ->
            fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                if (viewLifecycleOwner != null) {
                    Navigation.setViewNavController(fragment.requireView(), mockNavController)
                }
            }
        }
    }
    return mockNavController
}
