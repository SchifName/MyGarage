package com.example.mygarage.ui.addCar

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
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
import com.example.mygarage.utils.checkInternet
import com.example.mygarage.utils.fuelTypeAlertDialog
import com.google.android.material.snackbar.Snackbar
import java.util.*
import java.util.concurrent.TimeUnit


class AddNewCarFragment : Fragment() {

    private val REQUEST_CODE = 100

    private val error = ""

    private val carAddArgs: AddNewCarFragmentArgs by navArgs()
    private lateinit var car: Car


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

        addNewCarViewModel.brandAcquisition(view)

        binding.apply {
            carFuelTypeAddText.setOnClickListener {
                fuelTypeAlertDialog(requireContext(), carFuelTypeAddText)
            }

            carBrandAddText.setOnClickListener {
                setBrandCar()
            }

            carModelAddText.setOnClickListener {
                setModelCar()
            }

            if (carAddArgs.carId2 != 0L)
                bindModCar()
            buttonAddNewCar.setOnClickListener {
                if (carAddArgs.carId2 == 0L)
                    addNewCar()
                else {
                    if (car.image != null)
                        binding.imageViewAddImage.tag = "is_not_null"
                    modifyCar(car)
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
            view?.let { Snackbar.make(it, snackBarBlank(), Snackbar.LENGTH_SHORT).show() }
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

    /**
     * Private function for the appearance of an AlertDialog to select the car brand via API
     */
    private fun setBrandCar() {
        if(checkInternet(context)){

            val listBrandCar = addNewCarViewModel.getBrand()
            val items = arrayOfNulls<CharSequence>(listBrandCar.size)
            for (i in listBrandCar.indices) {
                items[i] = listBrandCar[i]
            }
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle(R.string.choose_car)
            builder.setSingleChoiceItems(
                items,
                addNewCarViewModel.checkedItemBrand
            ) { _: DialogInterface, which ->
                addNewCarViewModel.checkedItemBrand = which
            }
            builder.setItems(items) { _: DialogInterface, which ->
                addNewCarViewModel.checkedItemBrand = which
            }
            builder.setPositiveButton(R.string.delete_car_dialog_positive_button) { _: DialogInterface, _ ->
                if (addNewCarViewModel.checkedItemBrand != -1) {
                    binding.carBrandAddText.setText(items[addNewCarViewModel.checkedItemBrand].toString())
                    //binding.carModelAddText.setText("")
                    binding.carModelAddText.text = null
                } else {
                    //binding.carBrandAddText.setText("")
                    binding.carBrandAddText.text = null
                    view?.let { Snackbar.make(it, R.string.no_brand, Snackbar.LENGTH_SHORT).show() }
                }
            }
            builder.setNegativeButton(R.string.delete_car_dialog_negative_button) { _: DialogInterface, _ ->
                //binding.carBrandAddText.setText("")
                binding.carBrandAddText.text = null
            }
            builder.show()

        }else{
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Required an internet connection")
            builder.setNeutralButton("Ok"){ _: DialogInterface, _ ->
                view?.let { Snackbar.make(it, "Make sure to activate an internet connection", Snackbar.LENGTH_SHORT).show() }
            }
            builder.show()
        }
    }

    /**
     * Private function for the appearance of an AlertDialog to select the model of the selected brand via API
     */
    private fun setModelCar() {
        if(checkInternet(context)){

            val listModelCar = addNewCarViewModel.getModel(binding.carBrandAddText.text.toString())
            val itemsCar = arrayOfNulls<CharSequence>(listModelCar.size)
            for (i in listModelCar.indices) {
                itemsCar[i] = listModelCar[i]
            }
            val builderModel: AlertDialog.Builder = AlertDialog.Builder(context)
            builderModel.setTitle(R.string.choose_model)
            builderModel.setSingleChoiceItems(
                itemsCar,
                addNewCarViewModel.checkedItemModel
            ) { _: DialogInterface, which ->
                addNewCarViewModel.checkedItemModel = which
            }
            builderModel.setItems(itemsCar) { _: DialogInterface, which ->
                addNewCarViewModel.checkedItemModel = which
            }
            builderModel.setPositiveButton(R.string.delete_car_dialog_positive_button) { _: DialogInterface, _ ->
                if (addNewCarViewModel.checkedItemModel != -1) {
                    binding.carModelAddText.setText(itemsCar[addNewCarViewModel.checkedItemModel].toString())
                } else {
                    //binding.carModelAddText.setText("")
                    binding.carModelAddText.text = null
                    view?.let { Snackbar.make(it, R.string.no_model, Snackbar.LENGTH_SHORT).show() }
                }
            }
            builderModel.setNegativeButton(R.string.delete_car_dialog_negative_button) { _: DialogInterface, _ ->
                //binding.carModelAddText.setText("")
                binding.carModelAddText.text = null
            }
            builderModel.show()

        }else{
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setTitle("Required an internet connection")
            builder.setNeutralButton("Ok"){ _: DialogInterface, _ ->
                view?.let { Snackbar.make(it, "Make sure to activate an internet connection", Snackbar.LENGTH_SHORT).show() }
            }
            builder.show()
        }

    }


    @SuppressLint("StringFormatInvalid")
    private fun modifyCar(car: Car) {
        if (isValidCar()) {

            val id = carAddArgs.carId2
            val brand = binding.carBrandAddText.text.toString()
            val model = binding.carModelAddText.text.toString()
            val mileage = binding.carMileageAddText.text.toString()

            if (binding.carMileageAddText.text.toString().toDouble() >= car.mileage + 100000){
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
            view?.let { Snackbar.make(it, snackBarBlank(), Snackbar.LENGTH_SHORT).show() }
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
