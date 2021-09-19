package com.dungtran.alarmclock.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dungtran.alarmclock.alarmdata.Alarm
import com.dungtran.alarmclock.alarmdata.AlarmRepository
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application) : AndroidViewModel(application) {
    private var _allData: MutableLiveData<ArrayList<Alarm>> = MutableLiveData()
    val allData: LiveData<ArrayList<Alarm>> = _allData

    var position = 0

    private var alarmRepository: AlarmRepository = AlarmRepository(application)

    init {
        _allData = alarmRepository.allData as MutableLiveData<ArrayList<Alarm>>
    }

    fun update(newAlarm :Alarm){
        _allData.value?.set(position, newAlarm)
        viewModelScope.launch {
            updateAlarm(newAlarm)
        }
    }

    suspend fun updateAlarm(alarm: Alarm) {
        alarmRepository.updateAlarm(alarm)
    }

    suspend fun insertAlarm(alarm: Alarm) {
        alarmRepository.insertAlarm(alarm)
    }



}