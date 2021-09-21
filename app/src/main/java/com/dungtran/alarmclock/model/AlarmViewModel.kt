package com.dungtran.alarmclock.model

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.dungtran.alarmclock.alarmdata.Alarm
import com.dungtran.alarmclock.alarmdata.AlarmDatabase
import com.dungtran.alarmclock.alarmdata.AlarmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(private var alarmRepository: AlarmRepository) : ViewModel() {
//    private var _allData: MutableLiveData<List<Alarm>> = alarmRepository.allData as MutableLiveData<List<Alarm>>
    val allData = liveData(Dispatchers.IO) {
        emitSource(alarmRepository.getAllData())
    }

    var position = 0


    fun update(newAlarm :Alarm){
//        _allData.value?.get(position) = newAlarm
        viewModelScope.launch(Dispatchers.IO) {
            updateAlarm(newAlarm)
        }
    }

    suspend fun updateAlarm(alarm: Alarm) {
        alarmRepository.updateAlarm(alarm)
    }

    suspend fun insertAlarm(alarm: Alarm) {
        alarmRepository.insertAlarm(alarm)
    }
    init {

//        val testAlarm = Alarm(12, 45, true, false, false, false, false, false, false, false, false)
//        viewModelScope.launch(Dispatchers.IO) {
//            alarmRepository.insertAlarm(testAlarm)
//                Log.d("AlarmViewmodel: ", " ${alarmRepository.getAllData().value?.size}")
//        }
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