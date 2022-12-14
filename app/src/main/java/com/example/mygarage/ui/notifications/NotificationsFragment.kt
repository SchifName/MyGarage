package com.example.mygarage.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.mygarage.BaseApplication
import com.example.mygarage.databinding.FragmentNotificationsBinding
import com.example.mygarage.notificationManager.viewModelNotificationManager.NotificationManagerViewModel
import com.example.mygarage.notificationManager.viewModelNotificationManager.NotificationManagerViewModelFactory

class NotificationsFragment : Fragment() {
    private var _binding: FragmentNotificationsBinding? = null

    private val notificationViewModel: NotificationManagerViewModel by viewModels {
        NotificationManagerViewModelFactory(
            requireActivity().application,
            (activity?.application as BaseApplication).notDatabase.NotificationDao()
        )
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = NotificationListAdapter()


        val notificationSwipeGesture = object : NotificationSwipeGesture(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val notification = notificationViewModel.allNotification.value?.get(viewHolder.absoluteAdapterPosition)
                if (notification != null) {
                    notificationViewModel.deleteNotification(notification)
                }
            }
        }

        val touchHelper = ItemTouchHelper(notificationSwipeGesture)
        touchHelper.attachToRecyclerView(binding.recyclerView)

        notificationViewModel.allNotification.observe(this.viewLifecycleOwner) { notificationSelected ->
            notificationSelected.let {
                adapter.submitList(it)
            }
        }

        binding.apply {
            recyclerView.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}