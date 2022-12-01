package com.example.mygarage.ui.carDetail

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.mygarage.BaseApplication
import com.example.mygarage.R
import com.example.mygarage.databinding.FragmentCarDetailBinding
import com.example.mygarage.model.Car
import com.example.mygarage.model.carMileageWithUnitString
import com.example.mygarage.model.carPowerWithUnitString
import com.example.mygarage.model.formatCurrency
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
    }

    private fun bindCar(car: Car) {
        binding.apply {
            brandDetail.text = car.brand
            modelDetail.text = car.model
            priceTitle.text = getString(R.string.price_detail_string, formatCurrency(car.price))
            textView4.text = getString(R.string.power_detail_string, carPowerWithUnitString(car.power))
            textView5.text = getString(R.string.mileage_detail_string, carMileageWithUnitString(car.mileage))
            textView9.text = getString(R.string.fuel_type_detail_string, car.fuelType)
            textView8.text = getString(R.string.year_detail_string, car.yearOfProduction.toString())

            if (car.image != null) {
                val bmp = BitmapFactory.decodeByteArray(car.image, 0, car.image.size)
                binding.detailCarImage.setImageBitmap(
                    Bitmap.createScaledBitmap(
                        bmp,
                        1920,
                        1080,
                        false
                    )
                )
            } else {
                binding.detailCarImage.setImageResource(R.drawable.ic_baseline_directions_car_filled_24)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}