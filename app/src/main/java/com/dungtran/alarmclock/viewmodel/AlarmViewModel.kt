package com.dungtran.alarmclock.viewmodel

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.dungtran.alarmclock.alarmdata.Alarm
import com.dungtran.alarmclock.alarmdata.AlarmDatabase
import com.dungtran.alarmclock.alarmdata.AlarmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(private var alarmRepository: AlarmRepository) : ViewModel() {

    var allData: LiveData<List<Alarm>> = alarmRepository.getAllData()
    var hostAlarm: Alarm? = null

//    init {
//        viewModelScope.launch(Dispatchers.IO){
//            getData()
//        }
//    }
//
//    private fun getData(){
//        allData = alarmRepository.getAllData()
//    }

    suspend fun insertAlarm(alarm: Alarm) {
        alarmRepository.insertAlarm(alarm)
//        getData()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun changeStatus(alarm: Alarm, context: Context) {
        Log.d("Alarm Fragment", "${alarm.hours}:${alarm.minutes} - change status")
        if (alarm.isStart) {
//            viewModelScope.launch(Dispatchers.Default){
                Log.d("Alarm view model", "ON -> Off")
                alarm.cancelAlarm(context)
                updateAlarm(alarm)
//            }
        }
        else {
//            viewModelScope.launch(Dispatchers.Default){
                Log.d("Alarm view model", "Off -> ON")
                alarm.alarmSchedule(context)
                updateAlarm(alarm)
//            }
        }

    }

    fun updateAlarm(alarm: Alarm) {
        Log.d("Alarm view model", "updateAlarm: in update")
//        viewModelScope.launch(Dispatchers.IO) {
            alarmRepository.updateAlarm(alarm)
//            getData()
//        }
    }

    suspend fun deleteAlarm(alarm: Alarm, context: Context){
        alarm.cancelAlarm(context)
        alarmRepository.deleteAlarm(alarm)
//        getData()
    }

    companion object{
        private var instance: AlarmViewModel? = null
        fun getInstance(alarmRepository: AlarmRepository): AlarmViewModel{
            if(instance == null){
                instance = AlarmViewModel(alarmRepository)
            }
            return instance!!
        }
    }
}

class AlarmLiveDataFactory(context: Context) : ViewModelProvider.Factory {
    private val alarmDao = AlarmDatabase.getDatabase(context).alarmDAO()
    private val alarmRepository = AlarmRepository(alarmDao)
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AlarmViewModel.getInstance(alarmRepository) as T
    }
}