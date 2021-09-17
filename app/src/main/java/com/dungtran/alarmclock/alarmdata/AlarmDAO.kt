package com.dungtran.alarmclock.alarmdata

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AlarmDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAlarm(alarm: Alarm)

    @Query("SELECT * FROM alarm_table ORDER BY alarmId ASC")
    fun getAllAlarm(): LiveData<ArrayList<Alarm>>

    @Delete
    suspend fun deleteAlarm(alarm: Alarm)

    @Update
    suspend fun updateAlarm(alarm: Alarm)
}