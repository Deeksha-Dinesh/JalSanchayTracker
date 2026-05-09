package com.jalsanchay.tracker.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jalsanchay.tracker.data.db.AppDatabase
import com.jalsanchay.tracker.data.db.RainfallEntry
import com.jalsanchay.tracker.data.repository.WaterRepository
import com.jalsanchay.tracker.utils.WaterCalculator
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class WaterViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).rainfallDao()
    private val repository = WaterRepository(dao)

    private val prefs = application.getSharedPreferences("user_setup", Context.MODE_PRIVATE)

    // ── Observables ──────────────────────────────────────────────────
    val allEntries: LiveData<List<RainfallEntry>> = repository.getAllEntries()
    val totalLitresSaved: LiveData<Double?> = repository.getTotalLitresSaved()
    val entryCount: LiveData<Int> = repository.getEntryCount()
    val lastSevenEntries: LiveData<List<RainfallEntry>> = repository.getLastSevenEntries()

    // ── Events ────────────────────────────────────────────────────────
    private val _duplicateEntryEvent = MutableLiveData<Boolean>()
    val duplicateEntryEvent: LiveData<Boolean> = _duplicateEntryEvent

    // ── User Setup (SharedPreferences) ────────────────────────────────
    var roofAreaSqFt: Double
        get() = prefs.getFloat("roof_area", 500f).toDouble()
        set(v) = prefs.edit().putFloat("roof_area", v.toFloat()).apply()

    var tankCapacityLitres: Double
        get() = prefs.getFloat("tank_capacity", 1000f).toDouble()
        set(v) = prefs.edit().putFloat("tank_capacity", v.toFloat()).apply()

    var runoffCoefficient: Double
        get() = prefs.getFloat("runoff_coeff", 0.85f).toDouble()
        set(v) = prefs.edit().putFloat("runoff_coeff", v.toFloat()).apply()

    val isSetupDone: Boolean
        get() = prefs.getBoolean("setup_done", false)

    fun markSetupDone() = prefs.edit().putBoolean("setup_done", true).apply()

    // ── Actions ───────────────────────────────────────────────────────
    fun saveRainfallEntry(rainfallMm: Double) {
        val litres = WaterCalculator.calculateLitresSaved(roofAreaSqFt, rainfallMm, runoffCoefficient)
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        viewModelScope.launch {
            val existingEntries = repository.getEntriesForDateOnce(today)
            if (existingEntries.isNotEmpty()) {
                _duplicateEntryEvent.postValue(true)
                return@launch
            }
            val entry = RainfallEntry(
                date = today,
                rainfallMm = rainfallMm,
                litresSaved = litres,
                roofAreaSqFt = roofAreaSqFt,
                runoffCoefficient = runoffCoefficient
            )
            repository.insert(entry)
        }
    }

    fun deleteEntry(entry: RainfallEntry) {
        viewModelScope.launch { repository.delete(entry) }
    }

    fun getMonthlyEntries(month: String): LiveData<List<RainfallEntry>> {
        return repository.getEntriesByMonth(month)
    }

    fun getMonthlyLitres(month: String): LiveData<Double?> {
        return repository.getMonthlyLitresSaved(month)
    }

    fun getDailyLitres(date: String): LiveData<Double?> {
        return repository.getDailyLitresSaved(date)
    }

    fun currentMonth(): String =
        SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date())

    fun currentMonthName(): String =
        SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(Date())
}
