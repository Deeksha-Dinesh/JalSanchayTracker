package com.jalsanchay.tracker.data.repository

import androidx.lifecycle.LiveData
import com.jalsanchay.tracker.data.db.RainfallDao
import com.jalsanchay.tracker.data.db.RainfallEntry

class WaterRepository(private val dao: RainfallDao) {

    fun getAllEntries(): LiveData<List<RainfallEntry>> = dao.getAllEntries()

    fun getTotalLitresSaved(): LiveData<Double?> = dao.getTotalLitresSaved()

    fun getMonthlyLitresSaved(month: String): LiveData<Double?> = dao.getMonthlyLitresSaved(month)

    fun getEntriesByMonth(month: String): LiveData<List<RainfallEntry>> = dao.getEntriesByMonth(month)

    fun getLastSevenEntries(): LiveData<List<RainfallEntry>> = dao.getLastSevenEntries()

    fun getDailyLitresSaved(date: String): LiveData<Double?> = dao.getDailyLitresSaved(date)

    fun getEntryCount(): LiveData<Int> = dao.getEntryCount()

    suspend fun insert(entry: RainfallEntry) = dao.insert(entry)

    suspend fun delete(entry: RainfallEntry) = dao.delete(entry)

    suspend fun getEntriesForDateOnce(date: String): List<RainfallEntry> = dao.getEntriesForDate(date)
}
