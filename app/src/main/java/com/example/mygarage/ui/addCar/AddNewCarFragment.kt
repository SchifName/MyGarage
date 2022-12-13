package com.example.mygarage.ui.addCar

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mygarage.BaseApplication
import com.example.mygarage.R
import com.example.mygarage.databinding.FragmentAddNewCarBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.widget.*
import androidx.navigation.fragment.navArgs
import com.example.mygarage.model.Car
import com.example.mygarage.notificationManager.viewModelNotificationManager.NotificationManagerViewModel
import com.example.mygarage.notificationManager.viewModelNotificationManager.NotificationManagerViewModelFactory
import java.util.*
import java.util.concurrent.TimeUnit


class AddNewCarFragment : Fragment() {

    private val REQUEST_CODE = 100

    private val carAddArgs: AddNewCarFragmentArgs by navArgs()
    private lateinit var car: Car

    private lateinit var viewModel: AddNewCarViewModel

    private val addNewCarViewModel: AddNewCarViewModel by viewModels {
        AddNewCarViewModelFactory(
            (activity?.application as BaseApplication).database.CarDao()
        )
    }

    private var _binding: FragmentAddNewCarBinding? = null
    private val binding get() = _binding!!

    private val NotificationviewModel: NotificationManagerViewModel by viewModels {
        NotificationManagerViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddNewCarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navBar.visibility = View.GONE
        binding.apply {
            if (carAddArgs.carId2 != 0L)
                bindModCar()
            buttonAddNewCar.setOnClickListener {
                navBar.visibility = View.VISIBLE
                if (carAddArgs.carId2 == 0L)
                    addNewCar()
                else {
                    if (car.image != null)
                        binding.imageViewAddImage.tag = "is_not_null"
                    modifyCar()
                }
            }
            imageViewAddImage.setOnClickListener {
                openGallery()
            }
        }
    }

    private fun bindModCar() {
        addNewCarViewModel.getCarById(carAddArgs.carId2)
            .observe(this.viewLifecycleOwner) { carSelected ->
                car = carSelected
                bindCar(car)
            }
    }

    private fun bindCar(car: Car) {
        binding.apply {
            carBrandAddText.setText(car.brand)
            carYearAddText.setText(car.yearOfProduction.toString())
            carModelAddText.setText(car.model)
            carFuelTypeAddText.setText(car.fuelType)
            carPowerAddText.setText(car.power.toString())
            carPriceAddText.setText(car.price.toString())
            carMileageAddText.setText(car.mileage.toString())
            if (car.image != null) {
                val bmp = BitmapFactory.decodeByteArray(car.image, 0, car.image.size)
                binding.imageViewAddImage.setImageBitmap(
                    bmp
                )
            } else {
                binding.imageViewAddImage.setImageResource(R.drawable.ic_baseline_directions_car_filled_24)
            }
        }
    }

    private fun addNewCar() {
        if (isValidCar()) {
            addNewCarViewModel.addCar(
                Brand = binding.carBrandAddText.text.toString(),
                YearOfProduction = binding.carYearAddText.text.toString().toInt(),
                Model = binding.carModelAddText.text.toString(),
                FuelType = binding.carFuelTypeAddText.text.toString(),
                Power = binding.carPowerAddText.text.toString().toInt(),
                Price = binding.carPriceAddText.text.toString().toDouble(),
                Mileage = binding.carMileageAddText.text.toString().toDouble(),
                Image = checkIfInsertIsNull(createBitmapFromView(binding.imageViewAddImage))
            )
            val action = AddNewCarFragmentDirections.actionAddNewCarFragmentToNavigationHome()
            findNavController().navigate(action)
        } else {
            Log.d("error", "errore nella validità")
        }
    }

    private fun isValidCar(): Boolean {
        return try {
            addNewCarViewModel.checkInputEditTextNewCar(
                binding.carBrandAddText.text.toString(),
                binding.carYearAddText.text.toString().toInt(),
                binding.carModelAddText.text.toString(),
                binding.carFuelTypeAddText.text.toString(),
                binding.carPowerAddText.text.toString().toInt(),
                binding.carPriceAddText.text.toString().toDouble(),
                binding.carMileageAddText.text.toString().toDouble()
            )
        } catch (e: Exception) {
            Log.d("valid car", "errore in isValidCar " + e)
            false
        }
    }

    private fun checkIfInsertIsNull(image: Bitmap): Bitmap? {
        return if (binding.imageViewAddImage.tag == "is_not_null") {
            image
        } else {
            null
        }
    }

    private fun modifyCar() {
        if (isValidCar()) {
            addNewCarViewModel.modCar(
                id = carAddArgs.carId2,
                Brand = binding.carBrandAddText.text.toString(),
                YearOfProduction = binding.carYearAddText.text.toString().toInt(),
                Model = binding.carModelAddText.text.toString(),
                FuelType = binding.carFuelTypeAddText.text.toString(),
                Power = binding.carPowerAddText.text.toString().toInt(),
                Price = binding.carPriceAddText.text.toString().toDouble(),
                Mileage = binding.carMileageAddText.text.toString().toDouble(),
                Image = checkIfInsertIsNull(createBitmapFromView(binding.imageViewAddImage))
            )
            if (binding.carMileageAddText.text.toString().toDouble() >= 100000) NotificationviewModel.scheduleReminder(
                5,
                TimeUnit.SECONDS,
                getString(
                    R.string.service_car_expired_text, binding.carBrandAddText.text.toString()
                ),
                getString(R.string.service_car_context_text, binding.carBrandAddText.text.toString(), binding.carModelAddText.text.toString()),
                carAddArgs.carId2,
                binding.carBrandAddText.text.toString(),
                binding.carModelAddText.text.toString()
            )
            val action =
                AddNewCarFragmentDirections.actionAddNewCarFragmentToCarDetailFragment(carAddArgs.carId2)
            findNavController().navigate(action)
        } else {
            Log.d("error", "errore nella validità")
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            val selectedImage = data?.data
            if (selectedImage != null) {
                // handle chosen image
                binding.imageViewAddImage.setImageURI(data.data)
                binding.imageViewAddImage.tag = "is_not_null"
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun createBitmapFromView(view: View): Bitmap {
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        return view.drawingCache
    }
}
