package com.jalsanchay.tracker.data.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RainfallDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: RainfallEntry)

    @Delete
    suspend fun delete(entry: RainfallEntry)

    @Query("SELECT * FROM rainfall_entries ORDER BY date DESC")
    fun getAllEntries(): LiveData<List<RainfallEntry>>

    @Query("SELECT SUM(litresSaved) FROM rainfall_entries")
    fun getTotalLitresSaved(): LiveData<Double?>

    @Query("SELECT SUM(litresSaved) FROM rainfall_entries WHERE date LIKE :month || '%'")
    fun getMonthlyLitresSaved(month: String): LiveData<Double?>

    @Query("SELECT * FROM rainfall_entries WHERE date LIKE :month || '%' ORDER BY date DESC")
    fun getEntriesByMonth(month: String): LiveData<List<RainfallEntry>>

    @Query("SELECT SUM(litresSaved) FROM rainfall_entries WHERE date = :date")
    fun getDailyLitresSaved(date: String): LiveData<Double?>

    @Query("SELECT COUNT(*) FROM rainfall_entries")
    fun getEntryCount(): LiveData<Int>

    @Query("SELECT * FROM rainfall_entries ORDER BY date DESC LIMIT 7")
    fun getLastSevenEntries(): LiveData<List<RainfallEntry>>

    @Query("SELECT * FROM rainfall_entries WHERE date = :date")
    suspend fun getEntriesForDate(date: String): List<RainfallEntry>
}
