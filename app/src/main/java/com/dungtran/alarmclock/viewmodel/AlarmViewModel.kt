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
    private val _allData = MutableLiveData<List<Alarm>>()
    var allData: LiveData<List<Alarm>> = _allData
//
    init {
//        _allData.value = alarmRepository.getAllData().value
        viewModelScope.launch(Dispatchers.IO) {
            getData()
        }

    }
//    var allData: LiveData<List<Alarm>> = alarmRepository.getAllData()
//    val allData : LiveData<List<Alarm>> = liveData {
//        emitSource(alarmRepository.getAllData())
//    }


    private suspend fun getData(){
        _allData.postValue(alarmRepository.getAllData())
    }

    var hostAlarm: Alarm? = null

    suspend fun insertAlarm(alarm: Alarm) {
        alarmRepository.insertAlarm(alarm)
        getData()
    }

    suspend fun updateAlarm(alarm: Alarm) {
        alarmRepository.updateAlarm(alarm)

    }

    suspend fun deleteAlarm(alarm: Alarm, context: Context){
        alarm.cancelAlarm(context)
        alarmRepository.deleteAlarm(alarm)
        getData()
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