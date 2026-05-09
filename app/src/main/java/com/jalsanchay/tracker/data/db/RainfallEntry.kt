package com.jalsanchay.tracker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rainfall_entries")
data class RainfallEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,               // Format: "yyyy-MM-dd"
    val rainfallMm: Double,
    val litresSaved: Double,
    val roofAreaSqFt: Double,
    val runoffCoefficient: Double
)
