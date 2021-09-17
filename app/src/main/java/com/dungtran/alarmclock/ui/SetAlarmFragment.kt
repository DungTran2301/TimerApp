package com.dungtran.alarmclock.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dungtran.alarmclock.R
import com.dungtran.alarmclock.databinding.FragmentSetAlarmBinding

class SetAlarmFragment : Fragment() {
    lateinit var binding: FragmentSetAlarmBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSetAlarmBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.fragment_set_alarm, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}