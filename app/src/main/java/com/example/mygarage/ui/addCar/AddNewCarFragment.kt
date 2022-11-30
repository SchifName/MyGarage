package com.example.mygarage.ui.addCar

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


class AddNewCarFragment : Fragment() {

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
        _binding = FragmentAddNewCarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
        navBar.visibility = View.GONE
        binding.apply {
            buttonAddNewCar.setOnClickListener{
                addNewCar()
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
                //Image = checkIfInsertIsNull(createBitmapFromView(binding.carImage1))
                Image = null
            )
            val action = AddNewCarFragmentDirections.
            actionAddNewCarFragmentToNavigationHome()
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
            Log.d("error", "errore in isValidCar "+e)
            false
        }
    }
}
