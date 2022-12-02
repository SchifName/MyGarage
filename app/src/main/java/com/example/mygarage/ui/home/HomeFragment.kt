package com.example.mygarage.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.mygarage.BaseApplication
import com.example.mygarage.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(
            (activity?.application as BaseApplication).database.CarDao()
        )
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*try {
            homeViewModel.addCar("ALFA ROMEO", "CIAO", 2022, 8, "DIESEL", 0.00, null, 80.0)
        } catch (e: java.lang.Exception)
        {
            Log.d("error1", e.toString())
        }*/

        val adapter = HomeListAdapter(clickListener = { car ->
            val action = HomeFragmentDirections
                .actionNavigationHomeToCarDetailFragment(car.id)
            findNavController().navigate(action)
        })

        homeViewModel.allCars.observe(this.viewLifecycleOwner) { carSelected ->
            carSelected.let {
                adapter.submitList(it)
            }
        }

        binding.apply {
            recyclerView.adapter = adapter
        }
        binding.fab.setOnClickListener {
                val action = HomeFragmentDirections.actionNavigationHomeToAddNewCarFragment()
                this.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}