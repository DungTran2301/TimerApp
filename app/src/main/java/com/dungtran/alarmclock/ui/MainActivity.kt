package com.dungtran.alarmclock.ui

import android.os.Bundle
import android.util.Log

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dungtran.alarmclock.R
import com.dungtran.alarmclock.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.bottomNavigation.setupWithNavController(findNavController(R.id.fragments))

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragments) as NavHostFragment
        binding.bottomNavigation.setupWithNavController(navHostFragment.navController)

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.d("from destination: ", "${destination.id}")
            when (destination.id) {
                R.id.setAlarmFragment -> {
                    Log.d("from destination: ", "to SetAlarmFragment")
                    binding.bottomNavigation.visibility = View.GONE
                    binding.tvNameApp.visibility = View.GONE
                }
                else -> {
                    binding.bottomNavigation.visibility = View.VISIBLE
                    binding.tvNameApp.visibility = View.VISIBLE
                }
            }
        }
    }
}