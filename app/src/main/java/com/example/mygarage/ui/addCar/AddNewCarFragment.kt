package com.example.mygarage.ui.addCar

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils.concat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mygarage.BaseApplication
import com.example.mygarage.R
import com.example.mygarage.databinding.FragmentAddNewCarBinding
import com.example.mygarage.model.Car
import com.example.mygarage.notificationManager.viewModelNotificationManager.NotificationManagerViewModel
import com.example.mygarage.notificationManager.viewModelNotificationManager.NotificationManagerViewModelFactory
import com.google.android.material.snackbar.Snackbar
import java.util.*
import java.util.concurrent.TimeUnit


class AddNewCarFragment : Fragment() {

    private val REQUEST_CODE = 100

    private val error = ""

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

    private val notificationViewModel: NotificationManagerViewModel by viewModels {
        NotificationManagerViewModelFactory(
            requireActivity().application,
            (activity?.application as BaseApplication).notDatabase.NotificationDao()
        )
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
        binding.apply {
            if (carAddArgs.carId2 != 0L)
                bindModCar()
            buttonAddNewCar.setOnClickListener {
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

    private fun snackBarBlank(): Int {
        return when (error.isBlank()) {
            binding.carBrandAddText.text.toString().isBlank() -> R.string.blank_brand
            binding.carYearAddText.text.toString().isBlank() -> R.string.error_year
            binding.carModelAddText.text.toString().isBlank() -> R.string.blank_model
            binding.carFuelTypeAddText.text.toString().isBlank() -> R.string.blank_fuel
            binding.carPowerAddText.text.toString().isBlank() -> R.string.error_power
            binding.carPriceAddText.text.toString().isBlank() -> R.string.error_price
            binding.carMileageAddText.text.toString().isBlank() -> R.string.error_km
            else -> R.string.error_snackbar
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
            view?.let { Snackbar.make(it, snackBarBlank(), Snackbar.LENGTH_SHORT).show() }
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

    @SuppressLint("StringFormatInvalid")
    private fun modifyCar() {
        if (isValidCar()) {

            val id = carAddArgs.carId2
            val brand = binding.carBrandAddText.text.toString()
            val model = binding.carModelAddText.text.toString()
            val mileage = binding.carMileageAddText.text.toString()

            if (binding.carMileageAddText.text.toString().toDouble() >= 100000){
                notificationViewModel.scheduleReminder(
                    4,
                    TimeUnit.SECONDS,
                    getString(
                        R.string.service_car_expired_text, brand
                    ),
                    getString(R.string.service_car_context_text, brand, model),
                    id,
                    brand,
                    model,
                    mileage
                )
            }

            addNewCarViewModel.modCar(
                id = id,
                Brand = brand,
                YearOfProduction = binding.carYearAddText.text.toString().toInt(),
                Model = model,
                FuelType = binding.carFuelTypeAddText.text.toString(),
                Power = binding.carPowerAddText.text.toString().toInt(),
                Price = binding.carPriceAddText.text.toString().toDouble(),
                Mileage = mileage.toDouble(),
                Image = checkIfInsertIsNull(createBitmapFromView(binding.imageViewAddImage))
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
