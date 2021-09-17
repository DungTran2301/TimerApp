package com.dungtran.alarmclock.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dungtran.alarmclock.alarmdata.Alarm

class AlarmViewModel(application: Application):AndroidViewModel(application) {
    private val readAllData: MutableLiveData<ArrayList<Alarm>> = MutableLiveData()



}