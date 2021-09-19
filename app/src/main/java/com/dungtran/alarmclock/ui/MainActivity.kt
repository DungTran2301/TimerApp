package com.dungtran.alarmclock.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.dungtran.alarmclock.R
import com.dungtran.alarmclock.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setupWithNavController(findNavController(R.id.fragments))
//        binding.fragmentHost.findNavController().addOnDestinationChangedListener { _, destination, _ ->
//            when (destination.id) {
//                R.id.action_action_alarms_to_setAlarmFragment -> {
//                    binding.bottomNavigation.visibility = View.GONE
//                    binding.tvNameApp.visibility = View.GONE
//                }
//                else -> {
//                    binding.bottomNavigation.visibility = View.VISIBLE
//                    binding.tvNameApp.visibility = View.VISIBLE
//                }
//            }
//        }
    }
}