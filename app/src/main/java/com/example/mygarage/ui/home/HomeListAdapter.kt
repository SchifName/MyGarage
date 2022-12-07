package com.example.mygarage.ui.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.get
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mygarage.databinding.CarItemBinding
import com.example.mygarage.model.*
import com.example.mygarage.R
import com.example.mygarage.model.Car
import com.squareup.picasso.Picasso

class HomeListAdapter(
    private val clickListener: (Car) -> Unit,
    private val logoDataApi: LiveData<List<CarLogo>>
) : ListAdapter<Car, HomeListAdapter.HomeViewHolder>(DiffCallback) {

    class HomeViewHolder(
        private var context: Context,
        private var binding: CarItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(car: Car, clickListener: (Car) -> Unit, logoDataApi: LiveData<List<CarLogo>>) {
            binding.car = car
            binding.carYearProduction.text = context.getString(R.string.year_detail_string, car.yearOfProduction.toString())
            /*binding.carFuelType.text = context.getString(R.string.fuel_type_detail_string, car.fuelType)
            binding.carPrice.text = context.getString(R.string.price_detail_string, formatCurrency(car.price))
            binding.carPower.text = context.getString(R.string.power_detail_string, carPowerWithUnitString(car.power))
            binding.carMileage.text = context.getString(R.string.mileage_detail_string, carMileageWithUnitString(car.mileage))*/
            binding.card.setOnClickListener {
                clickListener(car)
            }
            setAndGetUriByBrandParsingListOfLogoAndImageView(
                logoDataApi.value,
                car.brand,
                binding.carLogo
            )
            if (car.image != null) {
                val bmp = BitmapFactory.decodeByteArray(car.image, 0, car.image.size)
                binding.carImage.setImageBitmap(
                    bmp
                )
            } else {
                binding.carImage.setImageResource(R.drawable.ic_baseline_directions_car_filled_24)
            }
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Car>() {
        override fun areItemsTheSame(oldItem: Car, newItem: Car): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Car, newItem: Car): Boolean {
            return oldItem == newItem
        }

    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val car = getItem(position)
        holder.bind(car, clickListener, logoDataApi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val context = parent.context
        return HomeViewHolder(
            context,
            CarItemBinding.inflate(layoutInflater, parent, false),
        )
    }

}

fun setAndGetUriByBrandParsingListOfLogoAndImageView(
    logoDataApi: List<CarLogo>?,
    brand: String,
    logo: ImageView
) {
    logoDataApi?.forEach {
        if (it.name.equals(brand, ignoreCase = true)) {
            Picasso.get().load(it.logo).into(logo)
            return
        }
    }
    logo.setImageResource(R.drawable.ic_baseline_directions_car_filled_24)
}