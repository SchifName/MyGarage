package com.example.mygarage.ui.carDetail

import android.app.AlertDialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mygarage.BaseApplication
import com.example.mygarage.R
import com.example.mygarage.databinding.FragmentCarDetailBinding
import com.example.mygarage.model.*
import com.example.mygarage.ui.home.setAndGetUriByBrandParsingListOfLogoAndImageView
import com.google.android.material.bottomnavigation.BottomNavigationView

class CarDetailFragment : Fragment() {

    private val carDetailArgs: CarDetailFragmentArgs by navArgs()
    private lateinit var car: Car

    private val detailCarViewModel: CarDetailViewModel by viewModels {
        CarDetailViewModelFactory(
            (activity?.application as BaseApplication).database.CarDao()
        )
    }

    private var _binding: FragmentCarDetailBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arg = arguments?.getLong("CarIdNotification")
        val navBar: BottomNavigationView =
            requireActivity().findViewById(R.id.nav_view)
        navBar.visibility = View.GONE
        var id = carDetailArgs.carId
        if (arg != null && arg > 0)
            id = arg
        if (id > 0) {
            detailCarViewModel.getCarById(id).observe(this.viewLifecycleOwner) { carSelected ->
                car = carSelected
                bindCar(car)
            }
        }
        binding.deleteFab.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            //set title
            builder.setTitle(getString(R.string.delete_dialog_title))
            //set message
            builder.setMessage(getString(R.string.delete_dialog_message))
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //positive action
            builder.setPositiveButton(getString(R.string.delete_dialog_positive_button)) { _, _ ->
                deleteCar(id)
                val action = CarDetailFragmentDirections.actionCarDetailFragmentToNavigationHome()
                val navBar: BottomNavigationView = requireActivity().findViewById(R.id.nav_view)
                navBar.visibility = View.VISIBLE
                this.findNavController().navigate(action)
            }

            //negative action
            builder.setNegativeButton(getString(R.string.delete_dialog_negative_button)) { _, _ ->

            }

            val alertDialog: AlertDialog = builder.create()
            //other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }

        binding.modifyFab.setOnClickListener{
            val action = CarDetailFragmentDirections.actionCarDetailFragmentToAddNewCarFragment(id)
            this.findNavController().navigate(action)
        }
    }

    private fun bindCar(car: Car) {
        binding.apply {
            brandDetail.text = car.brand
            modelDetail.text = car.model
            priceTitle.text = getString(R.string.price_detail_string, formatCurrency(car.price))
            carPowerDetail.text = getString(R.string.power_detail_string, carPowerWithUnitString(car.power))
            carMileageDetail.text = getString(R.string.mileage_detail_string, carMileageWithUnitString(car.mileage))
            carFuelDetail.text = getString(R.string.fuel_type_detail_string, car.fuelType)
            carProductionDetail.text = getString(R.string.year_detail_string, car.yearOfProduction.toString())
            val observer = Observer<List<CarLogo>> {
                setAndGetUriByBrandParsingListOfLogoAndImageView(
                    detailCarViewModel.logoDataApi.value,
                    car.brand,
                    binding.logoDetail
                )
            }
            detailCarViewModel.logoDataApi.observe(viewLifecycleOwner, observer)
            if (car.image != null) {
                val bmp = BitmapFactory.decodeByteArray(car.image, 0, car.image.size)
                binding.detailCarImage.setImageBitmap(
                    bmp
                )
            } else {
                binding.detailCarImage.setImageResource(R.drawable.ic_baseline_directions_car_filled_24)
            }
        }
    }

    private fun deleteCar(id: Long){
        detailCarViewModel.deleteCarById(id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}