package com.example.mygarage.ui.carDetail

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.mygarage.BaseApplication
import com.example.mygarage.R
import com.example.mygarage.databinding.FragmentCarDetailBinding
import com.example.mygarage.model.*
import com.example.mygarage.notificationManager.viewModelNotificationManager.NotificationManagerViewModel
import com.example.mygarage.notificationManager.viewModelNotificationManager.NotificationManagerViewModelFactory
import com.example.mygarage.ui.home.setAndGetUriByBrandParsingListOfLogoAndImageView

class CarDetailFragment : Fragment() {

    private val carDetailArgs: CarDetailFragmentArgs by navArgs()
    private lateinit var car: Car

    private val detailCarViewModel: CarDetailViewModel by viewModels {
        CarDetailViewModelFactory(
            (activity?.application as BaseApplication).database.CarDao()
        )
    }

    private val notificationViewModel: NotificationManagerViewModel by viewModels {
        NotificationManagerViewModelFactory(
            requireActivity().application,
            (activity?.application as BaseApplication).notDatabase.NotificationDao()
        )
    }

    private var _binding: FragmentCarDetailBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentCarDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        detailCarViewModel.arg = arguments?.getLong("CarIdNotification")
        detailCarViewModel.idCar = carDetailArgs.carId
        var actualArg = detailCarViewModel.arg
        var idCar = detailCarViewModel.idCar
        if (actualArg != null && actualArg > 0) idCar = actualArg
        if (idCar > 0) {
            detailCarViewModel.getCarById(idCar).observe(this.viewLifecycleOwner) { carSelected ->
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
            carPowerDetail.text =
                getString(R.string.power_detail_string, carPowerWithUnitString(car.power))
            carMileageDetail.text =
                getString(R.string.mileage_detail_string, carMileageWithUnitString(car.mileage))
            carFuelDetail.text = getString(R.string.fuel_type_detail_string, car.fuelType)
            carProductionDetail.text =
                getString(R.string.year_detail_string, car.yearOfProduction.toString())
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
                binding.detailCarImage.setImageResource(R.drawable.ic_base_image)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.appbar_menu_fragment_detail, menu)
    }

    //TODO QUI DENTRO
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.modify_option -> {
                val action = CarDetailFragmentDirections.actionCarDetailFragmentToAddNewCarFragment(detailCarViewModel.idCar)
                this.findNavController().navigate(action)
                true
            }
            R.id.delete_option -> {
                val builder = AlertDialog.Builder(requireContext())
                //set title
                builder.setTitle(getString(R.string.delete_dialog_title))
                //set message
                builder.setMessage(getString(R.string.delete_dialog_message))
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //positive action
                builder.setPositiveButton(getString(R.string.delete_dialog_positive_button)) { _, _ ->
                    deleteCar(detailCarViewModel.idCar)
                    detailCarViewModel.idCar = 0
                    detailCarViewModel.arg = null
                    val action = CarDetailFragmentDirections.actionCarDetailFragmentToNavigationHome()
                    this.findNavController().navigate(action)
                }

                //negative action
                builder.setNegativeButton(getString(R.string.delete_dialog_negative_button)) { _, _ ->

                }

                val alertDialog: AlertDialog = builder.create()
                //other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun deleteCar(id: Long) {
        detailCarViewModel.deleteCarById(id)
        notificationViewModel.deleteNotificationByCarId(id)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}