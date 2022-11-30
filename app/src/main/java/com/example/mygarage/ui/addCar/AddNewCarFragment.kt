package com.example.mygarage.ui.addCar

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mygarage.BaseApplication
import com.example.mygarage.R
import com.example.mygarage.databinding.FragmentAddNewCarBinding
import com.google.android.material.snackbar.Snackbar

class AddNewCarFragment : Fragment() {

    companion object {
        fun newInstance() = AddNewCarFragment()
    }

    private lateinit var viewModel: AddNewCarViewModel

    private val addNewCarViewModel: AddNewCarViewModel by viewModels {
        AddNewCarViewModelFactory(
            (activity?.application as BaseApplication).database.CarDao()
        )
    }

    private var _binding: FragmentAddNewCarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_new_car, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AddNewCarViewModel::class.java)
        // TODO: Use the ViewModel
    }
    private fun addNewCar() {
        if (isValidCar()) {
            addNewCarViewModel.addCar(
                Brand = binding.carBrandAddText.text.toString(),
                YearOfProduction = binding.carYearAddText.text.toString().toInt(),
                Model = binding.carModelAddText.text.toString(),
                FuelType = binding.carFuelTypeAddText.text.toString(),
                Power = binding.carPowerAddText,
                Price = binding.carPriceAddText,
                Mileage = binding.carMileageAddText.text.toString().toDouble(),
                Image = checkIfInsertIsNull(createBitmapFromView(binding.carImage1))
            )
            val action = AddNewCarFragmentDirections.
            actionAddNewCarFragmentToNavigationHome()
            findNavController().navigate(action)
        } else {
            /*showCustomSnackBar(
                binding.constraintLayoutAddNewCar,
                getString(R.string.error_add_car_toast),
                Snackbar.LENGTH_LONG
            )*/
        }
    }

    private fun isValidCar(): Boolean {
        return try {
            addNewCarViewModel.checkInputEditTextNewCar(
                binding.carBrandAddText.text.toString(),
                binding.carYearAddText.text.toString().toInt(),
                binding.carModelAddText.text.toString(),
                binding.carFuelTypeAddText.text.toString(),
                binding.carPowerAddText,
                binding.carPriceAddText,
                binding.carMileageAddText.text.toString().toDouble()
            )
        } catch (e: Exception) {
            showCustomSnackBar(
                binding.constraintLayoutAddNewCar,
                getString(R.string.error_add_car_toast),
                Snackbar.LENGTH_LONG
            )
            false
        }
    }
}
