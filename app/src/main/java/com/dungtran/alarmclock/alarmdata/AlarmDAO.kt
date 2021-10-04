package com.dungtran.alarmclock.alarmdata

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlarm(alarm: Alarm)

    @Query("SELECT * FROM alarm_table ORDER BY alarmId ASC")
    fun getAllAlarm(): LiveData<List<Alarm>>

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)

    @Update//(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateAlarm(vararg alarm: Alarm)

    @Query("SELECT * FROM alarm_table WHERE alarmId=:alarmId")
    suspend fun getSingleAlarm(alarmId: Int): Alarm
}